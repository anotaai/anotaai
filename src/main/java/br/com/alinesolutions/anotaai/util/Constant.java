package br.com.alinesolutions.anotaai.util;

public interface Constant {

	public interface App {
		String UNIT_NAME = "anota-ai-persistence-unit";
		Long SESSION_TIME = 30L;
		String COOKIE_SESSION_NAME = "globals";
		String NAME_FOTO_PERFIL = "fotoPerfil";
		Integer DEFAULT_ITENS_PER_PAGE = 5;
	}
		
	public interface Persistence {
		String START = "start";
		String MAX = "max";
		String QUERY = "query";
		String LIST_PARAM = "list";
	}
	
	public interface Entity {
		String CLIENTE = "cliente";
		String PRODUTO = "produto";
		String GRUPO_PRODUTO = "grupoProduto";
		String SETOR = "setor";
	}
	
	public interface FileNane {
		String I18N_PATH = "i18n/";
		String SHORTENER = "config/google/url-shortener/shortener.json";
		String CONFIRMACAO_CADASTRO_CONSUMIDOR_EMAIL = "template/email/usuario/confirmacao-cadastro-consumidor.html";
		String CONFIRMACAO_CADASTRO_CONSUMIDOR_SMS = "template/sms/usuario/confirmacao-cadastro-consumidor.txt";
		String CONFIRMACAO_CADASTRO_USUARIO_EMAIL = "template/email/usuario/confirmacao-cadastro-usuario.html";
		String CONFIRMACAO_CADASTRO_USUARIO_SMS = "template/email/usuario/confirmacao-cadastro-usuario.txt";
		String SOLICITACAO_EDICAO_CADASTRO_EMAIL = "template/email/usuario/solicitacao-edicao-cadastro.html";
		String SOLICITACAO_EDICAO_CADASTRO_SMS = "template/email/usuario/solicitacao-edicao-cadastro.txt";
		String CONFIRMACAO_ASSOCIACAO_CONSUMIDOR_EMAIL = "template/email/usuario/confirmacao-associacao-cliente.html";
		String CONFIRMACAO_ASSOCIACAO_CONSUMIDOR_SMS = "template/sms/usuario/confirmacao-associacao-cliente.txt";
		String NOTIFICACAO_RENEW_PASSWORD_EMAIL = "template/email/usuario/renew-password.html";
		String NOTIFICACAO_RENEW_PASSWORD_SMS = "template/sms/usuario/renew-password.txt";
	}

}
