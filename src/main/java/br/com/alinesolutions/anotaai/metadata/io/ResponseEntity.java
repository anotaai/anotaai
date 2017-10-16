package br.com.alinesolutions.anotaai.metadata.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.Login;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonInclude(Include.NON_NULL)
public class ResponseEntity <T extends BaseEntity<?, ?>>  implements Serializable {

	private static final long serialVersionUID = 1L;

	private T entity;

	private ResponseList<T> list;

	private Login login;

	private String responseText;

	private List<AnotaaiMessage> messages;
	
	/**
	 * Id do processo, A cada requisicao em processos sensiveis, como venda por exempo, um novo identificador do processo, PID (process id) 
	 * serah gerado e deverah ser utilizado na proxima requisicao,
	 *  
	 */
	//TODO ANOTAI - Implementar seguranca em processos sensiveis.
	private String pid;

	/**
	 * Determina se ocorreu ou nao um erro de negocio caso tenha ocorrido a
	 * lista de mensagens estara preenchida
	 */
	private Boolean isValid;

	public ResponseEntity() {
		super();
	}

	public ResponseEntity(T entity) {
		this();
		this.entity = entity;
	}

	public ResponseEntity(AnotaaiMessage mensagem) {
		addMessage(mensagem);
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
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

	public ResponseList<T> getList() {
		return list;
	}

	public void setList(ResponseList<T> list) {
		this.list = list;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
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
