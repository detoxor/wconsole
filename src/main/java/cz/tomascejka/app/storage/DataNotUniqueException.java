package cz.tomascejka.app.storage;

public class DataNotUniqueException extends RuntimeException {

	private static final long serialVersionUID = 3564836151848925743L;

	public DataNotUniqueException(final String message) {
		super(message);
	}
}
