package br.com.alinesolutions.anotaai.message;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import br.com.alinesolutions.anotaai.metadata.model.Email;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.service.UrlShortenerService;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.LoadResource;
import br.com.alinesolutions.anotaai.util.RequestUtils;

@br.com.alinesolutions.anotaai.message.qualifier.Email

@Singleton
@Startup
public class MessageEmail implements AnotaaiSendMessage {

	@Inject
	@Any
	private Event<Email> event;

	@Inject
	private LoadResource resourceFile;

	@Inject
	private UrlShortenerService shortener;
	
	@Override
	public void notificacaoRegistroUsuario(Usuario usuario) {
		
		String htmlMensagem = resourceFile.getFile(Constant.FileNane.CONFIRMACAO_CADASTRO_USUARIO_EMAIL);
		StringBuilder link = new StringBuilder(RequestUtils.getRequest().getClientHost());
		link.append("/activate/");
		link.append(usuario.getCodigoAtivacao());
		String linkShort = shortener.shortener(link.toString());
		htmlMensagem = htmlMensagem.replace("{linkAtivacao}", linkShort);
		int index = usuario.getNome().indexOf(" ") > 0 ? usuario.getNome().indexOf(" ") : usuario.getNome().length();
		htmlMensagem = htmlMensagem.replace("{nome}", usuario.getNome().substring(0, index));

		Email email = new Email("📝  Anota ai - Gerenciamento de caderneta online, " + usuario.getNome(),
				htmlMensagem.toString(), usuario.getEmail());
		event.fire(email);
	}

	public void notificacaoRegistroConsumidor(ClienteConsumidor clienteConsumidor) {
		
		String htmlMensagem = resourceFile.getFile(Constant.FileNane.CONFIRMACAO_CADASTRO_CONSUMIDOR_EMAIL);
		Usuario usuarioCliente = clienteConsumidor.getCliente().getUsuario();
		Usuario usuarioConsumidor = clienteConsumidor.getConsumidor().getUsuario();
		
		StringBuilder link = new StringBuilder(RequestUtils.getRequest().getClientHost());
		link.append("/comprador/");
		link.append(usuarioConsumidor.getCodigoAtivacao());
		 
  
		String linkShort = shortener.shortener(link.toString());
		htmlMensagem = htmlMensagem.replace("{linkAtivacao}", linkShort);
		int index = usuarioConsumidor.getNome().indexOf(" ") > 0 ? usuarioConsumidor.getNome().indexOf(" ")
				: usuarioConsumidor.getNome().length();
		htmlMensagem = htmlMensagem.replace("{nomeConsumidor}", usuarioConsumidor.getNome().substring(0, index));
		htmlMensagem = htmlMensagem.replace("{nomeUsuario}", usuarioCliente.getNome());

		Email email = new Email("📝  Anota ai - Gerenciamento de caderneta online, " + usuarioCliente.getNome(),htmlMensagem.toString(), usuarioConsumidor.getEmail());
		event.fire(email);
	}

	@Override
	public void recomendarEdicaoDeCadastro(ClienteConsumidor clienteConsumidor) {
		
		
		String htmlMensagem = resourceFile.getFile(Constant.FileNane.SOLICITACAO_EDICAO_CADASTRO_EMAIL);
		StringBuilder link = new StringBuilder(RequestUtils.getRequest().getClientHost()).append("/login");
		Usuario usuarioCliente = clienteConsumidor.getCliente().getUsuario();
		Usuario usuarioConsumidor = clienteConsumidor.getConsumidor().getUsuario();
		htmlMensagem = htmlMensagem.replace("{nomeCliente}", usuarioCliente.getNome());
		htmlMensagem = htmlMensagem.replace("{nomeConsumidor}", usuarioConsumidor.getNome());
		htmlMensagem = htmlMensagem.replace("{link}", link.toString());
		
		Email email = new Email("📝  Anota ai - Atualização de cadastro, " + usuarioCliente.getNome(),
				htmlMensagem.toString(), usuarioConsumidor.getEmail());
		event.fire(email);
		
	}

	@Override
	public void notificacaoAssociacaoConsumidorCliente(ClienteConsumidor clienteConsumidor) {
		
		String htmlMensagem = resourceFile.getFile(Constant.FileNane.CONFIRMACAO_ASSOCIACAO_CONSUMIDOR_EMAIL);
		StringBuilder link = new StringBuilder(RequestUtils.getRequest().getClientHost()).append("/login");
		Usuario usuarioCliente = clienteConsumidor.getCliente().getUsuario();
		Usuario usuarioConsumidor = clienteConsumidor.getConsumidor().getUsuario();
		htmlMensagem = htmlMensagem.replace("{nomeCliente}", usuarioCliente.getNome());
		htmlMensagem = htmlMensagem.replace("{nomeConsumidor}", usuarioConsumidor.getNome());
		htmlMensagem = htmlMensagem.replace("{link}", link.toString());
		
		Email email = new Email("📝  Anota ai - " + usuarioCliente.getNome(),
				htmlMensagem.toString(), usuarioConsumidor.getEmail());
		event.fire(email);
	}
	
	@Override
	public void notificacaoRenewPassword(Usuario usuario) {
		String htmlMensagem = resourceFile.getFile(Constant.FileNane.NOTIFICACAO_RENEW_PASSWORD_EMAIL);
		StringBuilder urlRenewPassword = new StringBuilder(RequestUtils.getRequest().getClientHost()).append("/renew/");
		urlRenewPassword.append(usuario.getCodigoAtivacao());
		String link = shortener.shortener(urlRenewPassword.toString());
		
		htmlMensagem = htmlMensagem.replace("{nome}", usuario.getNome());
		htmlMensagem = htmlMensagem.replace("{linkRedefinicao}", link.toString());
		
		Email email = new Email("📝  Anota ai - Redefinição de Senha", htmlMensagem.toString(), usuario.getEmail());
		event.fire(email);
	}
	
}
