package cz.tomascejka.app.storage;

public class ConsoleException extends RuntimeException {

	private static final long serialVersionUID = -8253319557543505313L;

	public ConsoleException(final String message) {
		super(message);
	}

	public ConsoleException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
