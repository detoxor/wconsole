package cz.tomascejka.app.storage;

public class DataNotUniqueException extends RuntimeException {

	private static final long serialVersionUID = -8253319557543505313L;
	
	public DataNotUniqueException(String message) {
		super(message);
	}

	public DataNotUniqueException(String message, Throwable cause) {
		super(message, cause);
	}
}
