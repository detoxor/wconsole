package cz.tomascejka.app.console;

import java.io.Console;
import java.util.Date;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.Repository;

public class WConsoleImpl implements WConsole {

	private Console console;
	
	/* moznost prevest do configuraku, pro preklady */
    private static final String TIMESTAMP = "%1$tH:%1$tM:%1$tS";
    private static final String USER_PROMPT = TIMESTAMP + " User: ";
    private static final String PASS_PROMPT = TIMESTAMP + " Password [Attempt : %2$s]: ";
    private static final String PASS_SIGNUP = TIMESTAMP + " Password: ";
	private static final String DENIED = "Access denied";
	private static final String ALLOWED = "Access allowed";
	private static final String ADD_USER = "Do you want to register? Please fill username and password.";
	private static final int attemptLimits = 10;
	
	public WConsoleImpl() {
		console = System.console();
		if(console == null) throw new RuntimeException("Console is not available");
	}

	public boolean login() {
        console.printf("Welcome to wconsole. Fill username please.\n");
        int count = 0;
        while (count < attemptLimits) {
            String username = console.readLine(USER_PROMPT, new Date());
            User user = repository.find(username);
            if(user.getId() == null) {//uzivatel neexistuje
            	char[] incominPassword = console.readPassword(PASS_SIGNUP, new Date());
            	signUp(username, incominPassword);
            } else {
            	char[] incominPassword = console.readPassword(PASS_PROMPT, new Date());
            	if(user.getPassword().equals(new String(incominPassword))) {
            		count = 0;
            		console.printf(ALLOWED);
            		return true;
            	}
            }
            console.printf(DENIED, ++count);
        }
        console.printf(DENIED);
        return false;
	}

	public boolean signUp(String userName, char[] password) {
		console.printf(ADD_USER);
    	boolean state = repository.add(new User(userName, password));//is false chyba?? mozna exception
    	console.printf(state ? "User has been successfully saved" : "User has not been saved");
		return state;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	//-- IoC
	private Repository<User> repository;
	
	public void setRepository(Repository<User> repository) {
		this.repository = repository;
	}
}
