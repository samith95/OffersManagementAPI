package api.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.model.Offer;
import api.model.Product;
import api.repository.OfferRepository;
import api.repository.ProductRepository;
import api.util.Consts;

@Service
public class OfferService {
	
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private OfferRepository offerRepository;
    
    @Autowired
	private ProductRepository productRepository;
    	
	/**
	 * creates an offer and saves it into the db
	 * 
	 * <p>
	 * if no ID is returned, it means that the function failed to create a new record
	 * 
	 * @param	offer	defines offer to be saved in db
	 * @return  the generated offer ID
	 */
    @Transactional
	public long createOffer(Offer offer) {
    	
    	//initialise offers default values
    	offer = intialiseOffer(offer);
    	
    	offer = checkOfferValidity(offer);
    	
    	//checks if product ID is present from returned offer
    	if(offer.getProductID() == new Offer().getProductID()) {
    		return new Long(-400);
    	}
    	
    	Offer returnedOffer = offerRepository.save(offer);
		if (returnedOffer == null){
			log.info("createOffer(): failed to create offer in db");
			return new Long(-500);
		}
		
		return returnedOffer.getId();
	}
    
	/**
	 * retrieves offer from the db
	 * 
	 * <p>
	 * if no offer is returned, it means that the function failed to find such offer
	 * 
	 * @param	id	of the offer to be retrieved
	 * @return  the retrieved offer
	 */
	public Offer getOffer(long id) {
		Offer off = findOfferByID(id);
		//check whether returned offer is empty
		if(off.getId() == new Offer().getId()) {
			return off;
		}
		//check status of the offer by analysing its validity
		off = checkStatus(off);
		return off;
	}
    
	/**
	 * cancels the offer of which id is passed as parameter
	 * 
	 * <p>
	 * this function retrieves the offer from the db, it changes its status
	 * (if not already cancelled) and saves it back into the db
	 * 
	 * @param	id	of the offer to be cancelled
	 * @return true if successful or false if offer wans't found
	 */
    @Transactional
	public boolean cancelOffer(long id) {
		//check whether offer exists in db
		Offer off = findOfferByID(id);
		if(off.getId() == new Offer().getId()) {
	    	log.info(String.format("cancelOffer(): offer %s could not be cancelled", 
	    			Long.toString(id)));
			return false;
		}
		
		//check if offer is already cancelled, if it is, then skip status 
		//change and save new status in db
		if(!off.getStatus().equals(Offer.CANCELLED__STATUS_OFFER_STRING)) {
	    	//if not cancelled then update the offer status, if the update fails, return false
			off.setStatus(Offer.CANCELLED__STATUS_OFFER_STRING);
			off = saveOffer(off);
			//check for errors in saving
			if (off == null) {
				return false;
			}
		}
		return true;
	}
	
    
    /**
     * implements logic to validate an offer
     * 
     * <p>
     * - checks if the productID of the passed offer exists in the product table of the db
     * - checks the status of the offer and updates it if needed
     * 
     * @param offer to be validated
     * @return the validated offer
     */
    private Offer checkOfferValidity(Offer offer) {
    	//check whether product exists in db
    	Product prod = productRepository.findById(offer.getProductID()).orElse(null);
    	if(prod == null) {
			log.info(String.format("createOffer(): the requested product id does not exist", 
					Long.toString(offer.getProductID())));
			return new Offer();
		}
    	
    	 return checkStatus(offer);
    }
    
    /**
     * implements initialisation logic by which any new offer must comply
     * 
     * <p>
     * - sets the current date as the createdOn date of the offer
     * - sets the status as undefined
     * 
     * @param offer represents a simple offer to be initialised with default values
     * @return the initialised offer
     */
    private Offer intialiseOffer(Offer offer) {
    	//the createdOn date is initialised to the day the request has been done
		offer.setCreatedOn(LocalDate.now().format(Consts.DATEFORMATTER));
		//a newly defined Offer will have undefined status
		offer.setStatus(Offer.UNDEFINED__STATUS_OFFER_STRING);
		return offer;
	}
	
    
    /**
     * implements the logic by which offer's status work
     * 
     * <p>
     * if a cancelled or expired offer is passed, the function will
     * return it back straight away, however, if the status is undefined
     * or valid, it will check the offers validity and initialise the offer
     * object accordingly
     * 
     * @param offer of which status should be checked
     * @return the offer with validated status
     */
    private Offer checkStatus(Offer offer) {
    	
    	//status check logic
    	switch(offer.getStatus()) {
    		//if offer is cancelled or expired return to calling function
    		case Offer.CANCELLED__STATUS_OFFER_STRING:
    		case Offer.EXPIRED__STATUS_OFFER_STRING:
    			return offer;
    		case Offer.UNDEFINED__STATUS_OFFER_STRING:
    		case Offer.VALID__STATUS_OFFER_STRING:
    			offer = checkIfValid(offer);
    			break;
    		default:
    			log.error(String.format("checkStatus(): status %s not recognised", 
    					offer.getStatus()));
    	}
    	    	
    	return offer;
    }
    
    /**
     * checks whether the offer is valid or expired
     * 
     * <p>
     * this is done by analysing the offer's validity timeframe
     * and in case of expired offer, the function will update the 
     * offer's status, save the offer into the db and return the
     * newly saved object back
     * 
     * @param offer
     * @return offer with correct status
     */
    private Offer checkIfValid(Offer offer) { 	
    	try {
        	LocalDate createdDate = LocalDate.parse(offer.getCreatedOn(), Consts.DATEFORMATTER);
        	LocalDate currentDate = LocalDate.now();
        	LocalDate validTimeFrame = createdDate.plusDays(offer.getDaysValidFor());
        	
        	//if the offer is outside its validity timeframe, then it is expired
        	if(currentDate.isAfter(validTimeFrame)) {
        		offer.setStatus(Offer.EXPIRED__STATUS_OFFER_STRING);
        		//in case it is expired, the offer is directly saved
       			offer = saveOffer(offer);
        		return offer;
        	}
        	
        	//if the offer is undefined and is not expired, then it must be valid
        	if(offer.getStatus().equals(Offer.UNDEFINED__STATUS_OFFER_STRING)) {
        		offer.setStatus(Offer.VALID__STATUS_OFFER_STRING);
        	}
        	
    	} catch (DateTimeException e) {
			log.info(String.format("checkIfValid(): internal error when parsing offer "
					+ "%s\nstacktrace: %s", Long.toString(offer.getId()), 
					e.getStackTrace().toString()));	
    	}
    	return offer;
    }
    
    /**
     * saves passed offer into db
     * 
     * @param offer to be saved
     * @return offer just saved in db (contains OfferID)
     */
    private Offer saveOffer(Offer offer) {
		Offer off = offerRepository.save(offer);
		if (off == null){
			log.info(String.format("saveOffer(): failed to create offer in db"));
		}
		return off;
    }
	
	
	/**
	 * function used to retrieve the offers by ID
	 * 
	 * <p>
	 * in case the offer is not found, an empty Offer is passed,
	 * the calling function must check whether the returned offer
	 * obj is empty
	 * 
	 * @param id id of the offer to be retrieved
	 * @return offer from db on success, empty offer otherwise
	 */
	public Offer findOfferByID(long id) {
		Offer off = offerRepository.findById(id).orElse(new Offer());
		//check if 
		if(off.getId() == new Offer().getId()) {
			log.info(String.format("findOfferByID(): failed to get offer %s in db", 
					Long.toString(id)));
		}
		return off;
	}
}
