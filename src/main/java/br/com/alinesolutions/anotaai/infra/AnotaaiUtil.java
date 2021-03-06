package br.com.alinesolutions.anotaai.infra;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
	
	public ZonedDateTime now() {
		return ZonedDateTime.now();
	}
	
	public LocalDate today() {
		return LocalDate.now();
	}

	public String formatarTelefoneStr(Telefone telefone) {
		StringBuilder sb = new StringBuilder();
		if (telefone.getDdi() != null && telefone.getDdd() != null && telefone.getNumero() != null && telefone.getNumero().toString().length() == 9) {
			sb.append("(").append(telefone.getDdd()).append(") ");
			sb.append(telefone.getNumero().toString().substring(0, 5));
			sb.append("-").append(telefone.getNumero().toString().substring(5, 9));
		}
		return sb.toString();
	}

	public ZonedDateTime toZonedDateTime(String dateInString, ZoneId zone) {
		Instant instant = Instant.parse(dateInString);
		return ZonedDateTime.ofInstant(instant, zone);
	}

}
