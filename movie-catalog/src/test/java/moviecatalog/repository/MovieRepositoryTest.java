package moviecatalog.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import moviecatalog.model.Director;
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
	
	@Autowired
	DirectorRepository directorRepository;
	
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
	    
	    assertNotEquals(movieRepository.findById(savedMovie.getId()).get().getTitle(), movie.getTitle());
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
	
	@SuppressWarnings("serial")
	@Test
	void testSaveMovieWithNewDirectors() {
		Movie movie = movieRepository.save(new Movie(0, "", null, new HashSet<Director>(){{new Director(); new Director();}}));
		
		for(Director director: movie.getDirectors()) {
			assertThat(movie.getDirectors(), hasItem(directorRepository.findById(director.getId()).get()));
		}
	}
	
	@SuppressWarnings("serial")
	@Test
	void testSaveMovieWithExistingDirectors() {
		Director director1 = directorRepository.save(new Director(0, "Name 1"));
		Director director2 = directorRepository.save(new Director(0, "Name 2"));
		Movie movie = movieRepository.save(new Movie(0, "", null, new HashSet<Director>(){{add(director1); add(director2);}}));
		
		assertThat(movie.getDirectors(), hasItem(directorRepository.findById(director1.getId()).get()));
		assertThat(movie.getDirectors(), hasItem(directorRepository.findById(director2.getId()).get()));
	}
	
}
