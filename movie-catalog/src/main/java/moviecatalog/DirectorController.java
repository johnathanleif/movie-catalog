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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
	 * GET the list of {@link Director}s using URI "/directors"
	 * */
	@GetMapping
    @ApiOperation(value = "Find All Movie Directors in the catalog", notes = "Get all Directors.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Iterable<Director> findAllDirectors() {
		return repository.findAll();
	}
	
	/**
	 * GET the {@link Director} with ID using URI "/directors/{ID}"
	 * */
	@GetMapping("/{id}")
    @ApiOperation(value = "Find Movie Director with ID", notes = "Get the Director with ID.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Optional<Director> findDirectorById(@PathVariable int id) {
		return repository.findById(id);
	}
	
	/**
	 * GET the list of {@link Director}s by name using URI "/directors/search-name-equals?query={name}"
	 * */
	@GetMapping(path = "/search-name-equals")
    @ApiOperation(value = "Find Movie Directors by Name", notes = "Director search by exact Name (name equals query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Iterable<Director> findAllDirectorsByName(@RequestParam String query) {
		return repository.findAllByName(query);
	}
	
	/**
	 * GET the list of {@link Director}s by partial name using URI "/directors/search-name-contains?query={partial name}"
	 * */
	@GetMapping(path = "/search-name-contains")
    @ApiOperation(value = "Find Movie Directors by partial Name", notes = "Director search by partial Name (name contains given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Iterable<Director> findAllDirectorsByNameContains(@RequestParam String query) {
		return repository.findAllByNameContains(query);
	}
	
	/**
	 * GET the list of {@link Director}s by start of name using URI "/directors/search-name-starts-with?query={start of name}"
	 * */
	@GetMapping(path = "/search-name-starts-with")
    @ApiOperation(value = "Find Movie Directors by start of Name", notes = "Director search by start of Name (name starts with given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Iterable<Director> findAllDirectorsByNameStartsWith(@RequestParam String query) {
		return repository.findAllByNameStartsWith(query);
	}
	
	/**
	 * GET the list of {@link Director}s by end of name using URI "/directors/search-name-ends-with?query={end of name}"
	 * */
	@GetMapping(path = "/search-name-ends-with")
    @ApiOperation(value = "Find Movie Directors by end of Name", notes = "Director search by end of Name (name ends with given query).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
	public Iterable<Director> findAllDirectorsByNameEndsWith(@RequestParam String query) {
		return repository.findAllByNameEndsWith(query);
	}
	
	/**
	 * POST a new {@link Director} using URI "/directors" with a JSON body of form:
	 * 
	 * 		{"name": "Director Name"}.
	 * 
	 * Director ID is generated even if included as a property.
	 * 
	 * */
	@PostMapping
    @ApiOperation(value = "Save new Movie Director", notes = "Save a new Director with Name (ID will be generated).")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Successful Operation.", response=Director.class)  
    	})
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
    @ApiOperation(value = "Update a Movie Director", notes = "Save existing Director by ID with Name (new Director created with generated ID if not found).")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.", response=Director.class)  
    	})
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
	 * DELETE the {@link Director} with ID using URI "/directors/{ID}"
	 * */
	@DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a Movie Director", notes = "Delete Director by ID.")
    @ApiResponses(value = {
    		@ApiResponse (code = 200, message = "Successful Operation.")  
    	})
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
