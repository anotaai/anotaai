package br.com.alinesolutions.anotaai.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.SessaoUsuario.SessaoUsuarioConstant;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = SessaoUsuario.class)
@NamedQueries({
	@NamedQuery(name = SessaoUsuarioConstant.FIND_ALL_KEY, query = SessaoUsuarioConstant.FIND_ALL_QUERY),
	@NamedQuery(name = SessaoUsuarioConstant.FIND_BY_SESSIONID_KEY, query = SessaoUsuarioConstant.FIND_BY_SESSIONID_QUERY),
	@NamedQuery(name = SessaoUsuarioConstant.RESET_SESSION_KEY, query = SessaoUsuarioConstant.RESET_SESSION_QUERY),
	@NamedQuery(name = SessaoUsuarioConstant.REMOVE_SESSION_KEY, query = SessaoUsuarioConstant.REMOVE_SESSION_QUERY),
	@NamedQuery(name = SessaoUsuarioConstant.COUNT_KEY, query = SessaoUsuarioConstant.COUNT_QUERY)
})
@Entity
@XmlRootElement
public class SessaoUsuario extends BaseEntity<Long, SessaoUsuario> {

	private static final long serialVersionUID = 1L;

	private String sessionID;
	private LocalDateTime ultimoAcesso;
	private Boolean keepAlive;

	@JsonBackReference(value = "sessoesAtivas")
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	private Usuario usuario;

	public SessaoUsuario() {
		super();
	}

	public SessaoUsuario(Long id, LocalDateTime ultimoAcesso, Boolean keepAlive, Long idUsuario, String nomeUsuario, String emailUsuario) {
		this();
		super.setId(id);
		this.ultimoAcesso = ultimoAcesso;
		this.keepAlive = keepAlive;
		this.usuario = new Usuario(idUsuario, nomeUsuario, emailUsuario);
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public LocalDateTime getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public interface SessaoUsuarioConstant {

		String FIELD_SESSION_ID = "sessionID";
		String FIND_ALL_KEY = "SessaoUsuario.findAll";
		String FIND_ALL_QUERY = "select new br.com.alinesolutions.anotaai.model.SessaoUsuario(su.id, su.ultimoAcesso, su.keepAlive, u.id, u.nome, u.email) from SessaoUsuario su join su.usuario u where su.keepAlive = FALSE";
		String FIND_BY_SESSIONID_KEY = "SessaoUsuario.findBySessionID";
		String FIND_BY_SESSIONID_QUERY = "select su from SessaoUsuario su where su.sessionID = :sessionID";
		String RESET_SESSION_KEY = "SessaoUsuario.resetSession";
		String RESET_SESSION_QUERY = "update SessaoUsuario set ultimoAcesso = :ultimoAcesso where sessionID = :sessionID";
		String REMOVE_SESSION_KEY = "SessaoUsuario.removeSession";
		String REMOVE_SESSION_QUERY = "delete from SessaoUsuario where sessionID = :sessionID";
		String COUNT_KEY = "SessaoUsuario.count";
		String COUNT_QUERY = "select count(su) from SessaoUsuario su where su.sessionID = :sessionID";

	}

}
