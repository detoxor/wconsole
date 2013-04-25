package cz.tomascejka.security;

import org.jasypt.util.text.BasicTextEncryptor;

public class PasswordManager {

	private static final byte[] bytePhrase = { 0x53, 0x6f, 0x6d, 0x65, 0x74,
			0x68, 0x69, 0x6e, 0x67, 0x56, 0x65, 0x72, 0x79, 0x53, 0x65, 0x63,
			0x72, 0x65, 0x74 };//"SomethingVerySecret"
	private static final String phrase = new String(bytePhrase);

	public static String encrypt(String text) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(phrase);
		return encryptor.encrypt(text);
	}

	public static String decrypt(String text) {
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		encryptor.setPassword(phrase);
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
