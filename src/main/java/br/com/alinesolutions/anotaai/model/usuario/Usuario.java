package br.com.alinesolutions.anotaai.model.usuario;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.domain.Operadora;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Usuario.UsuarioConstant;
import br.com.alinesolutions.anotaai.model.util.Arquivo;

@JsonFilter("entity")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Usuario.class)
@NamedQueries({ 
	@NamedQuery(name = UsuarioConstant.FIND_BY_EMAIL_KEY, query = UsuarioConstant.FIND_BY_EMAIL_QUERY),
	@NamedQuery(name = UsuarioConstant.FIND_BY_TELEFONE_LOGIN_KEY, query = UsuarioConstant.FIND_BY_TELEFONE_LOGIN_QUERY),
	@NamedQuery(name = UsuarioConstant.FIND_BY_TELEFONE_PERSIST_KEY, query = UsuarioConstant.FIND_BY_TELEFONE_PERSIST_QUERY),
	@NamedQuery(name = UsuarioConstant.FIND_BY_TELEFONE_KEY, query = UsuarioConstant.FIND_BY_TELEFONE_QUERY),
	@NamedQuery(name = UsuarioConstant.ACCESS_KEY, query = UsuarioConstant.ACCESS_QUERY),
	@NamedQuery(name = UsuarioConstant.COUNT_USURIO_BY_EMAIL_KEY, query = UsuarioConstant.COUNT_USURIO_BY_EMAIL_QUERY),
	@NamedQuery(name = UsuarioConstant.COUNT_USURIO_BY_TELEFONE_KEY, query = UsuarioConstant.COUNT_USURIO_BY_TELEFONE_QUERY),
	@NamedQuery(name = UsuarioConstant.SITUACAO_USUARIO_KEY, query = UsuarioConstant.SITUACAO_USUARIO_QUERY),
	@NamedQuery(name = UsuarioConstant.FIND_FOR_ACTIVATE_KEY, query = UsuarioConstant.FIND_FOR_ACTIVATE_QUERY),
	@NamedQuery(name = UsuarioConstant.FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_KEY, query = UsuarioConstant.FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_QUERY),
	@NamedQuery(name = UsuarioConstant.LOAD_FILE_KEY, query = UsuarioConstant.LOAD_FILE_QUERY)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Usuario set ativo = false where id = ?")
@XmlRootElement
public class Usuario extends BaseEntity<Long, Usuario> {

	private static final long serialVersionUID = 1L;

	private String nome;

	@Email
	@Column(unique = true)
	private String email;
	
	private String senha;
	
	private LocalDateTime dataCadastro;
	
	private String codigoAtivacao;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = true)
	private Arquivo fotoPerfil;

	@Enumerated(EnumType.ORDINAL)
	private SituacaoUsuario situacao;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Telefone telefone;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Preferencia preferencia;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
	private List<UsuarioPerfil> perfis;

	@JsonManagedReference(value="sessoesAtivas")
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<SessaoUsuario> sessoesAtivas;

	public Usuario() {
		super();
	}

	public Usuario(Long id, String nome, String email) {
		super();
		super.setId(id);
		this.nome = nome;
		this.email = email;
	}
	
	public Usuario(Long id, String nome, String email, Long idTelefone,Integer ddd, Integer ddi,
			Integer numero,SituacaoUsuario situacaoUsuario) {
		super();
		super.setId(id);
		this.nome = nome;
		this.email = email;
		this.telefone = new Telefone();
		this.telefone.setId(idTelefone);
		this.telefone.setDdi(ddi);
		this.telefone.setDdd(ddd);
		this.telefone.setNumero(numero);
		this.setSituacao(situacaoUsuario);
	}

	public Usuario(Long id, String nome, String email, SituacaoUsuario situacao) {
		this(id, nome, email);
		this.situacao = situacao;
	}

	public Usuario(Long id, String nome, String email, LocalDateTime dataCadastro, Long idTelefone, Integer ddi, Integer ddd,
			Integer numero, Operadora operadora) {
		this(id, nome, email);
		this.dataCadastro = dataCadastro;
		this.telefone = new Telefone();
		this.telefone.setId(idTelefone);
		this.telefone.setDdi(ddi);
		this.telefone.setDdd(ddd);
		this.telefone.setNumero(numero);
	}

	public Usuario(Long id, String nome, String email, LocalDateTime dataCadastro, Long idTelefone, Integer ddi, Integer ddd,
			Integer numero, Operadora operadora, SituacaoUsuario situacao) {
		this(id, nome, email, dataCadastro, idTelefone, ddi, ddd, numero, operadora);
		this.situacao = situacao;
	}

	public Usuario(Long id, String nome, String email, LocalDateTime dataCadastro, Long idTelefone, Integer ddi, Integer ddd,
			Integer numero, Operadora operadora, String senha) {
		this(id, nome, email, dataCadastro, idTelefone, ddi, ddd, numero, operadora);
		this.senha = senha;
	}

	public Usuario(Long id, String nome, String email, LocalDateTime dataCadastro, Long idTelefone, Integer ddi, Integer ddd,
			Integer numero, Operadora operadora, String senha, SituacaoUsuario situacao) {
		this(id, nome, email, dataCadastro, idTelefone, ddi, ddd, numero, operadora, senha);
		this.situacao = situacao;
	}

	public Usuario(String email, String senha) {
		this.email = email;
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Arquivo getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(Arquivo fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public List<UsuarioPerfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<UsuarioPerfil> perfis) {
		this.perfis = perfis;
	}

	public List<SessaoUsuario> getSessoesAtivas() {
		return sessoesAtivas;
	}

	public void setSessoesAtivas(List<SessaoUsuario> sessoesAtivas) {
		this.sessoesAtivas = sessoesAtivas;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public String getCodigoAtivacao() {
		return codigoAtivacao;
	}

	public void setCodigoAtivacao(String codigoAtivacao) {
		this.codigoAtivacao = codigoAtivacao;
	}

	public SituacaoUsuario getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoUsuario situacao) {
		this.situacao = situacao;
	}

	public Preferencia getPreferencia() {
		return preferencia;
	}

	public void setPreferencia(Preferencia preferencia) {
		this.preferencia = preferencia;
	}
	
	public interface UsuarioConstant {
		
		String FIELD_EMAIL = "email";
		String FIELD_NOME = "nome";
		String FIELD_TELEFONE = "telefone";
		String FIELD_CODIGO_ATIVACAO = "codigoAtivacao";
		String FIND_BY_EMAIL_KEY = "Usuario.findByEmail";
		String FIND_BY_EMAIL_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Usuario(u.id, u.nome, u.email, u.dataCadastro, t.id, t.ddi, t.ddd, t.numero, t.operadora, u.senha, u.situacao) from Usuario u join u.telefone t where u.email like :email";
		String FIND_BY_TELEFONE_LOGIN_KEY = "Usuario.findByTelefoneLogin";
		String FIND_BY_TELEFONE_LOGIN_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Usuario(u.id, u.nome, u.email, u.dataCadastro, t.id, t.ddi, t.ddd, t.numero, t.operadora, u.senha, u.situacao) from Usuario u join u.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero";
		String FIND_BY_TELEFONE_PERSIST_KEY = "Usuario.findByTelefonePersist";
		String FIND_BY_TELEFONE_PERSIST_QUERY = "select u from Usuario u join fetch u.perfis join u.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero";
		String ACCESS_KEY = "Usuario.accessKey";
		String ACCESS_QUERY = "select u from Usuario u join fetch u.perfis where u.email like :email";
		String FIND_BY_TELEFONE_KEY = "Usuario.findByTelefone";
		String FIND_BY_TELEFONE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Usuario(u.id, u.nome, u.email, u.situacao) from Usuario u join u.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero";
		String COUNT_USURIO_BY_EMAIL_KEY = "Usuario.countByEmail";
		String COUNT_USURIO_BY_EMAIL_QUERY = "select count(u.id) from Usuario u where u.email = :email";
		String COUNT_USURIO_BY_TELEFONE_KEY = "Usuario.countByTelefone";
		String COUNT_USURIO_BY_TELEFONE_QUERY = "select count(u.id) from Usuario u join u.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero";
		String SITUACAO_USUARIO_KEY = "Usuario.situacaoUsuario";
		String SITUACAO_USUARIO_QUERY = "select u.situacao from Usuario u where u = :usuario";
		String FIND_FOR_ACTIVATE_KEY = "Usuario.findActivate";
		String FIND_FOR_ACTIVATE_QUERY = "select u from Usuario u where u.codigoAtivacao = :codigoAtivacao";
		String FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_KEY = "Usuario.findForRegister";
		String FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Usuario(u.id, u.nome, u.email, u.dataCadastro, t.id, t.ddi, t.ddd, t.numero, t.operadora, u.situacao) from Usuario u join u.telefone t where u.codigoAtivacao = :codigoAtivacao";
		String LOAD_FILE_KEY= "Usuario.loadFile";
		String LOAD_FILE_QUERY = "select new br.com.alinesolutions.anotaai.model.util.Arquivo(a.path, a.name) from Usuario u join u.fotoPerfil a where u.id = :id";

	}
	
}
