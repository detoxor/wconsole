package cz.tomascejka.app.storage;

public class DataPersistException extends RuntimeException {

	private static final long serialVersionUID = -8463407421262455482L;

	public DataPersistException(final String message) {
		super(message);
	}
}
