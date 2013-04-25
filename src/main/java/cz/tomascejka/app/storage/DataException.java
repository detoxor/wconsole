package cz.tomascejka.app.storage;

public class DataException extends RuntimeException {

	private static final long serialVersionUID = -8253319557543505313L;

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}
}
