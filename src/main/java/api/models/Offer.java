package api.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Offer {
	//constants
	public static final String VALID__STATUS_OFFER_STRING = "valid";
	public static final String EXPIRED__STATUS_OFFER_STRING = "expired";
	public static final String CANCELLED__STATUS_OFFER_STRING = "cancelled";
	
	private String id;
	private String description;
	private BigDecimal price;
	private String currencyCode;
	private Date createdOn;
	private float daysValidFor;
	private String productID;
	private String status;

	
	/**
	 * Default constructor
	 * <p>
	 * The id is initialised with a random number
	 */
	public Offer() {
		this.id = UUID.randomUUID().toString();
	}
	
	/**
	 * Offer class' constructor
	 * <p>
	 * The id is initialised with a random number
	 *
	 * @param  description  offer's friendly description
	 * @param  price  the price of the offer
	 * @param  currencyCode  currency code of the price e.g. USD
	 * @param  createdOn  date on which the offer was created
	 * @param  daysValidFor days the offer is valid for
	 * @param  status  defines what the status of the offer is e.g. valid, expired or cancelled
	 * @param  productID  id of the product on which the offer is based
	 */
	public Offer(String description, BigDecimal price, String currencyCode, Date createdOn, float daysValidFor,
			String status, String productID) {
		this.id = UUID.randomUUID().toString();
		this.description = description;
		this.price = price;
		this.currencyCode = currencyCode;
		this.createdOn = createdOn;
		this.daysValidFor = daysValidFor;
		this.status = status;
		this.productID = productID;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * FOR TEST PURPOSE ONLY
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the daysValidFor
	 */
	public float getDaysValidFor() {
		return daysValidFor;
	}
	/**
	 * @param daysValidFor the daysValidFor to set
	 */
	public void setDaysValidFor(float daysValidFor) {
		this.daysValidFor = daysValidFor;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the productID
	 */
	public String getProductID() {
		return productID;
	}

	/**
	 * @param productID the product to set
	 */
	public void setProductID(String product) {
		this.productID = product;
	}
}
