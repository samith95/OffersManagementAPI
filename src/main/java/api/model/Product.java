package api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
public class Product {
	
    @Id
    @GeneratedValue()
    private long id;

	@Column(nullable = false)
	private String name;
    
    @Column(nullable = false)
    private String description;
    
    /**
	 * @param id	unique identifier
	 * @param name	name of the product
	 * @param description	brief description of the product
	 */
	public Product(long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
    /**
     * Default constructor
     * 
	 */
	public Product() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**FOR TEST PURPOSES ONLY
	 * @param the id value to be set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
