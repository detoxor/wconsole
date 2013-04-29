package cz.tomascejka.app.console.command;

import java.io.Console;

public class CommandHelp implements Command {

	public static final String KEY="help";

	public Object execute(final Console console) {
		console.printf("WNConsole commands:\n");
		final StringBuilder list = new StringBuilder();
		list.append("   help : ").append("Show this help").append("\n");
		list.append("  login : ").append("Allow to login in WNConsole").append("\n");
		list.append("   list : ").append("Show list all registered users from storage").append("\n");
		list.append("    add : ").append("Add user to storage").append("\n");
		list.append(" delete : ").append("Remove user from storage [only logged]").append("\n");
		list.append(" logout : ").append("Allow to logout").append("\n");
		list.append("   exit : ").append("Exit from console WNConsole").append("\n");
		console.printf(list.toString());
		return true;
	}
}
