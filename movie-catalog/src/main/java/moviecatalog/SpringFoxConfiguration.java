package moviecatalog;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfiguration {

	@Bean
	public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2)  
				.select()                                  
				.apis(RequestHandlerSelectors.basePackage("moviecatalog"))
				.paths(PathSelectors.any())                    
				.build()
				.apiInfo(getApiInfo());                                       
    }
	
    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Movie Catalog",
                "Spring Boot RESTful service using SpringFox + Swagger UI",
                "V1.0",
                "",
                new Contact("Johnathan Smith", "https://github.com/johnathanleif/movie-catalog", ""),
                "Unlicensed",
                "",
                Collections.emptyList()
        );
    }
    
}
