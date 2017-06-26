package br.com.alinesolutions.anotaai.i18n.en;

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

	@Override
	public String getApp() {
		return "Anota ai";
	}

	@Override
	public IMessage getMessage() {
		return new IMessage() {
			@Override
			public String getDefultError() {
				return "Erro inesperado";
			}
		};
	}

	@Override
	public ILogin getLogin() {
		return new ILogin() {
			
			@Override
			public String getForbidden() {
				return "Sessão expirada, favor efetuar o login novamente.";
			}
			
			@Override
			public String getConfirmacaoSenha() {
				return "A senha não confere com a confirmação de senha. Informe novamente.";
			}
		};
	}

}
