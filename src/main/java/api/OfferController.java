package api;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import api.models.Offer;
import api.service.OfferService;

/*
 * 
 * RESTful API end points
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
     * @param response	201 STATUS_CREATED on success
     */
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public
    @ResponseBody Map<String, String> 
    createOffer (@RequestBody Offer offer, HttpServletRequest request, HttpServletResponse response) {
    	long createdOfferID = 0;
    	try {
    	createdOfferID = this.offerService.createOffer(offer);
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(errors));
	        log.info("createOffer(): offer: %s could not be created, stacktrace: %s", Long.toString(createdOfferID), errors);
	        return null;
		}
        log.info("offer: %s has been created successfully", Long.toString(createdOfferID));
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("id", Long.toString(createdOfferID));
		return map;
    }
    

    /**
     * End point utilised to retrieve an Offer
     * 
     * @param id of the wanted offer
     * @param request HTTP request to retrieve the offer
     * @param response 200 STATUS_OK on success
     * @return	the requested offer
     */
	@RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    public
    @ResponseBody ResponseEntity<Map<String,Object>>
    getOffer(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
        Offer returnedOffer = this.offerService.getOffer(id);
   
        //map utilised to parse the object into a map
        ObjectMapper objMapper = new ObjectMapper();
        Map<String, Object> objMap = new HashMap<>();
    	
        if (returnedOffer.getId() == 0) {
           log.info("getOffer(): resource not found");
           return new ResponseEntity<Map<String,Object>>(objMap, HttpStatus.NOT_FOUND);
        }

        objMap = objMapper.convertValue(returnedOffer, Map.class);

        return new ResponseEntity<Map<String, Object>>(objMap, HttpStatus.OK);
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher aep) {
        this.eventPublisher = aep;
	}
}
