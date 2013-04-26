package cz.tomascejka.app.security;

import org.jasypt.util.text.BasicTextEncryptor;
/**
 * Interface works with jasypt implementation to encrypt/decrypt password
 * @author tomascejka
 */
public final class PasswordManager {

	private static final byte[] bytePhrase = { 0x53, 0x6f, 0x6d, 0x65, 0x74,
			0x68, 0x69, 0x6e, 0x67, 0x56, 0x65, 0x72, 0x79, 0x53, 0x65, 0x63,
			0x72, 0x65, 0x74 };//"SomethingVerySecret"
	private static final String PHRASE = new String(bytePhrase);

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

	// public static void main(String[] args) {
	// String encrypted = encrypt(args[0]);
	// System.out.println(encrypted);
	// System.out.println(decrypt(encrypted));
	// //System.out.println(printHex("SomethingVerySecret".getBytes()));
	// }
	//
	// private static String printHex(byte[] bytes) {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < bytes.length; i++) {
	// byte b = bytes[i];
	// byte b1 = (byte) (b & 0x0F);
	// byte b2 = (byte) (b >> 4);
	// sb.append("0x").append(byteToHex(b2)).append(byteToHex(b1)).append(", ");
	// }
	// return sb.toString();
	// }
	//
	// private static String byteToHex(byte b) {
	// if (b < 10)
	// return String.valueOf(b);
	// else {
	// char c = 'a';
	// c += (b - 10);
	// return String.valueOf(c);
	// }
	// }

}
