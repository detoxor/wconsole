package cz.tomascejka.app.domain;

public class NullUser extends User {

	public static final Integer NULL_ID = -999999999;
	public static final String NULL_NAME = "NULL_USER";
	
	public NullUser() {
		super(NULL_ID, NULL_NAME, null);
	}
}
