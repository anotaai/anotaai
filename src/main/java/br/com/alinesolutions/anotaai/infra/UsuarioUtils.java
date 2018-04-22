package br.com.alinesolutions.anotaai.infra;

import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;

public class UsuarioUtils {

	private static final ThreadLocal<Usuario> uniqueUsuario = new ThreadLocal<Usuario>();

	private static final ThreadLocal<Cliente> uniqueCliente = new ThreadLocal<Cliente>();

	public static void putUsuario(Usuario usuario) {
		uniqueUsuario.set(usuario);
	}

	public static Usuario getUsuario() {
		return uniqueUsuario.get();
	}

	public static void putCliente(Cliente cliente) {
		uniqueCliente.set(cliente);
	}

	public static Cliente getCliente() {
		return uniqueCliente.get();
	}

}
