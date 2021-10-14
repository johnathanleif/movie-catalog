package moviecatalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Director;
import moviecatalog.model.Movie;
import moviecatalog.model.Rating;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

	/**
	 * Retrieves all entities containing the given {@link Director}s.
	 * @return the entities with the given {@link Director}s
	 * */
	public Iterable<Movie> findAllByDirectorsIn(Iterable<Director> directors);
	
	/**
	 * Retrieves all entities containing the given {@link Director}s.
	 * @return the entities with the given {@link Director}s
	 * */
	public Iterable<Movie> findAllByDirectorsId(int director_id);
	
	/**
	 * Retrieves all entities with the given {@link Rating}.
	 * @return the entities with the given {@link Rating}
	 * */
	public Iterable<Movie> findAllByRating(Rating rating);
	
	/**
	 * Retrieves all entities containing the given {@link Director}s.
	 * @return the entities with the given {@link Director}s
	 * */
	public Iterable<Movie> findAllByRatingId(int rating_id);
	
	/**
	 * Retrieves all entities with the given {@link Rating} symbol.
	 * @return the entities with the given rating symbol
	 * */
	public Iterable<Movie> findAllByRatingSymbol(String ratingSymbol);
	
	/**
	 * Retrieves all entities with the given {@link Rating} age limit.
	 * @return the entities with the given age limit
	 * */
	public Iterable<Movie> findAllByRatingAgeLimitGreaterThan(int ageLimit);
	
}
