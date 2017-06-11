package br.com.alinesolutions.anotaai.i18n.pt;

import br.com.alinesolutions.anotaai.i18n.ITranslate;

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
	public final Message message = new Message();
	public final Entidade entidade = new Entidade();

	final static class Message {
		public final String defultError = "Erro inesperado";
	}

	final static class Login {
		public final String forbidden = "Sessão expirada, favor efetuar o login novamente.";
		public final String confirmacaoSenha = "A senha não confere com a confirmação de senha. Informe novamente.";
	}
	
	final static class Entidade {
		public final EntidadeDeletada editada = new EntidadeDeletada();
	}

	final static class EntidadeDeletada {
		public final String sucesso = "{0} editada com sucesso.";
	}
	
}
