package cz.tomascejka.app.console.command;

import java.io.Console;

import cz.tomascejka.app.domain.User;
import cz.tomascejka.app.storage.DataAccessFailException;
import cz.tomascejka.app.storage.DataNotFoundException;
import cz.tomascejka.app.storage.Repository;

public class CommandLogin implements Command {

	private static final String WCONSOLE_PREFIX="wconsole>";
	private static final String ACCESS_DENIED = "Access denied\n";
    private static final String ATTEMPT_DENIED = "Wrong user name or password [%1$d]\n";
	public static final String STORAGE_CORRUPTED = "Data storage is in inconsistent state. Contact administrator administrator@wconsole.zzz";
    private final Repository<User> repository;
	public static final String KEY="login";
	
	public CommandLogin(final Repository<User> repository) {
		this.repository = repository;
	}

	public Object execute(final Console console) {
        String retval = null;
		boolean isGranted = false;
		int count = 0;
        while (count < 2) {
        	try {
        		final String username = console.readLine(WCONSOLE_PREFIX + "User: ");
        		final char[] incominPassword = console.readPassword(WCONSOLE_PREFIX+ "Password: ");
        		//validace vstupu
        		if(username == null || "".equals(username)) {
        			console.printf("Username cannot be null\n");
        			continue;
        		}
        		if(incominPassword == null || incominPassword.length == 0) {
        			console.printf("Username cannot be null\n");
        			continue;
        		}        		
        		//najdi usera
        		final User user = repository.find(username);
        		if(new String(incominPassword).equals(user.getPassword())) {
        			count = 0;
        			isGranted=true;
            		console.printf(WCONSOLE_PREFIX+"Access allowed. Welcome to WNconsole\n");
            		retval = username;
        			break;
            	} else {
            		console.printf(ACCESS_DENIED);
            	}
        	} catch (DataNotFoundException e) {
    			console.printf(ATTEMPT_DENIED, ++count);
    		} catch (DataAccessFailException e) {
    			console.printf(STORAGE_CORRUPTED+"\n"+e);
    			break;
    		}
        }
        if(!isGranted){
        	console.printf(ACCESS_DENIED);
        } 
        return retval;
	}
}
