package api;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import api.model.Offer;
import api.service.OfferService;

/**
 * RESTful API end points
 * 
 * @author Samith Silva
 *
 */
@Controller
@RequestMapping(value = "/offer")
public class OfferController implements ApplicationEventPublisherAware{
	
    protected ApplicationEventPublisher eventPublisher;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    //used to store stacktrace in the logs in case of failures
	private StringWriter errors = new StringWriter();

    @Autowired
    private OfferService offerService;

    /**
     * End point utilised to create a new Offer
     * 
     * @param offer is the offer to be created and saved into the db
     * @param request	HTTP request to create a new offer
     * @param response	201 CREATED on success, 400 BAD REQUEST if 
     * passed offer is not correct (e.g. productID does not exist), 
     * 500 INTERNAL_SERVER_ERROR if the offer could not be saved in db or
     * in case of exception
     * @return id of the newly created offer or empty object otherwise
     */
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public
    @ResponseBody ResponseEntity<Object>
    createOfferHandler (@RequestBody Offer offer, HttpServletRequest request, HttpServletResponse response) {
    	log.info(String.format("createOfferHandler(): offer received to be created", Long.toString(offer.getId())));
        
    	HashMap<String, String> map = new HashMap<String, String>();
    	
    	String errorMsg = "";
    	
    	//used to retrieve the auto generated offer id from the db
    	long createdOfferID = 0;
    	
    	try {
	    	createdOfferID = this.offerService.createOffer(offer);
	    	//CHECK INTERNAL ERROR CODES
	    	//check whether the passed product ID exists in db 
	    	if (createdOfferID == -400) {
	    		errorMsg = "specified productID does not exist in db";
		        log.info(String.format("createOfferHandler(): %s", errorMsg));
		        map.put("error:", errorMsg);
				return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
	    	}
	    	
	    	//check whether it has been saved successfully into the db
	    	if (createdOfferID == -500) {
	    		errorMsg = "offer failed to be saved into the db";
		        log.info(String.format("createOfferHandler(): %s", errorMsg));
		        map.put("error:", errorMsg);
				return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
	    	
		} catch (Exception e) {
    		errorMsg = String.format("offer %s could not be created, stacktrace: %s", Long.toString(createdOfferID), errors);
	        log.info(String.format("createOfferHandler(): %s", errorMsg));
	        map.put("error:", "exception thrown in internal createOfferHandler(), please contact Samith Silva");
			return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    	//on success, send back id of newly created offer
        log.info(String.format("createOfferHandler(): offer %s has been created successfully", Long.toString(createdOfferID)));
        
    	map.put("id", Long.toString(createdOfferID));
    	
		return new ResponseEntity<Object>(map, HttpStatus.CREATED);
    }
    

    /**
     * End point utilised to retrieve an Offer
     * 
     * @param id of the wanted offer
     * @param request HTTP request to retrieve the offer
     * @param response 200 STATUS_OK on success, 404 NOT_FOUND if the offer is not found
     * 500 INTERNAL_SERVER_ERROR in case of thrown exception
     * @return	the requested offer on success, empty otherwise
     */
	@RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    public
    @ResponseBody ResponseEntity<Object>
    getOfferHandler(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
    	log.info(String.format("getOfferHandler(): offer %s received to be retrieved", Long.toString(id)));
    	
    	Offer returnedOffer = new Offer();
    	
	    try {
	    	returnedOffer = this.offerService.getOffer(id);
	        
	        if (returnedOffer.getId() == 0) {
	           log.info(String.format("getOfferHandler(): offer %x not found", id));
	           return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
	        }
	        
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(errors));
	        log.info(String.format("getOfferHandler(): offer %s could not be retrieved, stacktrace: %s", Long.toString(id), errors));
			return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
        log.info(String.format("getOfferHandler(): offer %x was succesffuly found status: %s", id, returnedOffer.getStatus()));
        return new ResponseEntity<Object>(returnedOffer, HttpStatus.OK);
    }
	
	
    /**
     * End point utilised to cancel an Offer
     * 
     * @param id of the wanted offer
     * @param request HTTP request to retrieve the offer
     * @param response 204 NO_CONTENT on success, 404 NOT_FOUND if offer is not found
     * 500 INTERNAL_SERVER_ERROR in case thrown expection
     */
    @RequestMapping(value = "/cancel/{id}",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public 
    @ResponseBody ResponseEntity<Object> 
    cancelOfferHandler(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
    	log.info(String.format("cancelOfferHandler(): offer %s received to be cancelled", Long.toString(id)));
    	
    	try {
    	
	    	if(!this.offerService.cancelOffer(id)) {
	    		//in case offer is not found.
		        log.info(String.format("cancelOfferHandler(): offer %s could not be found", Long.toString(id)));
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    	} else {
	    		//in case offer is cancelled
		        log.info(String.format("cancelOfferHandler(): offer %s has been cancelled", Long.toString(id)));
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    	}
    	
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(errors));
	        log.info(String.format("cancelOfferHandler(): offer %s could not be cancelled, stacktrace: %s", Long.toString(id), errors));
			return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * mandatory function when implementing ApplicationEventPublisherAware interface
     */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher aep) {
        this.eventPublisher = aep;
	}
}
