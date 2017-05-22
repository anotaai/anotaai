package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonInclude(Include.NON_NULL)
public class ResponseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private BaseEntity<?, ?> entity;

	private ResponseList itens;

	private Login login;

	private String responseText;

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

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public ResponseList getItens() {
		return itens;
	}

	public void setItens(ResponseList itens) {
		this.itens = itens;
	}

	public void addMessage(AnotaaiMessage message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(message);
	}

	public void addMessage(String key, TipoMensagem type, Long time, String... params) {
		addMessage(new AnotaaiMessage(key, type, time, params));
	}

}
