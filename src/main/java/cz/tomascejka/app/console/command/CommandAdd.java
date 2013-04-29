package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotUniqueException;
import cz.tomascejka.app.storage.DataPersistException;
import cz.tomascejka.app.storage.Repository;

public class CommandAdd implements Command {

	private final Repository<User> repository;
	private final String prefix;
	public static final String KEY="add";
	
	public CommandAdd(final Repository<User> repository, final String cmdLineUser) {
		this.repository = repository;
		this.prefix = cmdLineUser;
	}

	public Object execute(final Console console) throws DataPersistException, DataNotUniqueException, DataAccessFailException {
		console.printf("Please fill username and password for new user.\n");
		final String username = console.readLine(prefix+"User:");
		final char[] incominPassword = console.readPassword(prefix+"Password: ");		
		repository.add(new User(username, incominPassword));
		return true;
	}
}
