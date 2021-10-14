package moviecatalog.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Movie entity.
 * 
 * @author johnathanleif
 * 
 * */
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Movie {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	
	@ManyToOne(cascade = CascadeType.MERGE)		//MERGE allows new Movies to join to entities by ID only
	@JoinColumn(name = "rating_id")
	private Rating rating;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
		name = "movie_director",
		joinColumns = {@JoinColumn(name = "movie_id")},
		inverseJoinColumns = {@JoinColumn(name = "director_id")}
	)
	@EqualsAndHashCode.Exclude Set<Director> directors;
	
}
