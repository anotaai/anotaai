package br.com.alinesolutions.anotaai.i18n;

public interface IMessage {
	
	Long DEFAULT_TIME_VIEW = 5000L;
	Long LONG_TIME_VIEW = 10000L;
	Long KEEP_ALIVE_TIME_VIEW = -1L;
	
	String ENTIDADE_GRAVACAO_SUCESSO = "entidade.gravacao.sucesso";
	String ENTIDADE_EDICAO_SUCESSO = "entidade.edicao.sucesso";
	String ENTIDADE_EXCLUSAO_SUCESSO = "entidade.exclusao.sucesso";
	
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
	String SECURITY_ACCESS_FORBIDDEN = "security.access.forbidden";
	String SECURITY_ACCESS_DENIED = "security.access.denied";
	String SECURITY_SECURITY_SESSION_TIMEOUT = "security.session.timeout";
	String SOLICITACAO_ALTERACAO_SENHA = "mensagem.alteracao.senha";
	

}
