package moviecatalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Director;
import moviecatalog.model.Movie;
import moviecatalog.model.Rating;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

	public Iterable<Movie> findAllByDirectorsIn(Iterable<Director> director);
	
	public Iterable<Movie> findAllByRating(Rating rating);
	
	public Iterable<Movie> findAllByRatingSymbol(String ratingSymbol);
	
	public Iterable<Movie> findAllByRatingAgeLimitGreaterThan(int ageLimit);
	
}
