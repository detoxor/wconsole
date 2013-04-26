package cz.tomascejka.app.storage;

import java.util.List;

import cz.tomascejka.app.domain.User;
/**
 * Basic interface how to work with low-level data storage.
 * @author tomascejka
 * @param <E> define specific type of persistable item  
 */
public interface Repository<E> {
	/**
	 * @param entity to persist to data storage
	 * return true if success
	 */
	boolean add(E entity);
	/**
	 * @param unique key to find and delete entity
	 * return true if success
	 */
	boolean delete(String key);
	/**
	 * @return all founded entities in data storage
	 */
	List<E> list();
	/**
	 * @param unique key to find entity
	 * return E given entity
	 */
	User find(String key);
	/**
	 * delete all entities in data storage
	 */
	void deleteAll();
}
