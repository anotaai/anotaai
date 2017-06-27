package br.com.alinesolutions.anotaai.i18n.pt;

import java.util.AbstractMap.SimpleEntry;

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
			public SimpleEntry<String, String> getDefultError() {
				return new SimpleEntry<String, String>("chave.DefultError", "valor.DefultError");
			}
			
		};
	}

	@Override
	public ILogin getLogin() {
		return new ILogin() {

			@Override
			public SimpleEntry<String, String> getForbidden() {
				return new SimpleEntry<String, String>("chave.DefultError", "Sessão expirada, favor efetuar o login novamente.");
			}

			@Override
			public SimpleEntry<String, String> getConfirmacaoSenha() {
				return new SimpleEntry<String, String>("chave.ConfirmacaoSenha", "A senha não confere com a confirmação de senha. Informe novamente.");
			}
		};
	}
}
