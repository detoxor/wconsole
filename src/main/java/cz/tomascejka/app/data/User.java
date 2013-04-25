package cz.tomascejka.app.data;

public class User {

	private String username;
	private char[] password;
	
	public User(String username, char[] password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return new String(password);
	}	

}
