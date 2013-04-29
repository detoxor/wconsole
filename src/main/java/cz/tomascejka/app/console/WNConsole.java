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
import cz.tomascejka.app.console.command.CommandLogout;
import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.ConsoleException;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataException;
import cz.tomascejka.app.storage.Repository;
/**
 * @author tomascejka
 */
public class WNConsole {

	private final Console console;
	private String username;
	public static final String WNCONSOLE_PREFIX = "wnconsole>";
	public static final String STORAGE_CORRUPTED = "Data storage is in inconsistent state. Contact administrator administrator@wconsole.zzz";
	/**
	 * Init java console
	 */
	public WNConsole() {
		console = System.console();
		if (console == null) {
			throw new ConsoleException("Console is not available");
		}
	}
	/**
	 * Start with login/signup command. Init commands for console, start loop
	 * and wait for user input.
	 */
	public void boostrap() {
		console.printf("#######################################################\n");
		console.printf("#                                                     #\n");
		console.printf("#        Welcome to WNconsole, v0.0.1.SNAPSHOT        #\n");
		console.printf("#                                                     #\n");
		console.printf("#######################################################\n");
		
		//available commands for console
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put(CommandLogin.KEY, new CommandLogin(repository));
		commands.put(CommandHelp.KEY, new CommandHelp());
		commands.put(CommandAdd.KEY, new CommandAdd(repository));
		commands.put(CommandDelete.KEY, new CommandDelete(repository));
		commands.put(CommandList.KEY, new CommandList(repository));
		commands.put(CommandLogout.KEY, new CommandLogout());
		commands.put(CommandExit.KEY, new CommandExit());
		
		//wait for user input
		console.printf("Type 'help' to show all available commands. \n");
		console.printf("Please type 'login' and fill username and password \n");
		boolean isGranted = username != null;
		while (true) {
			String consolePrefix = isGranted ? "wnconsole@username>".replace("username", username) : WNCONSOLE_PREFIX;
			final String line = console.readLine(consolePrefix);
			final Scanner scanner = new Scanner(line);
			if (scanner.hasNext()) {
				final String commandName = scanner.next().toLowerCase();
				try {
					if(!isGranted && CommandLogin.KEY.equals(commandName)) {
						username = (String) commands.get(CommandLogin.KEY).execute(console);
						isGranted = username != null;
						consolePrefix = isGranted ? "wnconsole@username>".replace("username", username) : WNCONSOLE_PREFIX;
				    } else if (CommandHelp.KEY.equals(commandName)) {
						commands.get(CommandHelp.KEY).execute(console);
					} else if (CommandAdd.KEY.equals(commandName)) {
						commands.get(CommandAdd.KEY).execute(console);
					} else if (isGranted && CommandDelete.KEY.equals(commandName)) {
						((CommandDelete) commands.get(CommandDelete.KEY)).setLoggedUsername(username);
						commands.get(CommandDelete.KEY).execute(console);
					} else if (CommandList.KEY.equals(commandName)) {
						commands.get(CommandList.KEY).execute(console);
					} else if (isGranted && CommandLogout.KEY.equals(commandName)) {
						commands.get(CommandLogout.KEY).execute(console);
						username = null;
						isGranted = false;
					} else if (CommandExit.KEY.equals(commandName)) {
						final Boolean close = (Boolean) commands.get(CommandExit.KEY).execute(console);
						if (close) {
							break;
						}
					}else {
						console.printf("Uknown command:"+commandName+" or you are not logged in system!\n");
					}
				} catch (DataAccessFailException e) {
					console.printf(STORAGE_CORRUPTED);
				} catch (DataException e1) {
					throw new RuntimeException("Uknown state\n", e1);
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
