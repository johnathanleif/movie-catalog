package moviecatalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import moviecatalog.model.Movie;
import moviecatalog.model.Rating;
import moviecatalog.repository.MovieRepository;
import moviecatalog.repository.RatingRepository;

/**
 * Rest Controller for CRUD operations on Ratings in the catalog.
 * 
 * @author johnathanleif
 * 
 * */
@RestController
@RequestMapping("ratings")
@Api(tags = {"Rating Service"})
@Tag(name = "Rating Service", description = "Rating List API")
public class RatingController {
	
	@Autowired
	private RatingRepository repository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	/**
	 * GET the list of Ratings using URI "/ratings"
	 * */
	@GetMapping
	public Iterable<Rating> findAllRatings() {
		return repository.findAllByOrderByAgeLimit();
	}
	
	/**
	 * GET the Rating with ID using URI "/ratings/{ID}"
	 * */
	@GetMapping("/{id}")
	public Optional<Rating> findRatingById(@PathVariable int id) {
		return repository.findById(id);
	}
	
	/**
	 * GET the list of Ratings by symbol using URI "/ratings/search?symbol={symbol}"
	 * */
	@GetMapping(path = "/search-symbol")
	public Optional<Rating> findRatingBySymbol(@RequestParam String symbol) {
		return repository.findBySymbol(symbol);
	}
	
	/**
	 * POST a new Rating using URI "/ratings" with a JSON body of form:
	 * 
	 * 		{"symbol": "SYMBOL", "ageLimit": 18}.
	 * 
	 * Rating ID is generated even if included as a property.
	 * 
	 * */
	@PostMapping
	public Rating saveNewRating(@Valid @RequestBody Rating rating) {
		return repository.save(rating);
	}
	
	/**
	 * PUT a new/updated Rating with ID using URI "/ratings/{ID}" with a JSON body of form:
	 * 
	 * 		{"symbol": "SYMBOL", "ageLimit": 18}.
	 * 
	 * */
	@PutMapping("/{id}")
	public Rating saveRating(@Valid @RequestBody Rating rating, @PathVariable int id) {
		return repository.findById(id).map(rat -> {
			rat.setId(id);
			rat.setSymbol(rating.getSymbol());
			rat.setAgeLimit(rating.getAgeLimit());
			return repository.save(rat);
		}).orElseGet(() -> {
			rating.setId(id);
			return repository.save(rating);
		});
	}
	
	/**
	 * DELETE the Rating with ID using URI "/ratings/{ID}"
	 * */
	@DeleteMapping("/{id}")
	public void deleteRating(@PathVariable int id) {
		Optional<Rating> rating = repository.findById(id);
		if(rating.isPresent()) {
			Iterable<Movie> movies = movieRepository.findAllByRatingId(id);
			for(Movie movie: movies) {
				movie.setRating(null);		//remove join
			}
			repository.delete(rating.get());
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
