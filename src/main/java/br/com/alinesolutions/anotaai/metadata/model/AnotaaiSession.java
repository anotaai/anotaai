package br.com.alinesolutions.anotaai.metadata.model;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import br.com.alinesolutions.anotaai.model.SessaoUsuario;

public class AnotaaiSession {

	private HttpServletRequest uniqueRequest;
	private SessaoUsuario sessao;
	private static final String REFERER_HEADER = "Referer";
	private static final String TWO_POINTS = ":";
	private static final String TWO_BARS = "//";

	public AnotaaiSession(HttpServletRequest uniqueRequest, SessaoUsuario sessao) {
		super();
		this.uniqueRequest = uniqueRequest;
		this.sessao = sessao;
	}

	public HttpServletRequest getUniqueRequest() {
		return uniqueRequest;
	}
	
	public String getClientHost() {
		try {
			URL u = new URL(uniqueRequest.getHeader(REFERER_HEADER));
			StringBuilder host = new StringBuilder();
			host.append(u.getProtocol()).append(TWO_POINTS).append(TWO_BARS).append(u.getHost()).append(TWO_POINTS).append(u.getPort());
			return host.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public SessaoUsuario getSessao() {
		return sessao;
	}

	public void setUniqueRequest(HttpServletRequest uniqueRequest) {
		this.uniqueRequest = uniqueRequest;
	}

	public void setSessao(SessaoUsuario sessao) {
		this.sessao = sessao;
	}

}
