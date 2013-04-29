package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.console.WNConsole;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.DataPersistException;
import cz.tomascejka.app.storage.Repository;

public class CommandAdd implements Command {

	private final Repository<User> repository;
	public static final String KEY="add";
	
	public CommandAdd(final Repository<User> repository) {
		this.repository = repository;
	} 

	public Object execute(Console console) throws DataPersistException, DataAccessFailException {
		console.printf("Please fill username and password for new user.\n");
		final String username = console.readLine(WNConsole.WNCONSOLE_PREFIX+"User:");
		final char[] incominPassword = console.readPassword(WNConsole.WNCONSOLE_PREFIX+"Password: ");	
		try {
			repository.add(new User(username, incominPassword));
			console.printf("User '"+username +"' has been successfully persisted.\n");
		} catch (DataNotUniqueException e) {
			console.printf("User '"+username +"' already exists. Use different username.\n");
		}
		return true;
	}
}
