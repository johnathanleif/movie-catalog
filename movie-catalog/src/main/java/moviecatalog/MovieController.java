package moviecatalog;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import moviecatalog.model.Director;
import moviecatalog.model.Movie;
import moviecatalog.model.Rating;
import moviecatalog.repository.DirectorRepository;
import moviecatalog.repository.MovieRepository;
import moviecatalog.repository.RatingRepository;

/**
 * Rest Controller for CRUD operations on Movies to the catalog.
 * 
 * @author johnathanleif
 * 
 * */
@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;	
	@Autowired
	private RatingRepository ratingRepository;
	@Autowired
	private DirectorRepository directorRepository;
	
	/**
	 * GET the list of Movies using URI "/movies"
	 * */
	@GetMapping
	public Iterable<Movie> findAllMovies() {
		return movieRepository.findAll();
	}
	
	/**
	 * GET the Movie with ID using URI "/movies/{ID}"
	 * */
	@GetMapping("/{id}")
	public Optional<Movie> findMovieById(@PathVariable int id) {
		return movieRepository.findById(id);
	}
	
	/**
	 * GET the list of Movies by Directors by ID using URI "/movies/search?director-id={id}"
	 * */
	@RequestMapping(path = "/search", params = "director-id")
	public Iterable<Movie> findAllMoviesByDirectorName(@RequestParam("director-id") int id) {
		return movieRepository.findAllByDirectorsId(id);
	}
	
	/**
	 * GET the list of Movies by Directors by name using URI "/movies/search?director-name={name}"
	 * */
	@RequestMapping(path = "/search", params = "director-name")
	public Iterable<Movie> findAllMoviesByDirectorName(@RequestParam("director-name") String name) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(name);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of Movies by Directors by partial name using URI "/movies/search?director-name-contains={partial name}"
	 * */
	@RequestMapping(path = "/search", params = "director-name-contains")
	public Iterable<Movie> findAllMoviesByDirectorNameContains(@RequestParam("director-name-contains") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of Movies by Directors by start of name using URI "/movies/search?director-name-starts-with={start of name}"
	 * */
	@RequestMapping(path = "/search", params = "director-name-starts-with")
	public Iterable<Movie> findAllMoviesByDirectorNameStartsWith(@RequestParam("director-name-starts-with") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of Movies by Directors by end of name using URI "/movies/search?director-name-ends-with={end of name}"
	 * */
	@RequestMapping(path = "/search", params = "director-name-ends-with")
	public Iterable<Movie> findAllMoviesByDirectorNameEndsWith(@RequestParam("director-name-ends-with") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of Movies by Ratings by ID using URI "/movies/search?rating-id={ID}"
	 * */
	@RequestMapping(path = "/search", params = "rating-id")
	public Iterable<Movie> findAllMoviesByRatingId(@RequestParam("rating-id") int id) {
		return movieRepository.findAllByRatingId(id);
	}
	
	/**
	 * GET the list of Movies by Ratings by symbol using URI "/movies/search?rating={symbol}"
	 * */
	@RequestMapping(path = "/search", params = "rating")
	public Iterable<Movie> findAllMoviesByRatingSymbol(@RequestParam("rating") String symbol) {
		return movieRepository.findAllByRatingSymbol(symbol);
	}
	
	/**
	 * GET the list of Movies above Rating by symbol using URI "/movies/search?rated-above={symbol}"
	 * */
	@RequestMapping(path = "/search", params = "rated-above")
	public Iterable<Movie> findAllMoviesByRatingGreaterThan(@RequestParam("rated-above") String symbol) {
		Optional<Rating> rating = ratingRepository.findBySymbol(symbol);
		if(rating.isPresent()) {
			return movieRepository.findAllByRatingAgeLimitGreaterThan(rating.get().getAgeLimit());
		} else {
			return Collections.emptySet();
		}
	}
	
	/**
	 * POST a new Movie using URI "/movies" with a JSON body of form:
	 * 
	 * 		{"title": "Title", "rating": {"id": ID}, "directors": [{"id": D1_ID}, {"id": D2_ID}]} 
	 * for movies with existing Ratings and Directors in the catalog;
	 * 		{"title": "Title", "rating": {"symbol": "SYMBOL", "ageLimit": 18}, "directors": [{"name": "Director Name"}, {"name": "Named Director"}]} 
	 * for movies with new Ratings and Directors (or a combination).
	 * 
	 * If given IDs for Ratings or Directors does not exist an they are ignored. Ratings or Directors with existing IDs will not be updated from this request.
	 * 
	 * Movie ID is generated even if ID included as a property.
	 * 
	 * */
	@PostMapping
	public Movie saveNewMovie(@Valid @RequestBody Movie movie) {
		mergeJoinedEntities(movie);			//spring handling entity cascading inconsistently for PUT and POST so do manually
		persistNewJoinedEntities(movie);
		
		return movieRepository.save(movie);
	}
	
	/**
	 * PUT a new/updated Movie with ID using URI "/movies/{ID}" with a JSON body of form:
	 * 
	 * 		{"title": "Title", "rating": {"id": ID}, "directors": [{"id": D1_ID}, {"id": D2_ID}]} 
	 * for movies with existing Ratings and Directors in the catalog;
	 * 		{"title": "Title", "rating": {"symbol": "SYMBOL", "ageLimit": 18}, "directors": [{"name": "Director Name"}, {"name": "Named Director"}]} 
	 * for movies with new Ratings and Directors (or a combination).
	 * 
	 * If given IDs for Ratings or Directors does not exist they are ignored. Ratings or Directors with existing IDs will not be updated from this request.
	 * 
	 * */
	@PutMapping("/{id}")
	public Movie saveMovie(@Valid @RequestBody Movie movie, @PathVariable int id) {
		mergeJoinedEntities(movie);			//spring handling entity cascading inconsistently for PUT and POST so do manually
		persistNewJoinedEntities(movie);

		movie.setId(id);		
		return movieRepository.save(movie);
	}
	
	/**
	 * DELETE the Movie with ID using URI "/movies/{ID}"
	 * */
	@DeleteMapping("/{id}")
	public void deleteMovie(@PathVariable int id) {
		movieRepository.deleteById(id);
	}
	
	private void persistNewJoinedEntities(final Movie movie) {
		if(movie.getRating() != null && movie.getRating().getId() == null) {
			ratingRepository.save(movie.getRating());
		}
		if(movie.getDirectors() != null) {
			for(Director dir: movie.getDirectors()) {
				if(dir.getId() == null) {
					directorRepository.save(dir);
				}
			} 
		}
	}
	
	private void mergeJoinedEntities(final Movie movie) {
		if(movie.getRating() != null && movie.getRating().getId() != null) {
			Optional<Rating> rating = ratingRepository.findById(movie.getRating().getId());
			if(rating.isPresent()) {
				movie.setRating(rating.get());
			}
		}
		if(movie.getDirectors() != null) {
			Set<Director> directors = new HashSet<>();
			for(Director dir: movie.getDirectors()) {
				if(dir.getId() != null) {
					Optional<Director> director = directorRepository.findById(dir.getId());
					if(director.isPresent()) {
						directors.add(director.get());
					}
				} else {
					directors.add(dir);		//keep new directors without IDs for persistance
				}
			}
			movie.setDirectors(directors);		//alternative to using a proper iterator to remove during loop
		}
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
	
}
