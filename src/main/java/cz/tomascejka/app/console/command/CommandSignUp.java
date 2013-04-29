package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.console.WNConsole;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.DataPersistException;
import cz.tomascejka.app.storage.Repository;

public class CommandSignUp implements Command {
	
	private static final String REGISTER_USER = WNConsole.WNCONSOLE_PREFIX+"Do you want to register? [y/n]: ";
	private static final String SAVE_SUCCESS = WNConsole.WNCONSOLE_PREFIX+"User has been successfully saved. Welcome to WNconsole. \n";
	private static final String SINGUP_CANCELLED = WNConsole.WNCONSOLE_PREFIX+"Sing up to wconsole system has been cancelled. Your login data has not been saved to data storage. \n";
    
    private final Repository<User> repository;
	public static final String KEY="singup";
    
	public CommandSignUp(final Repository<User> repository) {
		this.repository = repository;
	}

	public Object execute(final Console console) throws DataPersistException, DataNotUniqueException, DataAccessFailException {
		final String answer  = console.readLine(REGISTER_USER);
		String retval = null;
		String username = null;
		try {
			if("y".equals(answer)) {
				username = console.readLine(WNConsole.WNCONSOLE_PREFIX + "User: ");
				final char[] incominPassword = console.readPassword(WNConsole.WNCONSOLE_PREFIX+ "Password: ");			
				repository.add(new User(username, incominPassword));
				console.printf(SAVE_SUCCESS);
				retval = username;
			} else if ("n".equals(answer)) {
				console.printf(SINGUP_CANCELLED);
			} else {
				console.printf("Uknown answer "+answer);
			}
		} catch (DataNotUniqueException e) {
			console.printf("User '"+username + "' already exists. Use different username.\n");
		}
		return retval;
	}
}
