package api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import api.model.Offer;
import api.model.Product;
import api.util.Consts;

public class TestObject {
	/**
	 * Creates a mock Offer object initialised with the prefix passed as parameter
	 * <p>
	 * The function creates an Offer object and set its variables to test data
	 * In order to keep traceability, the prefix (string passed as parameter)
	 * should be used by calling functions to state the function name. Such variable will
	 * be saved in the description of the offer.
	 *
	 * @param	prefix	should state the calling function name for traceability
	 * @return	the test offer object to be used by test functions
	 */
    public static Offer mockOffer(String prefix) {
        Offer o = new Offer();
        o.setProductID(4621346);
        o.setDescription(prefix);
        o.setPrice(new BigDecimal("100"));
        o.setCurrencyCode("EUR");
        o.setCreatedOn(LocalDate.now().format(DateTimeFormatter.ofPattern(Consts.TIMEFORMAT)));
        o.setDaysValidFor(20);
        return o;
    }
    
	/**
	 * Creates a mock Product object initialised with the prefix passed as parameter
	 * <p>
	 * The function creates an Product object and set its variables to test data
	 * In order to keep traceability, the prefix (string passed as parameter)
	 * should be used by calling functions to state the test this object was made for
	 * Such variable will be saved in the description and name of the product.
	 *
	 * @param	prefix	should state the test this object was made for
	 * @return	the test product object to be used by test functions
	 */
    public static Product mockProduct(String prefix) {
    	Product p = new Product();
    	p.setId(4621346);
    	p.setName(prefix);
    	p.setDescription(prefix);
		return p;
    }
}
