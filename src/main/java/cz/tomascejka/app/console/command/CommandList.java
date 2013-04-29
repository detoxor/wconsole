package cz.tomascejka.app.console.command;

import java.io.Console;
import java.util.List;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.Repository;

public class CommandList implements Command {

	private final Repository<User> repository;
	public static final String KEY="list";
	
	public CommandList(final Repository<User> repository) {
		this.repository = repository;
	}

	public Object execute(final Console console) throws DataAccessFailException {
		final List<User> users = repository.list();
		console.printf("All registered users ["+users.size()+"]:\n");
		final StringBuilder list = new StringBuilder();
		int count = 0;
		for (User user : users) {
			final String password = user.getPassword();
			list.append("[").append(++count).append("] ");
			list.append("ID=").append(user.getId()).append(" ");
			list.append("NAME=").append(user.getUsername()).append(" ");
			list.append("ECNRYPTED=").append(password);
			list.append("\n");
		}
		console.printf(list.toString());
		return true;
	}
}
