package br.com.alinesolutions.anotaai.metadata.model;

import javax.servlet.http.HttpServletRequest;

import br.com.alinesolutions.anotaai.model.SessaoUsuario;

public class AnotaaiSession {

	private HttpServletRequest uniqueRequest;
	private SessaoUsuario sessao;

	public AnotaaiSession(HttpServletRequest uniqueRequest, SessaoUsuario sessao) {
		super();
		this.uniqueRequest = uniqueRequest;
		this.sessao = sessao;
	}

	public HttpServletRequest getUniqueRequest() {
		return uniqueRequest;
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
