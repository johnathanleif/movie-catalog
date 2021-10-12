package moviecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieCatalogApplication {

	public static void main(String args[]) {
			
		SpringApplication.run(MovieCatalogApplication.class, args);
	
	}
	
//	@Configuration
//	public class PersistenceConfig {
//
//		@Bean
//		public DataSource dataSource(){
//		    return
//		        (new EmbeddedDatabaseBuilder())
//		        .addScript("director.sql")
//		        .addScript("rating.sql")
//		        .addScript("movie.sql")
//		        .addScript("movie_director.sql")
//		        .addScript("test_insert.sql")
//		        .build();
//		}
//		
//	}
	
}
