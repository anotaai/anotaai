package br.com.alinesolutions.anotaai.i18n.en;

import br.com.alinesolutions.anotaai.i18n.ITranslate;

public class TranslateEN implements ITranslate {

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

	public final String app = "Anota Ai";
	public final Message message = new Message();

	final static class Message {
		public final String defultError = "Erro inesperado...";
	}

	final static class Login {
		public final String forbidden = "Sessão expirada, favor efetuar o login novamente.";
		public final String confirmacaoSenha = "A senha não confere com a confirmação de senha. Informe novamente.";
	}
}
