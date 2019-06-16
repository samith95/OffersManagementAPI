package api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import api.models.Offer;
import api.service.OfferService;

/*
 * 
 * RESTful API endpoints
 *
 */

@Controller
@RequestMapping(value = "/offer/")
public class OfferController {
	
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private OfferService offerService;

    /**
     * Endpoint utilised to create a new Offer
     * 
     * @param offer is the offer to be created and saved into the db
     * @param request	Http request to create a new offer
     * @param response	201 STATUS_CREATED on success
     */
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createOffer (Offer offer, HttpServletRequest request, HttpServletResponse response) {
        long createdOfferID = this.offerService.createOffer(offer);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdOfferID).toString());
        log.info("offer: %s has been created successfully", Long.toString(offer.getId()));
    }
    

    /**
     * Endpoint utilised to retrieve an Offer
     * 
     * @param id of the wanted offer
     * @param request Http request to retrieve the offer
     * @param response 200 STATUS_OK on success
     * @return	the requested offer
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public Offer getOffer(Long id, HttpServletRequest request, HttpServletResponse response) {
        Offer returnedOffer = this.offerService.getOffer(id);
        if (returnedOffer == null) {
           log.info("getOffer(): resource not found");
        }
        return returnedOffer;
    }
}
