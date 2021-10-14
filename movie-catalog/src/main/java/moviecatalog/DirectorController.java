package moviecatalog;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import moviecatalog.model.Director;
import moviecatalog.model.Movie;
import moviecatalog.repository.DirectorRepository;
import moviecatalog.repository.MovieRepository;

/**
 * Rest Controller for CRUD operations on Directors to the catalog.
 * 
 * @author johnathanleif
 * 
 * */	
@RestController
@RequestMapping("directors")
@Api(tags = {"Director Service"})
@Tag(name = "Director Service", description = "Director List API")
public class DirectorController {
	
	@Autowired
	private DirectorRepository repository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	/**
	 * GET the list of Directors using URI "/directors"
	 * */
	@GetMapping
	public Iterable<Director> findAllDirectors() {
		return repository.findAll();
	}
	
	/**
	 * GET the Director with ID using URI "/directors/{ID}"
	 * */
	@GetMapping("/{id}")
	public Optional<Director> findDirectorById(@PathVariable int id) {
		return repository.findById(id);
	}
	
	/**
	 * GET the list of Directors by name using URI "/directors/search-name-equals?query={name}"
	 * */
	@GetMapping(path = "/search-name-equals")
	public Iterable<Director> findAllDirectorsByName(@RequestParam String query) {
		return repository.findAllByName(query);
	}
	
	/**
	 * GET the list of Directors by partial name using URI "/directors/search-name-contains?query={partial name}"
	 * */
	@GetMapping(path = "/search-name-contains")
	public Iterable<Director> findAllDirectorsByNameContains(@RequestParam String query) {
		return repository.findAllByNameContains(query);
	}
	
	/**
	 * GET the list of Directors by start of name using URI "/directors/search-name-starts-with?query={start of name}"
	 * */
	@GetMapping(path = "/search-name-starts-with")
	public Iterable<Director> findAllDirectorsByNameStartsWith(@RequestParam String query) {
		return repository.findAllByNameStartsWith(query);
	}
	
	/**
	 * GET the list of Directors by end of name using URI "/directors/search-name-ends-with?query={end of name}"
	 * */
	@GetMapping(path = "/search-name-ends-with")
	public Iterable<Director> findAllDirectorsByNameEndsWith(@RequestParam String query) {
		return repository.findAllByNameEndsWith(query);
	}
	
	/**
	 * POST a new Director using URI "/directors" with a JSON body of form:
	 * 
	 * 		{"name": "Director Name"}.
	 * 
	 * Director ID is generated even if included as a property.
	 * 
	 * */
	@PostMapping
	public Director saveNewDirector(@Valid @RequestBody Director director) {
		return repository.save(director);
	}
	
	/**
	 * PUT a new/updated Director with ID using URI "/directors/{ID}" with a JSON body of form:
	 * 
	 * 		{"name": "Director Name"}.
	 * 
	 * */
	@PutMapping("/{id}")
	public Director saveDirector(@Valid @RequestBody Director director, @PathVariable int id) {
		return repository.findById(id).map(dir -> {
			dir.setId(id);
			dir.setName(director.getName());
			return repository.save(dir);
		}).orElseGet(() -> {
			director.setId(id);
			return repository.save(director);
		});
	}
	
	
	/**
	 * DELETE the Director with ID using URI "/directors/{ID}"
	 * */
	@DeleteMapping("/{id}")
	public void deleteDirector(@PathVariable int id) {
		Optional<Director> director = repository.findById(id);
		if(director.isPresent()) {
			Iterable<Movie> movies = movieRepository.findAllByDirectorsId(id);
			for(Movie movie: movies) {
				movie.getDirectors().remove(director.get());		//remove join
			}
			repository.deleteById(id);
		}
	}

}
