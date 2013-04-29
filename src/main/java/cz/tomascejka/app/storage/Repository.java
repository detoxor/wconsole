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
	 * @throws DataPersistException if entity cannot be persited to data storage
	 * @throws DataNotUniqueException if new entity duplicat already existing entity in data storage
	 * @throws DataAccessFailException if data storage is not available or is in inconsistent state 
	 * return true if success
	 */
	boolean add(E entity) throws DataPersistException, DataNotUniqueException, DataAccessFailException;
	/**
	 * @param unique key to find and delete entity
	 * @throws DataNotFoundException if entity is not found in data storage
	 * @throws DataAccessFailException if data storage is not available or is in inconsistent state
	 * @return true if success
	 */
	boolean delete(String key) throws DataNotFoundException, DataAccessFailException;
	/**
	 * @throws DataAccessFailException if data storage is not available or is in inconsistent state
	 * @return all founded entities in data storage
	 */
	List<E> list() throws DataAccessFailException;
	/**
	 * @throws DataNotFoundException if entity is not found in data storage
	 * @throws DataAccessFailException if data storage is not available or is in inconsistent state
	 * @param unique key to find entity
	 * return E given entity
	 */
	User find(String key) throws DataNotFoundException, DataAccessFailException;
	/**
	 * delete all entities in data storage
	 */
	void deleteAll();
}
