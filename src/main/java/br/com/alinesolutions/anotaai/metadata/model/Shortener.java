package br.com.alinesolutions.anotaai.metadata.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidade que representa as credenciais do encurtador de url 
 * do google, existe um arquivo json na pasta resources que precisa ser
 * carregado e esta entidade representa as suas propriedades
 * @author gleidson
 *
 */
@XmlRootElement
public class Shortener {

	private String key;
	private String application;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

}
