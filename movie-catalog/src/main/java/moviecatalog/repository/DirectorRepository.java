package moviecatalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Director;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Integer> {
	
	public Iterable<Director> findAllByName(String name);
	
	public Iterable<Director> findAllByNameContains(String nameQuery);
	
	public Iterable<Director> findAllByNameStartsWith(String nameQuery);
	
	public Iterable<Director> findAllByNameEndsWith(String nameQuery);
	
}