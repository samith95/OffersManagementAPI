package api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import api.models.Offer;

/**
 * Repository used to delegate custom updates and CRUD operations
 */
public interface OfferRepository extends CrudRepository<Offer, Long>{
	@Modifying
	@Query("update Offer o set o.status = ?1 where o.id = ?2")
	int updateOfferStatus(String status, String id);
}
