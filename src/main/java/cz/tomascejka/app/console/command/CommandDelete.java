package cz.tomascejka.app.console.command;

import java.io.Console;

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
	private final String prefix;
	private final String loggedUsername;
	public static final String KEY = "delete";
	/**
	 * @param repository data storage implementation
	 * @param cmdLineUser prefix for console message
	 * @param loggedUsername actual logged user
	 */
	public CommandDelete(final Repository<User> repository, final String cmdLineUser, final String loggedUsername) {
		this.repository = repository;
		this.prefix = cmdLineUser;
		this.loggedUsername = loggedUsername;
	}
	/**
	 * It allows delete user from storage (except yourself)
	 * @throws DataNotFoundException if given user is not found
	 * @throws DataAccessFailException if there is a problem with data storage (unavailable, inconsistent)
	 * @param console
	 * @return always true
	 */
	public Object execute(final Console console) throws DataNotFoundException, DataAccessFailException {
		console.printf("Please fill username \n");
		final String username = console.readLine(prefix+"User: ");	
		if(username.equals(loggedUsername)) {
			console.printf("You cannot delete yourself!\n");
		} else {
			repository.delete(username);
		}
		return true;
	}
}
