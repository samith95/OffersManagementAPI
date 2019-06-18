package api.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.models.Offer;
import api.models.Product;
import api.repository.OfferRepository;
import api.repository.ProductRepository;
import api.util.Consts;

@Service
public class OfferService {
	
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
	static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Consts.TIMEFORMAT);
	
    @Autowired
	private OfferRepository offerRepository;
    
    @Autowired
	private ProductRepository productRepository;
    	
	/**
	 * creates an offer and saves it into the db
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
    	
    	Offer returnedOffer = offerRepository.save(offer);
		if (returnedOffer == null){
			log.info("createOffer(): failed to create offer in db");
		}
		
		return returnedOffer.getId();
	}
    
    private Offer checkOfferValidity(Offer offer) {
    	//check whether product exists in db
    	Product returnedProduct = productRepository.findById(offer.getProductID()).orElse(null);
    	if(returnedProduct == null) {
			log.info("createOffer(): the requested product id does not exist", Long.toString(offer.getProductID()));
		}
    	
    	 return checkStatus(offer);
    }
    
    private Offer intialiseOffer(Offer offer) {
    	//the createdOn date is initialised to the day the request has been done
		offer.setCreatedOn(LocalDate.now().format(DateTimeFormatter.ofPattern(Consts.TIMEFORMAT)));
		//a newly defined Offer will have undefined status
		offer.setStatus(Offer.UNDEFINED__STATUS_OFFER_STRING);
		return offer;
	}
	
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
    			log.error("checkStatus(): status %s not recognised", offer.getStatus());
    	}
    	    	
    	return offer;
    }
    
    private Offer checkIfValid(Offer offer) { 	
    	try {
        	LocalDate createdDate = LocalDate.parse(offer.getCreatedOn(), dateFormatter);
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
			log.info("checkIfValid(): internal error when parsing offer %s\nstacktrace: %s", Long.toString(offer.getId()), e.getStackTrace().toString());	
    	}
    	return offer;
    }
    
    private Offer saveOffer(Offer offer) {
		Offer dbOffer = offerRepository.save(offer);
		if (dbOffer == null){
			log.info("saveOffer(): failed to create offer in db");
		}
		return dbOffer;
    }
	
	/**
	 * retrieves offer from the db
	 * <p>
	 * if no offer is returned, it means that the function failed to find such offer
	 * 
	 * @param	id	of the offer to be retrieved
	 * @return  the retrieved offer
	 */
	public Offer getOffer(long id) {
		Offer returnedOffer = offerRepository.findById(id).orElse(new Offer());
		if(returnedOffer == new Offer()) {
			log.info("getOffer(): failed to get offer %s in db", Long.toString(id));
		}
		return returnedOffer;
	}
	
	/**
	 * updates the status of an offer
	 * <p>
	 * if multiple rows are modified this function will throw an exception
	 * 
	 * @param	id	of the offer to be updated
	 * @param	status	new status by which the offer must be updated
	 * @return  true if successfull update, false otherwise
	 */
	public boolean updateOfferStatus(long id, String status) {
		int modifiedRowCount = offerRepository.updateOfferStatus(status, Long.toString(id));
		if(modifiedRowCount == 0) {
			log.info("updateOfferStatus(): failed to update offer \"%s\" in db", Long.toString(id));
			return false;
		}
		if(modifiedRowCount>1) {
			log.info("updateOfferStatus(): multiple row (%d) updated when updating offer \"%s\" in db", 
					modifiedRowCount, Long.toString(id));
			throw new RuntimeException("Internal error, multiple rows updated");
		}
		return true;
	}
}
