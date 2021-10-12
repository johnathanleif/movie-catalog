package moviecatalog;

import java.util.Collections;
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

import moviecatalog.model.Director;
import moviecatalog.model.Movie;
import moviecatalog.model.Rating;
import moviecatalog.repository.DirectorRepository;
import moviecatalog.repository.MovieRepository;
import moviecatalog.repository.RatingRepository;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieRepository repository;	
	@Autowired
	private RatingRepository ratingRepository;
	@Autowired
	private DirectorRepository directorRepository;
	
	
	@GetMapping
	public Iterable<Movie> findAllMovies() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Movie> findMovieById(@PathVariable int id) {
		return repository.findById(id);
	}
	
	@RequestMapping(path = "/search", params = "director-name")
	public Iterable<Movie> findAllMoviesByDirectorName(@RequestParam("director-name") String name) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(name);
		return repository.findAllByDirectorsIn(directors);
	}
	
	@RequestMapping(path = "/search", params = "director-name-contains")
	public Iterable<Movie> findAllMoviesByDirectorNameContains(@RequestParam("director-name-contains") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return repository.findAllByDirectorsIn(directors);
	}
	
	@RequestMapping(path = "/search", params = "director-name-starts-with")
	public Iterable<Movie> findAllMoviesByDirectorNameStartsWith(@RequestParam("director-name-starts-with") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return repository.findAllByDirectorsIn(directors);
	}
	
	@RequestMapping(path = "/search", params = "director-name-ends-with")
	public Iterable<Movie> findAllMoviesByDirectorNameEndsWith(@RequestParam("director-name-ends-with") String nameQuery) {
		Iterable<Director> directors = directorRepository.findAllByNameContains(nameQuery);
		return repository.findAllByDirectorsIn(directors);
	}
	
	@RequestMapping(path = "/search", params = "rating")
	public Iterable<Movie> findAllMoviesByRating(@RequestParam("rating") String symbol) {
		return repository.findAllByRatingSymbol(symbol);
	}
	
	@RequestMapping(path = "/search", params = "rated-above")
	public Iterable<Movie> findAllMoviesByRatingGreaterThan(@RequestParam("rated-above") String symbol) {
		Optional<Rating> rating = ratingRepository.findBySymbol(symbol);
		if(rating.isPresent()) {
			return repository.findAllByRatingAgeLimitGreaterThan(rating.get().getAgeLimit());
		} else {
			return Collections.emptySet();
		}
	}
	
	@PostMapping
	public Movie saveNewMovie(@Valid @RequestBody Movie movie) {
		return repository.save(movie);
	}
	
	@PutMapping("/{id}")
	public Movie saveMovie(@RequestBody Movie movie, @PathVariable int id) {
		return repository.findById(id).map(mov -> {
			mov.setId(id);
			mov.setTitle(movie.getTitle());
			return repository.save(mov);
		}).orElseGet(() -> {
			movie.setId(id);
			return repository.save(movie);
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
