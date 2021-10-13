package moviecatalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import moviecatalog.model.Director;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Integer> {
	
	/**
	 * Retrieves all entities by name.
	 * @return the entities with the given name
	 * */
	public Iterable<Director> findAllByName(String name);
	
	/**
	 * Retrieves all entities by name containing the given query string.
	 * @return the entities with name containing the given query string
	 * */
	public Iterable<Director> findAllByNameContains(String nameQuery);
	
	/**
	 * Retrieves all entities by name starting with the given query string.
	 * @return the entities with name starting with the given query string
	 * */
	public Iterable<Director> findAllByNameStartsWith(String nameQuery);
	
	/**
	 * Retrieves all entities by name ending with the given query string.
	 * @return the entities with name ending with the given query string
	 * */
	public Iterable<Director> findAllByNameEndsWith(String nameQuery);
	
}