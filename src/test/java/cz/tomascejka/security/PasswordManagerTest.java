package cz.tomascejka.security;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.app.security.PasswordManager;

public class PasswordManagerTest {

	private static final String FOR_ECRYPT = "tomascejka";
	
	@Before
	public void setUp() throws Exception {
//		do nothing
	}

	@Test
	public void testEncryptDecrypt() {
		final String encrypted = PasswordManager.encrypt(FOR_ECRYPT);
		final String decrypted = PasswordManager.decrypt(encrypted);
		assertEquals("Passwords do not match", FOR_ECRYPT,decrypted);
	}

}
