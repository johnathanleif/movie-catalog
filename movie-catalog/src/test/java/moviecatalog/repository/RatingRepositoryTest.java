package moviecatalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moviecatalog.model.Rating;

@SpringBootTest
public class RatingRepositoryTest {

	private static final String NEW_SYMBOL = "A";
	private static final String UPDATE_SYMBOL = "B";
	private static final int NEW_AGE_LIMIT = 0;
	private static final int UPDATE_AGE_LIMIT = 1;
	
	@Autowired
	RatingRepository repository;
	
	@Test
	void testSaveRating() {
	    Rating rating = repository.save(new Rating(0, NEW_SYMBOL, NEW_AGE_LIMIT));
	    
		assertEquals(repository.findById(rating.getId()).get(), rating);
	}
	
	@Test
	void testUpdateRating() {
	    Rating rating = repository.save(new Rating(0, NEW_SYMBOL, NEW_AGE_LIMIT));
	    Rating savedRating = repository.findById(rating.getId()).get();
	    
	    savedRating.setSymbol(UPDATE_SYMBOL);
	    savedRating.setAgeLimit(UPDATE_AGE_LIMIT);
	    repository.save(savedRating);
	    
	    assertNotEquals(repository.findById(savedRating.getId()).get(), rating);
		assertEquals(repository.findById(savedRating.getId()).get(), savedRating);
	}
	
}
