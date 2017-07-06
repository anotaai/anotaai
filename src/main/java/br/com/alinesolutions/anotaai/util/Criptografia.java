package br.com.alinesolutions.anotaai.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;

public class Criptografia {

	private static MessageDigest md;
	private static String ALGORITHM = "SHA-256";
	private static String CHARSET_NAME = "UTF-8";

	static {
		try {
			md = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException ex) {
			ResponseEntity<?> responseEntity = new ResponseEntity<>();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(IMessage.ERRO_NAO_IDENTIFICADO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
			throw new AppException(responseEntity);
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
			ResponseEntity<?> responseEntity = new ResponseEntity<>();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(IMessage.ERRO_NAO_IDENTIFICADO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
			throw new AppException(responseEntity);
		}
	}
}