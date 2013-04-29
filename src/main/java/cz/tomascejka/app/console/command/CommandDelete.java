package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.console.WNConsole;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotFoundException;
import cz.tomascejka.app.storage.Repository;
/**
 * Delete user from data storage
 * @author tomascejka
 */
public class CommandDelete implements Command {

	private final Repository<User> repository;
	private String loggedUsername;
	public static final String KEY = "delete";
	/**
	 * @param repository data storage implementation
	 * @param cmdLineUser prefix for console message
	 * @param loggedUsername actual logged user
	 */
	public CommandDelete(final Repository<User> repository) {
		this.repository = repository;
	}
	/**
	 * It allows delete user from storage (except yourself)
	 * @throws DataNotFoundException if given user is not found
	 * @throws DataAccessFailException if there is a problem with data storage (unavailable, inconsistent)
	 * @param console
	 * @return always true
	 */
	public Object execute(final Console console) throws DataAccessFailException {
		console.printf("Please fill username \n");
		final String username = console.readLine(WNConsole.WNCONSOLE_PREFIX+"User: ");
		try {
			if(username.equals(loggedUsername)) {
				console.printf("You cannot delete yourself!\n");
			} else {
				repository.delete(username);
				console.printf("User '"+username+"' has been successfully deleted.");
			}
		} catch (DataNotFoundException e) {
			console.printf("User '"+username+"' is not exist in data storage");
		}
		return true;
	}
	
	public void setLoggedUsername(String loggedUsername) {
		this.loggedUsername = loggedUsername;
	}
}
