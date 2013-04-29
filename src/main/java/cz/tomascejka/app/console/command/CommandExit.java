package cz.tomascejka.app.console.command;

import java.io.Console;

public class CommandExit implements Command {

	public static final String KEY="exit";

	public Object execute(final Console console) {
		final String answer = console.readLine("Are you sure? [y/n]: ");
		boolean retval = false;
		if("y".equals(answer)){
			retval = true;
		} else if("n".equals(answer)) {
			retval = false;
		} else {
			console.printf("Uknown answer: "+answer);
			retval = false;
		}
		return retval;
	}
}
