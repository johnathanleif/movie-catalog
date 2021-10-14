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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Api(tags = {"Movie Service"})
@Tag(name = "Movie Service", description = "Movie List API")
public class MovieController {

	@Autowired
	private MovieRepository movieRepository;	
	@Autowired
	private RatingRepository ratingRepository;
	@Autowired
	private DirectorRepository directorRepository;
	
	/**
	 * GET the list of {@link Movie}s using URI "/movies"
	 * */
	@GetMapping
    @ApiOperation(value = "Find All Movies in the catalog", notes = "Get all Movies.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMovies() {
		return movieRepository.findAll();
	}
	
	/**
	 * GET the {@link Movie} with ID using URI "/movies/{ID}"
	 * */
	@GetMapping("/{id}")
    @ApiOperation(value = "Find Movie with ID", notes = "Get the Movie with ID.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Optional<Movie> findMovieById(@PathVariable int id) {
		return movieRepository.findById(id);
	}
	
	/**
	 * GET the list of {@link Movie}s by Directors by ID using URI "/movies/search-director-id?query={id}"
	 * */
	@GetMapping(path = "/search-director-id")
    @ApiOperation(value = "Find Movies by Director ID", notes = "Movie search by Director ID.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByDirectorName(@RequestParam int query) {
		return movieRepository.findAllByDirectorsId(query);
	}
	
	/**
	 * GET the list of {@link Movie}s by Directors by name using URI "/movies/search-director-name?query={name}"
	 * */
	@GetMapping(path = "/search-director-name")
    @ApiOperation(value = "Find Movies by Directors Name", notes = "Movie search by Director exact Name (name equals query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByDirectorName(@RequestParam("director-name") String query) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(query);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of {@link Movie}s by Directors by partial name using URI "/movies/search-director-name-contains?query={partial name}"
	 * */
	@GetMapping(path = "/search-director-name-contains")
    @ApiOperation(value = "Find Movies by Directors partial Name", notes = "Movie search by Director partial Name (name contains given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByDirectorNameContains(@RequestParam String query) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(query);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of {@link Movie}s by Directors by start of name using URI "/movies/search-director-name-starts-with?query={start of name}"
	 * */
	@GetMapping(path = "/search-director-name-starts-with")
    @ApiOperation(value = "Find Movies by Directors start of Name", notes = "Movie search by Director start of Name (name starts with given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByDirectorNameStartsWith(@RequestParam String query) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(query);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of {@link Movie}s by Directors by end of name using URI "/movies/search-director-name-ends-with?query={end of name}"
	 * */
	@GetMapping(path = "/search-director-name-ends-with")
    @ApiOperation(value = "Find Movies by Directors end of Name", notes = "Movie search by Director end of Name (name ends with given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByDirectorNameEndsWith(@RequestParam String query) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(query);
		return movieRepository.findAllByDirectorsIn(directors);
	}
	
	/**
	 * GET the list of {@link Movie}s by Ratings by ID using URI "/movies/search?rating-id={ID}"
	 * */
	@GetMapping(path = "/search-rating-id")
    @ApiOperation(value = "Find Movies by Rating ID", notes = "Movie search by Rating ID.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByRatingId(@RequestParam int query) {
		return movieRepository.findAllByRatingId(query);
	}
	
	/**
	 * GET the list of {@link Movie}s by Ratings by symbol using URI "/movies/search-rating?query={symbol}"
	 * */
	@GetMapping(path = "/search-rating")
    @ApiOperation(value = "Find Movies by Rating", notes = "Movie search by Rating Symbol (PG, 12A, 15, etc.).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByRatingSymbol(@RequestParam String query) {
		return movieRepository.findAllByRatingSymbol(query);
	}
	
	/**
	 * GET the list of {@link Movie}s above Rating by symbol using URI "/movies/search-rated-above?query={symbol}"
	 * */
	@GetMapping(path = "/search-rated-above")
    @ApiOperation(value = "Find Movies above Rating", notes = "Movie search above Rating with Symbol (PG, 12A, 15, etc.).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Movie.class )  
    	})
	public Iterable<Movie> findAllMoviesByRatingGreaterThan(@RequestParam("rated-above") String query) {
		Optional<Rating> rating = ratingRepository.findBySymbol(query);
		if(rating.isPresent()) {
			return movieRepository.findAllByRatingAgeLimitGreaterThan(rating.get().getAgeLimit());
		} else {
			return Collections.emptySet();
		}
	}
	
	/**
	 * POST a new {@link Movie} using URI "/movies" with a JSON body of form:
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
    @ApiOperation(value = "Save new Movie.", notes = "Save a new Movie with Title, Rating (strictly new (without ID) or existing), and/or Directors (strictly new (without ID) or existing) (ID will be generated).")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Successful Operation.", response=Movie.class)  
    	})
	public Movie saveNewMovie(@Valid @RequestBody Movie movie) {
		mergeJoinedEntities(movie);			//spring handling entity cascading inconsistently for PUT and POST so do manually
		persistNewJoinedEntities(movie);
		
		return movieRepository.save(movie);
	}
	
	/**
	 * PUT a new/updated {@link Movie} with ID using URI "/movies/{ID}" with a JSON body of form:
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
    @ApiOperation(value = "Update a Movie", notes = "Save existing Movie by ID with Title, Rating (strictly new (without ID) or existing), and/or Directors (strictly new (without ID) or existing) (new Movie created with generated ID if not found).")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Successful Operation.", response=Movie.class)  
    	})
	public Movie saveMovie(@Valid @RequestBody Movie movie, @PathVariable int id) {
		mergeJoinedEntities(movie);			//spring handling entity cascading inconsistently for PUT and POST so do manually
		persistNewJoinedEntities(movie);

		movie.setId(id);		
		return movieRepository.save(movie);
	}
	
	/**
	 * DELETE the {@link Movie} with ID using URI "/movies/{ID}"
	 * */
	@DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a Movie", notes = "Delete Movie by ID.")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Successful Operation.")  
    	})
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
