package br.com.alinesolutions.anotaai.i18n;

public class Locale {

	private String key;
	private String label;
	private String code;

	public Locale(String key, String label, String code) {
		super();
		this.key = key;
		this.label = label;
		this.code = code;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
