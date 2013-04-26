package cz.tomascejka.app.domain;

import java.util.Arrays;

public class User {

	private Integer idUser;
	private String username;
	private char[] password;
	
	public User(final String username, final char[] password) {
		this(null, username, password);
	}
	
	public User(final Integer idUser, final String username, final char[] password) {
		this.idUser = idUser;
		this.username = username;
		this.password = (password == null) ? null : Arrays.copyOf(password, password.length);
	}	

	public final String getUsername() {
		return username;
	}

	public final String getPassword() {
		return password == null ? null : new String(password);
	}

	public final Integer getId() {
		return idUser;
	}	

}
