package br.com.alinesolutions.anotaai.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiViewException;
import br.com.alinesolutions.anotaai.metadata.model.AppException;

public class Criptografia {

	private static MessageDigest md;
	private static String ALGORITHM = "SHA-256";
	private static String CHARSET_NAME = "UTF-8";

	static {
		try {
			md = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException ex) {
			throw new AppException(new AnotaaiViewException());
		}
	}

	public static String criptografar(String pwd) throws AppException {
		try {
			byte messageDigest[] = md.digest(pwd.getBytes(CHARSET_NAME));
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new AppException(new AnotaaiViewException());
		}
	}
}