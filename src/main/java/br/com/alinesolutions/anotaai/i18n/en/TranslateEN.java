package br.com.alinesolutions.anotaai.i18n.en;

import java.util.AbstractMap.SimpleEntry;

import br.com.alinesolutions.anotaai.i18n.base.ITranslate;

public final class TranslateEN implements ITranslate {

	private static final long serialVersionUID = 1L;

	private static TranslateEN instance;

	private TranslateEN() {
		super();
	}

	static {
		instance = new TranslateEN();
	}

	public static TranslateEN getInstance() {
		return instance;
	}

	public final String app = "Anota ai";

	@Override
	public String getApp() {
		return "Anota ai";
	}

	@Override
	public IMessage getMessage() {
		return new IMessage() {
			@Override
			public SimpleEntry<String, String> getDefultError() {
				return new SimpleEntry<String, String>("message.defulterror.value", "Erro não identificado");
			}
		};
	}

	@Override
	public ILogin getLogin() {
		return new ILogin() {
			@Override
			public SimpleEntry<String, String> getForbidden() {
				return new SimpleEntry<String, String>("login.forbidden.value", "Sessão expirada, favor efetuar o login novamente.");
			}
			@Override
			public SimpleEntry<String, String> getConfirmacaoSenha() {
				return new SimpleEntry<String, String>("login.confirmacaoSenha.value", "A senha não confere com a confirmação de senha. Informe novamente.");
			}
		};
	}
}
