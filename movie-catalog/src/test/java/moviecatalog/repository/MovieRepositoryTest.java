package moviecatalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moviecatalog.model.Movie;
import moviecatalog.model.Rating;

@SpringBootTest
public class MovieRepositoryTest {
	
	private static final String NEW_TITLE = "new title";
	private static final String UPDATE_TITLE = "update title";
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	RatingRepository ratingRepository;
	
	@Test
	void testSaveNewMovie() {
	    Movie movie = movieRepository.save(new Movie(0, NEW_TITLE, null, null));
		assertEquals(movieRepository.findById(movie.getId()).get(), movie);
	}
	
	@Test
	void testUpdateMovie() {
	    Movie movie = movieRepository.save(new Movie(0, NEW_TITLE, null, null));
	    Movie savedMovie = movieRepository.findById(movie.getId()).get();
	    savedMovie.setTitle(UPDATE_TITLE);
	    movieRepository.save(savedMovie);
	    assertNotEquals(movieRepository.findById(savedMovie.getId()).get(), movie);
		assertEquals(movieRepository.findById(savedMovie.getId()).get(), savedMovie);
	}
	
	@Test
	void testSaveMovieWithNewRating() {
		Movie movie = movieRepository.save(new Movie(0, "", new Rating(0, "A", 0), null));

		assertEquals(ratingRepository.findById(movie.getRating().getId()).get(), movie.getRating());
	}
	
	@Test
	void testSaveMovieWithExistingRating() {
		Rating rating = ratingRepository.save(new Rating(0, "A", 0));
		Movie movie = movieRepository.save(new Movie(0, "", rating, null));

		assertEquals(ratingRepository.findById(rating.getId()).get(), movie.getRating());
		
	}	
	
}
