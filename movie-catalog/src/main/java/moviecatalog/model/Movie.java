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
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Movie {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Exclude private int id;
	private String title;
	
	@Valid
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rating_id")
	private Rating rating;
	
	@Valid
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "movie_director",
		joinColumns = {@JoinColumn(name = "movie_id")},
		inverseJoinColumns = {@JoinColumn(name = "director_id")}
	)
	@EqualsAndHashCode.Exclude Set<Director> directors;
	
}
