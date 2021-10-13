package moviecatalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

import moviecatalog.model.Rating;
import moviecatalog.repository.RatingRepository;

/**
 * Rest Controller for CRUD operations on Ratings in the catalog.
 * 
 * @author johnathanleif
 * 
 * */
@RestController
@RequestMapping("ratings")
public class RatingController {
	
	@Autowired
	private RatingRepository repository;
	
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
	@RequestMapping(path = "/search", params = "symbol")
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
