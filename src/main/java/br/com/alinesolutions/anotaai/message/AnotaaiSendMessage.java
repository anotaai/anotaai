package br.com.alinesolutions.anotaai.message;

import javax.ejb.Local;

import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;

@Local
public interface AnotaaiSendMessage {

	void notificacaoRegistroUsuario(Usuario usuario);
	
	void notificacaoRegistroConsumidor(ClienteConsumidor clienteConsumidor);

	void recomendarEdicaoDeCadastro(ClienteConsumidor clienteConsumidor);

	void notificacaoAssociacaoConsumidorCliente(ClienteConsumidor clienteConsumidor);
	
}
