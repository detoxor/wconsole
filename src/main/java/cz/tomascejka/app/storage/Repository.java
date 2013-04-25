package cz.tomascejka.app.storage;

import java.util.List;

import cz.tomascejka.app.domain.User;
/**
 * @author tomascejka
 * @param <E> defined specific type of persistable item  
 */
public interface Repository<E> {
	/**
	 * @param entity to persist to data storage
	 * return true if success
	 */
	boolean add(E entity);
	/**
	 * @param unique username to find and delete entity
	 * return true if success
	 */
	boolean delete(String username);
	/**
	 * @return all founded entities in data storage
	 */
	List<E> list();
	/**
	 * @param unique username to find entity
	 * return E given entity
	 */
	User find(String username);
	/**
	 * delete all entities in data storage
	 */
	void deleteAll();
}
