package br.com.alinesolutions.anotaai.i18n;

public interface IMessage {
	
	String ENTIDADE_GRAVACAO_SUCESSO = "entidade.gravacao.sucesso";
	String ENTIDADE_EDICAO_SUCESSO = "entidade.edicao.sucesso";
	String ENTIDADE_EXCLUSAO_SUCESSO = "entidade.exclusao.sucesso";
	
	interface Message {
		String VENDA_OBRIGATORIA = "message.obrigatorio.ivenda.venda";
		String MOVIMENTACAO_PRODUTO_OBRIGATORIA = "message.obrigatorio.ivenda.movimentacaoproduto";
		String PRODUTO_OBRIGATORIO = "message.obrigatorio.ivenda.produto";
		String ITEM_VENDA_OBRIGATORIO = "message.obrigatorio.ivenda.itemvenda";
		String CONSUMIDOR_OBRIGATORIO = "message.obrigatorio.ivenda.consumidor";
		String CONSUMIDOR_INVALIDO = "message.invalido.ivenda.consumidor.invalido";
		String PRODUTO_NAO_CADASTRADO = "message.produto.ivenda.naocadastrado";
		String QUANTIDADE_VENDIDA = "message.produto.ivenda.quantidade";
	}
	
	String EMAIL_JA_CADASTRADO = "email.ja.cadastrado";
	String ERRO_NAO_IDENTIFICADO = "msg.erro.nao.identificado";
	String CAMPO_OBRIGATORIO_NAO_INFORMADO = "campo.obrigatorio.nao.informado";
	String TELEFONE_JA_CADASTRADO = "telefone.ja.cadastrado";
	String CONSUMIDOR_JA_CADASTRADO = "consumidor.ja.cadastrado";
	String CONSUMIDOR_JA_REGISTRADO = "consumidor.ja.registrado";
	String USUARIO_SENHA_INVALIDO = "usuario.senha.invalido";
	String EDICAO_EXCLUSIVA_USUARIO_JA_CADASTRADO = "usuario.cadastro.ativado.efetuado";
	String EDICAO_EXCLUSIVA_USUARIO_MULTIPLOS_CLIENTES = "edicao.exclusiva.usuario.multiplos.clientes";
	String USUARIO_JA_CADASTRADO = "usuario.ja.cadastrado";
	String USUARIO_CADASTRO_ATIVADO_SUCESSO = "usuario.cadastro.ativado.sucesso";
	String CODIGO_ATIVACAO_INVALIDO = "codigo.ativacao.invalido";
	String USUARIO_PENDENTE_VALIDACAO = "usuario.pendente.validacao";
	String USUARIO_NAO_ENCONTRADO = "usuario.nao.encontrado";
	String USUARIO_NAO_REGISTRADO = "usuario.nao.registrado";
	String USUARIO_INATIVO = "usuario.inativo";
	String USUARIO_BLOQUEADO = "usuario.bloqueado";
	String USUARIO_EDITADO_SUCESSO = "usuario.editado.sucesso";
	String ILLEGAL_ARGUMENT = "illegal.argument.exception";
	String RECOMENDACAO_EDICAO_ENVIADA = "usuario.cadastro.recomendacaoedicao";
	String ENTIDADE_JA_CADASTRADA = "entidade.ja.cadastrada";
	String SECURITY_ACCESS_FORBIDDEN = "security.access.forbidden";
	String SECURITY_ACCESS_DENIED = "security.access.denied";
	String SECURITY_SECURITY_SESSION_TIMEOUT = "security.session.timeout";
	String SOLICITACAO_ALTERACAO_SENHA = "mensagem.alteracao.senha";
	String CONSUMIDOR_INVALIDO = "mensagem.venda.consumidor.naocadastrado";	

}
