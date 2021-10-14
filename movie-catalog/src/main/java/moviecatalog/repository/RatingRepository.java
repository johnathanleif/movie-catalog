package moviecatalog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
	
	/**
	 * Returns all instances of the type ordered by age limit.
	 * @return all entities ordered by age limit
	 * */
	public Iterable<Rating> findAllByOrderByAgeLimit();
	
	/**
	 * Retrieves an entity by its symbol.
	 * @return the entity with the given symbol
	 * */
	public Optional<Rating> findBySymbol(String symbol);	//TODO: either this is wrong or the db model is wrong
	
}
