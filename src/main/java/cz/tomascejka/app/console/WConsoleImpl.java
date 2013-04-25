package cz.tomascejka.app.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.util.Date;

import cz.tomascejka.app.data.Repository;
import cz.tomascejka.app.data.User;
import cz.tomascejka.app.data.WConsoleDataException;

public class WConsoleImpl implements WConsole {

	private Console console;
	private Repository<User> repository;
	
    private static final String TIMESTAMP = "%1$tH:%1$tM:%1$tS";
    private static final String USER_PROMPT = TIMESTAMP + " User: ";
    private static final String PASS_PROMPT = TIMESTAMP + " Password [Count : %2$s]: ";
	private static final String ACCESS_DENIED = "Access denied";
	private static final String ACCESS_ALLOWED = null;	
	
	public WConsoleImpl() {
		console = System.console();
		if(console == null) throw new RuntimeException("Console is not available");
	}

	public boolean login() {
        console.printf("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        console.printf("X                  User login                         X");
        console.printf("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        boolean accessOk = false;
        int countAttemps = 0;
        while (!accessOk && countAttemps < 3)
        {
            String username = console.readLine(USER_PROMPT, new Date());
            char[] incominPassword = console.readPassword(PASS_PROMPT, new Date(), username);
            User user = repository.find(username);
            if(user == null) {//uknown user(not exist in storage)
            	boolean state = repository.add(new User(username, incominPassword));
            	if(state) console.printf("User has been successfully saved");
            	else console.printf("User has been successfully saved");
            } else if(user.getPassword().equals(new String(incominPassword))) {
            	countAttemps = 0;
            	accessOk = true;
            	break;
            }
            console.printf(ACCESS_DENIED, ++countAttemps);
        }

        if (!accessOk) {
            console.printf(ACCESS_DENIED);
            return false;
        }
        console.printf(ACCESS_ALLOWED);
        return true;
	}

	public void command() {
		// TODO Auto-generated method stub
		
	}
}
