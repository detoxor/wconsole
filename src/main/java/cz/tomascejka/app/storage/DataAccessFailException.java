package cz.tomascejka.app.storage;


public class DataAccessFailException extends DataException {

	private static final long serialVersionUID = -1540333333320507925L;

	public DataAccessFailException(final String message) {
		super(message);
	}
}
