package cz.tomascejka.app.console.command;

import java.io.Console;

public class CommandLogout implements Command {

	public static final String KEY="logout";

	public Object execute(final Console console) {
		console.printf("Bye.\n");
        return null;
	}
}
