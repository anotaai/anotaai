package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;

public class AnotaaiMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean isKey;

	private String key;

	private String text;

	private String[] params;

	private TipoMensagem type;

	private Long time;

	public AnotaaiMessage(String key, TipoMensagem type, Long time, String... params) {
		super();
		this.key = key;
		this.params = params;
		this.type = type;
		this.time = time;
		this.isKey = Boolean.TRUE;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public TipoMensagem getType() {
		return type;
	}

	public void setType(TipoMensagem type) {
		this.type = type;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getIsKey() {
		return isKey;
	}

}
