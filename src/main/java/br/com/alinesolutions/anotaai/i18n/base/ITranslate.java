package br.com.alinesolutions.anotaai.i18n.base;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public interface ITranslate extends Serializable {

	String getApp();

	IMessage getMessage();

	ILogin getLogin();

	public static interface IMessage {
		SimpleEntry<String, String> getDefultError();
	}

	public static interface ILogin {
		SimpleEntry<String, String> getForbidden();

		SimpleEntry<String, String> getConfirmacaoSenha();
	}

}
