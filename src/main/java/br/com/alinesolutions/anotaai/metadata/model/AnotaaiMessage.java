package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;

public class AnotaaiMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean isKey;

	private String key;

	private String text;

	private Map<String, String> params;

	private TipoMensagem type;

	private Long time;

	public AnotaaiMessage(String key, TipoMensagem type, Long time, String... params) {
		super();
		this.key = key;
		this.type = type;
		this.time = time;
		this.isKey = Boolean.TRUE;
		this.params = new HashMap<>();
		this.addParams(params);
	}

	private void addParams(String[] params) {
		for (int i = 0; i < params.length; i++) {
			this.addParams(String.valueOf(i), params[i]);
		}
	}
	
	public void addParams(String key, String value) {
		this.params.put(key, value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
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
