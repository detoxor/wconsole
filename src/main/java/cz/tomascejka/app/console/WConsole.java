package cz.tomascejka.app.console;


public interface WConsole {
	/**
	 * @return true if login finishes succefully
	 */
	boolean login();
	/**
	 * It allows to save new user to data storage
	 * @param userName
	 * @param password
	 * @return true if persist finishes successfully
	 */
	boolean signUp(String userName, char[] password);
	/**
	 * Bootrap of console
	 */
	void run();
}
