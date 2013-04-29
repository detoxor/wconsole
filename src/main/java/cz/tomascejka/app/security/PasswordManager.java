package cz.tomascejka.app.security;

import org.jasypt.util.text.BasicTextEncryptor;
/**
 * Interface works with jasypt implementation to encrypt/decrypt password
 * @author tomascejka
 */
public final class PasswordManager {

	private static final byte[] BYTE_PHRASE = { 0x53, 0x6f, 0x6d, 0x65, 0x74,
			0x68, 0x69, 0x6e, 0x67, 0x56, 0x65, 0x72, 0x79, 0x53, 0x65, 0x63,
			0x72, 0x65, 0x74 };//"SomethingVerySecret"
	private static final String PHRASE = new String(BYTE_PHRASE);

	private PasswordManager() {
		super();
	}
	/**
	 * It is used PBE with MD5 asd DES algoritmus
	 * @param text to encrypt 
	 * @return decrypted given text
	 */
	public static String encrypt(final String text) {
		final BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(PHRASE);
		return encryptor.encrypt(text);
	}
	/**
	 * @param text to decrypt
	 * @return decrypted given text
	 */
	public static String decrypt(final String text) {
		final BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(PHRASE);
		return encryptor.decrypt(text);
	}
}
