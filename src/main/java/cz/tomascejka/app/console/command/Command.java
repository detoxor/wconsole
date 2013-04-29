package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.storage.DataException;
/**
 * Basic interface console command
 * @author tomascejka
 */
public interface Command {
	/**
	 * Execute specific command
	 * @param console
	 * @return mixed object it depends on concrete implementation
	 * @throws DataException it allows declared problems in command
	 */
	Object execute(final Console console) throws DataException;
}
