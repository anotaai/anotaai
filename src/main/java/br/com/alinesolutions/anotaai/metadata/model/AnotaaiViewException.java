package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;

public class AnotaaiViewException implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<AnotaaiMessage> anotaaiExceptionMessages;
	
	public AnotaaiViewException() {
		anotaaiExceptionMessages = new ArrayList<>();
	}
	
	public AnotaaiViewException(String key, TipoMensagem tipoMensagem, Long time, String... params) {
		this();
		setMessage(key, tipoMensagem, time, params);
	}

	public AnotaaiViewException setMessage(String key, TipoMensagem tipoMensagem, Long time, String... params) {
		anotaaiExceptionMessages.add(new AnotaaiMessage(key, tipoMensagem, time, params));
		return this;
	}
	
	public List<AnotaaiMessage> getAnotaaiExceptionMessages() {
		return anotaaiExceptionMessages;
	}
	
}
