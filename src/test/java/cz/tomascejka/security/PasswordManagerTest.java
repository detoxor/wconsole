package cz.tomascejka.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PasswordManagerTest {

	@Before
	public void setUp() throws Exception {
//		do nothing
	}

	@Test
	public void testEncryptDecrypt() {
		String forEncrypt = "tomascejka";
		String encrypted = PasswordManager.encrypt(forEncrypt);
		String decrypted = PasswordManager.decrypt(encrypted);
		assertTrue(forEncrypt.equals(decrypted));
	}

}
