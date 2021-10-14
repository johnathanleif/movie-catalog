package moviecatalog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Movie Rating entity.
 * 
 * @author johnathanleif
 * 
 * */
@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"symbol"}))
@Data @NoArgsConstructor @AllArgsConstructor
public class Rating {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	@NotBlank(message = "Symbol required.") 
	private String symbol = null;
    @NotNull(message = "Age Limit required for Rating comparisons.")
    private Integer ageLimit = null;
	
}
