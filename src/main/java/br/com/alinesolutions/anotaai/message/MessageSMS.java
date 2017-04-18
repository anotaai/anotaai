package br.com.alinesolutions.anotaai.message;

import javax.ejb.Stateless;

import br.com.alinesolutions.anotaai.message.qualifier.SMS;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;

@SMS
@Stateless
public class MessageSMS implements AnotaaiSendMessage {

	@Override
	public void notificacaoRegistroUsuario(Usuario usuario) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificacaoRegistroConsumidor(ClienteConsumidor clienteConsumidor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recomendarEdicaoDeCadastro(ClienteConsumidor clienteConsumidor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificacaoAssociacaoConsumidorCliente(ClienteConsumidor clienteConsumidor) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void notificacaoRenewPassword(Usuario usuario) {
		// TODO Auto-generated method stub
		
	}

}
