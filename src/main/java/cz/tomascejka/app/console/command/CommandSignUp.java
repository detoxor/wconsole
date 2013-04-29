package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.console.WConsole;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.DataPersistException;
import cz.tomascejka.app.storage.Repository;

public class CommandSignUp implements Command {
	
	private static final String REGISTER_USER = WConsole.WCONSOLE_PREFIX+"Do you want to register? [y/n]: ";
	private static final String SAVE_SUCCESS = WConsole.WCONSOLE_PREFIX+"User has been successfully saved. Welcome to Wconsole. \n";
	private static final String SINGUP_CANCELLED = WConsole.WCONSOLE_PREFIX+"Sing up to wconsole system has been cancelled. Your login data has not been saved to data storage. \n";
    
    private final Repository<User> repository;
	public static final String KEY="singup";
    
	public CommandSignUp(final Repository<User> repository) {
		this.repository = repository;
	}

	public Object execute(final Console console) throws DataPersistException, DataNotUniqueException, DataAccessFailException {
		final String answer  = console.readLine(REGISTER_USER);
		String retval = null;
		if("y".equals(answer)) {
			final String username = console.readLine(WConsole.WCONSOLE_PREFIX + "User: ");
			final char[] incominPassword = console.readPassword(WConsole.WCONSOLE_PREFIX+ "Password: ");			
			repository.add(new User(username, incominPassword));
			console.printf(SAVE_SUCCESS);
			retval = username;
		} else if ("n".equals(answer)) {
			console.printf(SINGUP_CANCELLED);
		} else {
			console.printf("Uknown answer "+answer);
		}
		return retval;
	}
}
