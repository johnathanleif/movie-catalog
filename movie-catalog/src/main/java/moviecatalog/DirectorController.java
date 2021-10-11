package moviecatalog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import moviecatalog.model.Director;
import moviecatalog.repository.DirectorRepository;

@RestController
@RequestMapping("directors")
public class DirectorController {
	
	@Autowired
	private DirectorRepository repository;
	
	
	@GetMapping
	public Iterable<Director> findAllDirectors() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Director> findDirectorById(@PathVariable int id) {
		return repository.findById(id);
	}
	
	@RequestMapping(path = "/search", params = "name")
	public Iterable<Director> findAllDirectorsByName(@RequestParam String name) {
		return repository.findAllByName(name);
	}
	
	@RequestMapping(path = "/search", params = "name-contains")
	public Iterable<Director> findAllDirectorsByNameContains(@RequestParam(name = "name-contains") String nameQuery) {
		return repository.findAllByNameContains(nameQuery);
	}
	
	@RequestMapping(path = "/search", params = "name-starts-with")
	public Iterable<Director> findAllDirectorsByNameStartsWith(@RequestParam(name = "name-starts-with") String nameQuery) {
		return repository.findAllByNameStartsWith(nameQuery);
	}
	
	@RequestMapping(path = "/search", params = "name-ends-with")
	public Iterable<Director> findAllDirectorsByNameEndsWith(@RequestParam(name = "name-ends-with") String nameQuery) {
		return repository.findAllByNameEndsWith(nameQuery);
	}
	
	@PostMapping
	public Director saveNewDirector(@RequestBody Director director) {
		return repository.save(director);
	}
	
	@PutMapping("/{id}")
	public Director saveDirector(@RequestBody Director director, @PathVariable int id) {
		return repository.findById(id).map(dir -> {
			dir.setId(id);
			dir.setName(director.getName());
			return repository.save(dir);
		}).orElseGet(() -> {
			director.setId(id);
			return repository.save(director);
		});
	}

}
