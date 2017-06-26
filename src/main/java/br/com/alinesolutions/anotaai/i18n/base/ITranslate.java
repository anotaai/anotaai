package br.com.alinesolutions.anotaai.i18n.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public interface ITranslate extends Serializable {

	String getApp();
	IMessage getMessage();
	ILogin getLogin();
	
	public static interface IMessage {
		String getDefultError();
	}
	
	public static interface ILogin {
		String getForbidden();
		String getConfirmacaoSenha();
	}
	
}
