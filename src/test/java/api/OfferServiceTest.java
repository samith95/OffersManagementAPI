/**
 * 
 */
package api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import api.model.Offer;
import api.model.Product;
import api.repository.OfferRepository;
import api.repository.ProductRepository;
import api.service.OfferService;

/**
 * Unit tests for the OfferService class
 * 
 * @author Samith Silva
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes=Application.class)
@ActiveProfiles("test")
public class OfferServiceTest {
	
    @Autowired
    private OfferService offerService = new OfferService();
    
    @Autowired
	private OfferRepository offerRepository;
    
    @Autowired
	private ProductRepository productRepository;
    
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
	private Map<String, Object> testcase = new HashMap<String, Object>();

	/**
	 * Test method for {@link api.service.OfferService#createOffer(api.models.Offer)}.
	 */
	@Test
	public void testCreateOffer() {
			
		//arraylist to define all scenarios using table driven test methodology
		ArrayList<Map<String, Object>> testcasesList = new ArrayList<Map<String, Object>>();
		
		//save valid product into product repository for successful offer creation
		Product validProduct = TestObject.mockProduct("successful scenario - valid productID");
		validProduct = productRepository.save(validProduct);
		
		//successful scenario - valid productID
		Offer off = TestObject.mockOffer("successfull offer creation");
		off.setProductID(validProduct.getId());
		testcasesList = addTestcase(testcasesList, "successful offer creation - valid productID", off, 1);

		//unsuccessful scenario - wrong productID
		off = TestObject.mockOffer("unsuccessful scenario - wrong productID");
		off.setProductID(1111111111L);
		testcasesList = addTestcase(testcasesList, "unsuccessful offer creation - wrong productID", off, -400L);
		
		//result returned back from the function
		long actualResult;
		
		//check each testcase
		for(Map<String, Object> tc : testcasesList) {
			log.info(String.format("testCreateOffer(): executing test: %s", tc.get("name")));
			actualResult = offerService.createOffer(Offer.class.cast(tc.get("testData")));
			assertEquals(actualResult, Long.parseLong(tc.get("expected").toString()));
		}
	}

	/**
	 * Test method for {@link api.service.OfferService#getOffer(long)}.
	 */
	@Test
	public void testGetOffer() {
		
		//save offer that is now expired
		Offer expiredOffer = TestObject.mockOffer("valid in db but expired offer saving");
		expiredOffer.setCreatedOn("01/01/1980");
		expiredOffer.setStatus(Offer.VALID__STATUS_OFFER_STRING);
		expiredOffer = offerRepository.save(expiredOffer);
		
		//arraylist to define all scenarios using table driven test methodology
		ArrayList<Map<String, Object>> testcasesList = new ArrayList<Map<String, Object>>();
		
		//retrieve expired offer - valid in db but expired
		Offer expectedOffer = expiredOffer;
		expectedOffer.setStatus(Offer.EXPIRED__STATUS_OFFER_STRING);
		testcasesList = addTestcase(testcasesList, "retrieve expired offer - valid in db but expired", expiredOffer.getId(), expectedOffer);
		
		//unsuccessful scenario - search for a non saved offerID
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - search for a non saved offerID", -5L, new Offer());
		
		//result returned back from the function
		Offer actualResult;
		
		//check each testcase
		for(Map<String, Object> tc : testcasesList) {
			log.info(String.format("testGetOffer(): executing test: %s", tc.get("name")));
			actualResult = offerService.getOffer(Long.parseLong(tc.get("testData").toString()));
			assertEquals(actualResult.getId(), Offer.class.cast(tc.get("expected")).getId());
		}
	}

	/**
	 * Test method for {@link api.service.OfferService#cancelOffer(long)}.
	 */
	@Test
    @Transactional
	public void testCancelOffer() {

		//save expired offer
		Offer expiredOffer = TestObject.mockOffer("expired offer saving");
		expiredOffer.setStatus(Offer.EXPIRED__STATUS_OFFER_STRING);
		expiredOffer = offerRepository.save(expiredOffer);
		
		//save cancelled offer
		Offer cancelledOffer = TestObject.mockOffer("cancelled offer saving");
		cancelledOffer.setStatus(Offer.CANCELLED__STATUS_OFFER_STRING);
		cancelledOffer = offerRepository.save(cancelledOffer);
		
		//save valid offer
		Offer validOffer = TestObject.mockOffer("valid offer saving");
		validOffer.setStatus(Offer.VALID__STATUS_OFFER_STRING);
		validOffer = offerRepository.save(validOffer);
		
		//arraylist to define all scenarios using table driven test methodology
		ArrayList<Map<String, Object>> testcasesList = new ArrayList<Map<String, Object>>();
		
		//successful scenario - cancel valid offer - function should return true, signaling the change of status from valid to cancelled
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - cancel valid offer", validOffer.getId(), true);
		
		//unsuccessful scenario - cancel expired offer
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - cancel expired offer", expiredOffer.getId(), false);
		
		//unsuccessful scenario - cancel cancelled offer
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - cancel cancelled offer", cancelledOffer.getId(), false);

		//unsuccessful scenario - cancel an unexisting offer
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - cancel valid offer", validOffer.getId(), true);

		//result returned back from the function
		boolean actualResult;
		
		//check each testcase
		for(Map<String, Object> tc : testcasesList) {
			log.info(String.format("testCancelOffer(): executing test: %s", tc.get("name")));
			actualResult = offerService.cancelOffer(Long.parseLong(tc.get("testData").toString()));
			assertEquals(actualResult, (Boolean)tc.get("expected"));
		}
	}

	/**
	 * Test method for {@link api.service.OfferService#findOfferByID(long)}.
	 */
	@Test
	public void testFindOfferByID() {		
		//save valid offer to be retrieved
		Offer savedOffer = TestObject.mockOffer("valid offer saving");
		savedOffer.setStatus(Offer.VALID__STATUS_OFFER_STRING);
		savedOffer = offerRepository.save(savedOffer);
		
		//arraylist to define all scenarios using table driven test methodology
		ArrayList<Map<String, Object>> testcasesList = new ArrayList<Map<String, Object>>();
		
		//successful scenario - retrieve existing offer
		testcasesList = addTestcase(testcasesList, "successful scenario - retrieve existing offer", savedOffer.getId(), savedOffer);
		
		//unsuccessful scenario - retrieve non-existing offer
		testcasesList = addTestcase(testcasesList, "unsuccessful scenario - retrieve non-existing offer", -5L, new Offer());
		
		//result returned back from the function
		Offer actualResult;
		
		//check each testcase
		for(Map<String, Object> tc : testcasesList) {
			log.info(String.format("testCancelOffer(): executing test: %s", tc.get("name")));
			actualResult = offerService.findOfferByID(Long.parseLong(tc.get("testData").toString()));
			assertEquals(actualResult.getId(), Offer.class.cast(tc.get("expected")).getId());
		}
	}
	
	
	/**
	 * utilised for table driven tests, this function adds a testcase to the returned list
	 * 
	 * @param list	of testcases to be executed
	 * @param name	of the testcase to add
	 * @param testData	needed for the test to execute
	 * @param expected	result from the test execution
	 * @return	passed list with appended a new testcase
	 */
	private ArrayList<Map<String, Object>> addTestcase(ArrayList<Map<String, Object>> list, String name, Object testData, Object expected) {
		testcase.clear();
		
		testcase.put("name", name);
		testcase.put("testData", testData);
		testcase.put("expected", expected);
		
		list.add(testcase);
		return list;
	}

}
