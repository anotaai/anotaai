package br.com.alinesolutions.anotaai.i18n;

public final class TranslateEN implements ITranslate {
	
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
	
	final String app = "Anota Ai";
	final Message message = new Message();

	final static class Message {
		final String defultError = "Erro inesperado...";
	}

	final static class Login {
		final String forbidden = "Sessão expirada, favor efetuar o login novamente.";
		final String confirmacaoSenha = "A senha não confere com a confirmação de senha. Informe novamente.";
	}
}
