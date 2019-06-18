package api.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Offer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Offer {
	//constants
	//when an offer has just been created, it will have an undefined state
	public static final String UNDEFINED__STATUS_OFFER_STRING = "undefined";
	public static final String VALID__STATUS_OFFER_STRING = "valid";
	public static final String EXPIRED__STATUS_OFFER_STRING = "expired";
	public static final String CANCELLED__STATUS_OFFER_STRING = "cancelled";
	
    @Id
    @GeneratedValue()
	private long id;
    
    @Column(nullable = false)
	private String description;
    
    @Column(nullable = false)
	private BigDecimal price;
    
    @Column(nullable = false)
	private String currencyCode;
    
    @Column(nullable = false)
	private String createdOn;

    //in case the validity of the offer is undefined, this value might be empty
    @Column()
	private int daysValidFor;
    
    @Column(nullable = false)
	private long productID;
    
    @Column(nullable = false)
	private String status;

	
	/**
	 * Default constructor
	 * <p>
	 * The id is initialised with a random number
	 */
	public Offer() {
	}
	
	/**
	 * Offer class' constructor
	 * <p>
	 * The id is initialised with a random number
	 *
	 * @param  description  offer's friendly description
	 * @param  price  the price of the offer
	 * @param  currencyCode  currency code of the price e.g. USD
	 * @param  createdOn  date on which the offer was created (passed as string for ease of json creation)
	 * @param  daysValidFor days the offer is valid for
	 * @param  status  defines what the status of the offer is e.g. valid, expired or cancelled
	 * @param  productID  id of the product on which the offer is based
	 */
	public Offer(String description, BigDecimal price, String currencyCode, String createdOn, int daysValidFor,
			String status, long productID) {
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
	public long getId() {
		return id;
	}
	/**
	 * FOR TEST PURPOSE ONLY
	 * 
	 * @param id the id to set
	 */
	public void setId(long id) {
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
	public String getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the daysValidFor
	 */
	public int getDaysValidFor() {
		return daysValidFor;
	}
	/**
	 * @param daysValidFor the daysValidFor to set
	 */
	public void setDaysValidFor(int daysValidFor) {
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
	public long getProductID() {
		return productID;
	}

	/**
	 * @param productID the product to set
	 */
	public void setProductID(long productID) {
		this.productID = productID;
	}
}
