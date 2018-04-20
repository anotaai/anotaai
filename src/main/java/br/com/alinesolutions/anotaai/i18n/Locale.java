package br.com.alinesolutions.anotaai.i18n;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Locale {

	PT("pt", "Portugues", "pt-BR"),
	EN("en", "English", "en-US");

	private static Map<String, Locale> locales = new HashMap<>();
	
	static {
		locales.put("", Locale.PT);
		locales.put("", Locale.EN);
	}
	
	private Locale(String key, String label, String code) {
		this.key = key;
		this.label = label;
		this.code = code;
	}

	private String key;
	private String label;
	private String code;

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
	
	public static Locale getByKey(String key) {
		return locales.values().stream().filter(locale -> locale.key == key).findFirst().orElseThrow(() -> new IllegalArgumentException(key));
	}

}