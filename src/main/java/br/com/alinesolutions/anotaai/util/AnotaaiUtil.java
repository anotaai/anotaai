package br.com.alinesolutions.anotaai.util;

import br.com.alinesolutions.anotaai.model.usuario.Telefone;

public class AnotaaiUtil {

	private static AnotaaiUtil instance;

	static {
		instance = new AnotaaiUtil();
	}

	private AnotaaiUtil() {
		super();
	}

	public static AnotaaiUtil getInstance() {
		return instance;
	}

	public String formatarTelefoneStr(Telefone telefone) {
		StringBuilder sb = new StringBuilder();
		if (telefone.getDdi() != null && telefone.getDdd() != null && telefone.getNumero() != null
				&& telefone.getNumero().toString().length() == 9) {
			sb.append("(").append(telefone.getDdd()).append(") ");
			sb.append(telefone.getNumero().toString().substring(0, 5));
			sb.append("-").append(telefone.getNumero().toString().substring(5, 9));
		}

		return sb.toString();
	}

}
