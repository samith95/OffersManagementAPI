/**
 * 
 */
package api;

import static org.junit.Assert.*;

import org.junit.Test;

import api.service.OfferService;

/**
 * Unit tests for the OfferService class
 * 
 * @author Samith Silva
 *
 */
public class OfferServiceTest {

	/**
	 * Test method for {@link api.service.OfferService#createOffer(api.models.Offer)}.
	 */
	@Test
	public void testCreateOffer() {
		//scenarios:
		//successful
		//with wrong productID
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link api.service.OfferService#getOffer(long)}.
	 */
	@Test
	public void testGetOffer() {
		//scenarios:
		//found:
			//valid
			//valid in db but expired
		//not found
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link api.service.OfferService#cancelOffer(long)}.
	 */
	@Test
	public void testCancelOffer() {
		//scenario:
		//found:
			//valid
			//cancelled
			//expired
		//not found
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link api.service.OfferService#findOfferByID(long)}.
	 */
	@Test
	public void testFindOfferByID() {
		//scenarios:
		//found
		//not found
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link api.service.OfferService#updateOfferStatus(long, java.lang.String)}.
	 */
	@Test
	public void testUpdateOfferStatus() {
		//scenarios:
		//pass valid status and id
		//pass invalid status and id
		//pass valid status and wrong id
		//pass wrong status and valid id
		fail("Not yet implemented");
	}

}
