package api.repository;

import org.springframework.data.repository.CrudRepository;

import api.models.Product;

/**
 * Repository used to delegate custom updates and CRUD operations, 
 * will be utilised in future for custom queries on products
 */
public interface ProductRepository extends CrudRepository<Product, Long>{
	
}
