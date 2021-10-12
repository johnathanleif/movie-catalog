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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"symbol"}))
@Data @NoArgsConstructor @AllArgsConstructor
public class Rating {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Exclude private int id;
   	@NotBlank(message = "Symbol required.") 
	private String symbol;
   	@NotNull(message = "Age Limit required for Rating comparisons.")
    @EqualsAndHashCode.Exclude int ageLimit;
	
}
