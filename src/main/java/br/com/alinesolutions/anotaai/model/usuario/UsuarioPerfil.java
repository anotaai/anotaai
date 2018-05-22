package br.com.alinesolutions.anotaai.model.usuario;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil.UsuarioConstant;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=UsuarioPerfil.class)
@NamedQueries({ 
	@NamedQuery(name = UsuarioConstant.FIND_BY_EMAIL_KEY, query = UsuarioConstant.FIND_BY_EMAIL_QUERY) 
})
@Entity

@Where(clause = "ativo = true")
@SQLDelete(sql = "update UsuarioPerfil set ativo = false where id = ?")
@XmlRootElement
public class UsuarioPerfil extends BaseEntity<Long, UsuarioPerfil> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(cascade = CascadeType.DETACH)
	private Usuario usuario;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Perfil perfil;

	public UsuarioPerfil(Long idUsuarioPerfil, Long idUsuario, String nomeUsuario, Perfil perfil) {
		super.setId(idUsuarioPerfil);
		this.usuario = new Usuario();
		this.usuario.setId(idUsuario);
		this.usuario.setNome(nomeUsuario);
		this.perfil = perfil;
	}

	public UsuarioPerfil() {
		super();
	}

	public UsuarioPerfil(Usuario usuario, Perfil perfil) {
		super();
		this.usuario = usuario;
		this.perfil = perfil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public interface UsuarioConstant {
		String EMAIL_FIELD = "email";
		String FIND_BY_EMAIL_KEY = "UsuarioPerfil.findByEmail";
		String FIND_BY_EMAIL_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil(up.id, up.usuario.id, up.usuario.nome, up.perfil) from UsuarioPerfil up where up.usuario.email like :email";
	}

}
