package cz.tomascejka.app;

import cz.tomascejka.app.console.WNConsole;
import cz.tomascejka.app.storage.impl.UserFileRepository;

public class App { // NOPMD by tomascejka on 26.4.13 14:56
	public static void main(final String[] args) {
		final WNConsole console = new WNConsole();
		console.setRepository(new UserFileRepository("users.txt"));
		console.boostrap();
	}
}
