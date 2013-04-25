package cz.tomascejka.app.console;


public interface WConsole {

	boolean login();
	
	boolean signUp(String userName, char[] password);
	
	void run();
	
}
