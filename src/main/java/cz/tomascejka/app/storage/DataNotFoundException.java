package cz.tomascejka.app.storage;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5560613253415480901L;

	public DataNotFoundException(final String message) {
		super(message);
	}

	public DataNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
