package moviecatalog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Movie Director entity.
 * 
 * @author johnathanleif
 * 
 * */
@Entity
@Table(name = "director")
@Data @NoArgsConstructor @AllArgsConstructor
public class Director {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	private String name = null;
	
}
