package br.com.alinesolutions.anotaai.util;

public interface Constant {

	public interface App {
		String UNIT_NAME = "anota-ai-persistence-unit";
		Long SESSION_TIME = 30L;
		String COOKIE_SESSION_NAME = "globals";
		String NAME_FOTO_PERFIL = "fotoPerfil";
	}
	
	public interface Message {
		Long DEFAULT_TIME_VIEW = 5000L;
		Long LONG_TIME_VIEW = 10000L;
		Long KEEP_ALIVE_TIME_VIEW = -1L;
		String EMAIL_JA_CADASTRADO = "email.ja.cadastrado";
		String ERRO_NAO_IDENTIFICADO = "msg.erro.nao.identificado";
		String CAMPO_OBRIGATORIO_NAO_INFORMADO = "campo.obrigatorio.nao.informado";
		String TELEFONE_JA_CADASTRADO = "telefone.ja.cadastrado";
		String CONSUMIDOR_JA_CADASTRADO = "consumidor.ja.cadastrado";
		String CONSUMIDOR_JA_REGISTRADO = "consumidor.ja.registrado";
		String USUARIO_SENHA_INVALIDO = "usuario.senha.invalido";
		String EDICAO_EXCLUSIVA_USUARIO_JA_CADASTRADO = "edicao.exclusiva.usuario.ja.cadastrado";
		String EDICAO_EXCLUSIVA_USUARIO_MULTIPLOS_CLIENTES = "edicao.exclusiva.usuario.multiplos.clientes";
		String USUARIO_JA_CADASTRADO = "usuario.ja.cadastrado";
		String CODIGO_ATIVACAO_INVALIDO = "codigo.ativacao.invalido";
		String USUARIO_PENDENTE_VALIDACAO = "usuario.pendente.validacao";
		String USUARIO_NAO_ENCONTRADO = "usuario.nao.encontrado";
		String USUARIO_NAO_REGISTRADO = "usuario.nao.registrado";
		String USUARIO_INATIVO = "usuario.inativo";
		String USUARIO_BLOQUEADO = "usuario.bloqueado";
		String USUARIO_EDITADO_SUCESSO = "usuario.editado.sucesso";
		String ILLEGAL_ARGUMENT = "illegal.argument.exception";
		String RECOMENDACAO_EDICAO_ENVIADA = "recomendacao.edicao.enviada";
		String ENTIDADE_JA_CADASTRADA = "entidade.ja.cadastrada";
		String ENTIDADE_GRAVADA_SUCESSO = "entidade.gravada.sucesso";
		String ENTIDADE_DELETADA_SUCESSO = "entidade.deletada.sucesso";
		String ENTIDADE_EDITADA_SUCESSO = "entidade.editada.sucesso";
		String SECURITY_ACCESS_FORBIDDEN = "security.access.forbidden";
		String SECURITY_ACCESS_DENIED = "security.access.denied";
		String SECURITY_SECURITY_SESSION_TIMEOUT = "security.session.timeout";
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
		String SHORTENER = "config/google/url-shortener/shortener.json";
		String FIREBASE = "config/google/firebase/firebase.json";
		String FIREBASE_CONFIG = "config/google/firebase/firebase-config.json";
		String CONFIRMACAO_CADASTRO_CONSUMIDOR_EMAIL = "template/email/usuario/confirmacao-cadastro-consumidor.html";
		String CONFIRMACAO_CADASTRO_CONSUMIDOR_SMS = "template/sms/usuario/confirmacao-cadastro-consumidor.txt";
		String CONFIRMACAO_CADASTRO_USUARIO_EMAIL = "template/email/usuario/confirmacao-cadastro-usuario.html";
		String CONFIRMACAO_CADASTRO_USUARIO_SMS = "template/email/usuario/confirmacao-cadastro-usuario.txt";
		String SOLICITACAO_EDICAO_CADASTRO_EMAIL = "template/email/usuario/solicitacao-edicao-cadastro.html";
		String SOLICITACAO_EDICAO_CADASTRO_SMS = "template/email/usuario/solicitacao-edicao-cadastro.txt";
		String CONFIRMACAO_ASSOCIACAO_CONSUMIDOR_EMAIL = "template/email/usuario/confirmacao-associacao-cliente.html";
		String CONFIRMACAO_ASSOCIACAO_CONSUMIDOR_SMS = "template/sms/usuario/confirmacao-associacao-cliente.txt";
	}

	public interface Url {

		String DATABASE_URL_FIREBASE = "https://databaseName.firebaseio.com/";
		
	}

}
