package api;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import api.Application;
import api.OfferController;
import api.model.Offer;
import api.util.Consts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes=Application.class)
@ActiveProfiles("test")
public class OfferRequestTest {

    @InjectMocks
    OfferController controller;

    @Autowired
    WebApplicationContext context;
    
    private MockMvc mvc;
    
    private String offerPageURL = "/offer/";

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @Transactional
    public void createAndValidateAndCancelAndValidate() throws Exception {
    	//set up test data
        Offer offer = mockOffer("createAndValidateAndCancelAndValidate");
        
        //create offer and set offer ID
        offer.setId(createOfferAndReturnID(offer));
        
        //the expected offer should now be Cancelled, hence set offer to cancelled and check the response
        offer.setStatus(Offer.VALID__STATUS_OFFER_STRING);
        //query the just created offer and validate its content
        retrieveAndValidateOffer(offer);
        
        //cancel the just created offer
        cancelOffer(offer.getId());
        
        //the expected offer should now be Cancelled, hence set offer to cancelled and check the response
        offer.setStatus(Offer.CANCELLED__STATUS_OFFER_STRING);
        //query the just updated offer, validate its content and check that it has been cancelled
        retrieveAndValidateOffer(offer);
    }
    
	/**
	 * Creates an offer using the passed offer object and returns its offer ID
	 * <p>
	 * The function sends a POST request to the API to create an offer
	 * The API then sends the Offer ID (which is generated in the API) back 
	 *
	 * @param	expectedOffer	is the offer to be created by the API
	 * @return	the Offer ID created by the API
	 */
    public long createOfferAndReturnID(Offer expectedOffer) throws Exception {
        byte[] offJson = toJson(expectedOffer);

        MvcResult result = mvc.perform(post(offerPageURL)
                .content(offJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(expectedOffer.getDescription())))
                .andReturn();
        
        String response = result.getRequest().getContentAsString();
        //retrieve ID from response and convert it to long variable
        return Long.parseLong(JsonPath.parse(response).read("$[0].id"));
    }
    
	/**
	 * Retrieves and validate an offer against the passed expectedOffer
	 * <p>
	 * The function sends a GET request to the API to retrieve the expectedOffers' ID
	 * The API then sends the requested Offer back
	 * The function then validates the content of the response against the passed expectedOffer 
	 *
	 * @param	offer	is the offer to be created by the API
	 */
    public void retrieveAndValidateOffer(Offer expectedOffer) throws Exception {
        mvc.perform(get(offerPageURL + expectedOffer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedOffer.getId())))
                .andExpect(jsonPath("$.productID", is(expectedOffer.getProductID())))
                .andExpect(jsonPath("$.description", is(expectedOffer.getDescription())))
                .andExpect(jsonPath("$.price", is(expectedOffer.getPrice())))
                .andExpect(jsonPath("$.currencyCode", is(expectedOffer.getCurrencyCode())))
		        .andExpect(jsonPath("$.createdOn", is(expectedOffer.getCreatedOn())))
		        .andExpect(jsonPath("$.daysValidFor", is(expectedOffer.getDaysValidFor())))
		        .andExpect(jsonPath("$.status", is(expectedOffer.getDaysValidFor())));
    }
    
	/**
	 * Cancels the offer specified in offerID passed parameter
	 * <p>
	 * The function sends a PUT request to the API to edit the status variable of the offer
	 *
	 * @param	offerID	defines the offer ID of the offer to be cancelled by the API
	 */
    public void cancelOffer(long offerID) throws Exception {
        mvc.perform(put(offerPageURL + Long.toString(offerID))
                .content(new String("{\"status\":\""+Offer.CANCELLED__STATUS_OFFER_STRING+"\"}").getBytes())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
    
	/**
	 * Creates a mock Offer object initialised with the prefix passed as parameter
	 * <p>
	 * The function creates an Offer object and set its variables to test data
	 * In order to keep traceability, the prefix (string passed as parameter)
	 * should be used by calling functions to state the function name. Such variable will
	 * be saved in the description of the offer.
	 *
	 * @param	prefix	should state the calling function name for traceability
	 * @return	the test offer object to be used by the test functions
	 */
    private Offer mockOffer(String prefix) {
        Offer o = new Offer();
        o.setProductID(4621346);
        o.setDescription(prefix);
        o.setPrice(new BigDecimal("100.01"));
        o.setCurrencyCode("EUR");
        o.setCreatedOn(LocalDate.now().format(DateTimeFormatter.ofPattern(Consts.TIMEFORMAT)));
        o.setDaysValidFor(20);
        return o;
    }
    
	/**
	 * Converts a passed object into a byte array
	 *
	 * @param	obj	object to be converted into byte array
	 * @return	byte array of the passed object
	 */
    private byte[] toJson(Object obj) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(obj).getBytes();
    }
}
