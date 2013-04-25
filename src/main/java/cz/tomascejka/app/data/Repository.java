package cz.tomascejka.app.data;

import java.util.List;

public interface Repository<E> {

	boolean add(E entity);
	
	boolean delete(Long idEntity);
	
	List<E> list();

	E find(String name);
	
}
