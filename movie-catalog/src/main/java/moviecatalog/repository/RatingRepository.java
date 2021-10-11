package moviecatalog.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
	
	public Iterable<Rating> findAllByOrderByAgeLimit();
	public Optional<Rating> findBySymbol(String symbol);
	
}
