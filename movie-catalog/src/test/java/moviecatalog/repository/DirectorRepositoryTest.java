package moviecatalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moviecatalog.model.Director;

@SpringBootTest
public class DirectorRepositoryTest {

	private static final String NEW_NAME = "name";
	private static final String UPDATE_NAME = "new_name";
	
	@Autowired
	DirectorRepository repository;
	
	@Test
	void testSaveNewDirector() {
	    Director director = repository.save(new Director(0, NEW_NAME));
		assertEquals(repository.findById(director.getId()).get(), director);
	}
	
	@Test
	void testUpdateMovie() {
		Director director = repository.save(new Director(0, NEW_NAME));
		Director savedDirector = repository.findById(director.getId()).get();
		
	    savedDirector.setName(UPDATE_NAME);
	    repository.save(savedDirector);
	    
	    assertNotEquals(repository.findById(savedDirector.getId()).get().getName(), director.getName());
		assertEquals(repository.findById(savedDirector.getId()).get(), savedDirector);
	}
	
}
