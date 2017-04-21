package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonInclude(Include.NON_NULL)
public class ResponseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private BaseEntity<?, ?> entity;

	private String responseText;

	private AnotaaiViewException exception;

	private List<AnotaaiMessage> messages;

	/**
	 * Determina se ocorreu ou nao um erro de negocio caso tenha ocorrido a
	 * lista de mensagens estara preenchida
	 */
	private Boolean isValid;

	public ResponseEntity() {
		super();
	}

	public ResponseEntity(BaseEntity<?, ?> entity) {
		this();
		this.entity = entity;
	}

	public ResponseEntity(AnotaaiMessage mensagem) {
		addMessage(mensagem);
	}

	public BaseEntity<?, ?> getEntity() {
		return entity;
	}

	public void setEntity(BaseEntity<?, ?> entity) {
		this.entity = entity;
	}

	public AnotaaiViewException getException() {
		return exception;
	}

	public void setException(AnotaaiViewException exception) {
		this.exception = exception;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public List<AnotaaiMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<AnotaaiMessage> messages) {
		this.messages = messages;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public void addMessage(AnotaaiMessage message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(message);
	}

}
