package moviecatalog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Rating {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Exclude private int id;
	@NotBlank(message = "Symbol required.")
	private String symbol;
    @EqualsAndHashCode.Exclude int ageLimit;
	
}
