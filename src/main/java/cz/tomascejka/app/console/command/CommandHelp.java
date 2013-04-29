package cz.tomascejka.app.console.command;

import java.io.Console;

public class CommandHelp implements Command {

	public static final String KEY="help";
	private boolean isGranted;
	
	public CommandHelp(boolean isGranted) {
		this.isGranted = isGranted;
	}

	public Object execute(final Console console) {
		console.printf("WConsole commands:\n");
		final StringBuilder list = new StringBuilder();
		list.append("   help : ").append("Show this help").append("\n");
		if(isGranted) {
			list.append("   list : ").append("Show list all registered users from storage").append("\n");
			list.append("    add : ").append("Add user to storage").append("\n");
			list.append(" delete : ").append("Remove user from storage").append("\n");
		}
		list.append("   exit : ").append("Exit from console WConsole").append("\n");
		console.printf(list.toString());
		return true;
	}
}
