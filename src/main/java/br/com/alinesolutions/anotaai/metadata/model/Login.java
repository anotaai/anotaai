package br.com.alinesolutions.anotaai.metadata.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoAcesso;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;


@JsonFilter("entity")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private String sessionID;

	private Boolean keepAlive;

	private static final Long sessionTime;

	private static final String cookieSessionName;

	private TipoAcesso tipoAcesso;

	static {
		sessionTime = Constant.App.SESSION_TIME;
		cookieSessionName = Constant.App.COOKIE_SESSION_NAME;
	}

	public Login() {
		super();
	}

	public Login(String email, Telefone telefone, String sessionID) {
		this();
		this.usuario = new Usuario();
		this.usuario.setEmail(email);
		this.usuario.setTelefone(telefone);
		this.sessionID = sessionID;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Long getSessionTime() {
		return sessionTime;
	}

	public String getCookieSessionName() {
		return cookieSessionName;
	}

	public TipoAcesso getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(TipoAcesso tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

}
