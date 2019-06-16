package api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.models.Offer;
import api.repository.OfferRepository;

@Service
public class OfferService {
	
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
	private OfferRepository offerRepository;
    	
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
    	//TODO: check whether specified productID exists
    	Offer returnedOffer = offerRepository.save(offer);
		if (returnedOffer == null){
			log.info("createOffer(): failed to create offer in db");
		}
		return returnedOffer.getId();
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
		Offer returnedOffer = offerRepository.findById(id).orElse(null);
		if(returnedOffer == null) {
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
