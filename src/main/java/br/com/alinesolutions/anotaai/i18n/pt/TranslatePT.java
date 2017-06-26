package br.com.alinesolutions.anotaai.i18n.pt;

import br.com.alinesolutions.anotaai.i18n.base.ITranslate;

public final class TranslatePT implements ITranslate {

	private static final long serialVersionUID = 1L;

	private static TranslatePT instance;

	private TranslatePT() {
		super();
	}

	static {
		instance = new TranslatePT();
	}

	public static TranslatePT getInstance() {
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
