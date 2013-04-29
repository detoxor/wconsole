package cz.tomascejka.app.storage;

import cz.tomascejka.app.console.command.DataException;

public class DataAccessFailException extends DataException {

	private static final long serialVersionUID = -1540333333320507925L;

	public DataAccessFailException(final String message) {
		super(message);
	}
}
