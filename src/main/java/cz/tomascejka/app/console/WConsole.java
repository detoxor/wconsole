package cz.tomascejka.app.console;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cz.tomascejka.app.console.command.Command;
import cz.tomascejka.app.console.command.CommandAdd;
import cz.tomascejka.app.console.command.CommandDelete;
import cz.tomascejka.app.console.command.CommandExit;
import cz.tomascejka.app.console.command.CommandHelp;
import cz.tomascejka.app.console.command.CommandList;
import cz.tomascejka.app.console.command.CommandLogin;
import cz.tomascejka.app.console.command.CommandSignUp;
import cz.tomascejka.app.console.command.DataException;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.ConsoleException;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.Repository;
/**
 * @author tomascejka
 */
public class WConsole {

	private final Console console;
	public static final String WCONSOLE_PREFIX = "wconsole>";
	public static final String STORAGE_CORRUPTED = "Data storage is in inconsistent state. Contact administrator administrator@wconsole.zzz";
	/**
	 * Init java console
	 */
	public WConsole() {
		console = System.console();
		if (console == null) {
			throw new ConsoleException("Console is not available");
		}
		console.printf("#######################################################\n");
		console.printf("#                                                     #\n");
		console.printf("#        Welcome to Wconsole, v0.0.1.SNAPSHOT         #\n");
		console.printf("#                                                     #\n");
		console.printf("#######################################################\n");
	}
	/**
	 * Start console login command and execute loop to wait for user inputs
	 */
	public void boostrap() {
		//login
		String username = (String) new CommandLogin(repository).execute(console);
		if(username == null) {
			//sign up
			try {
				username = (String) new CommandSignUp(repository).execute(console);
	  	    } catch (DataAccessFailException e) {
	  	    	console.printf(STORAGE_CORRUPTED+"\n "+e.getMessage());
	  	    }
		}
		//run console
		boolean isGranted = username != null;
		final String consolePrefix = isGranted ? "wconsole@username>".replace("username", username) : WCONSOLE_PREFIX;
		
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put(CommandHelp.KEY, new CommandHelp(isGranted));
		commands.put(CommandAdd.KEY, new CommandAdd(repository, consolePrefix));
		commands.put(CommandDelete.KEY, new CommandDelete(repository, consolePrefix, username));
		commands.put(CommandList.KEY, new CommandList(repository));
		commands.put(CommandExit.KEY, new CommandExit());
		
		while (true) {
			final String line = console.readLine(consolePrefix);
			final Scanner scanner = new Scanner(line);
			if (scanner.hasNext()) {
				final String commandName = scanner.next().toLowerCase();
				try {
					if (CommandHelp.KEY.equals(commandName)) {
						commands.get(CommandHelp.KEY).execute(console);
					} else if (isGranted && CommandAdd.KEY.equals(commandName)) {
						commands.get(CommandAdd.KEY).execute(console);
					} else if (isGranted && CommandDelete.KEY.equals(commandName)) {
						commands.get(CommandDelete.KEY).execute(console);
					} else if (isGranted && CommandList.KEY.equals(commandName)) {
						commands.get(CommandList.KEY).execute(console);
					} else if (CommandExit.KEY.equals(commandName)) {
						final Boolean close = (Boolean) commands.get(CommandExit.KEY).execute(console);
						if (close) {
							break;
						}
					} else {
						console.printf("Uknown command:"+commandName+"\n");
					}
				} catch (DataAccessFailException e) {
					console.printf(STORAGE_CORRUPTED);
				} catch (DataException e1) {
					throw new RuntimeException("Uknown state", e1);
				}
			}
			scanner.close();
		}
	}

	// -- IoC
	private Repository<User> repository;

	public void setRepository(final Repository<User> repository) {
		this.repository = repository;
	}
}
