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
	 * GET the list of Directors by name using URI "/directors/search?name={name}"
	 * */
	@RequestMapping(path = "/search", params = "name")
	public Iterable<Director> findAllDirectorsByName(@RequestParam String name) {
		return repository.findAllByName(name);
	}
	
	/**
	 * GET the list of Directors by partial name using URI "/directors/search?name-contains={partial name}"
	 * */
	@RequestMapping(path = "/search", params = "name-contains")
	public Iterable<Director> findAllDirectorsByNameContains(@RequestParam(name = "name-contains") String nameQuery) {
		return repository.findAllByNameContains(nameQuery);
	}
	
	/**
	 * GET the list of Directors by start of name using URI "/directors/search?name-starts-with={start of name}"
	 * */
	@RequestMapping(path = "/search", params = "name-starts-with")
	public Iterable<Director> findAllDirectorsByNameStartsWith(@RequestParam(name = "name-starts-with") String nameQuery) {
		return repository.findAllByNameStartsWith(nameQuery);
	}
	
	/**
	 * GET the list of Directors by end of name using URI "/directors/search?name-ends-with={end of name}"
	 * */
	@RequestMapping(path = "/search", params = "name-ends-with")
	public Iterable<Director> findAllDirectorsByNameEndsWith(@RequestParam(name = "name-ends-with") String nameQuery) {
		return repository.findAllByNameEndsWith(nameQuery);
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
