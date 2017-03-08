package br.com.alinesolutions.anotaai.model.usuario;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.Operadora;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor.ConsumidorConstant;
import br.com.alinesolutions.anotaai.model.venda.ConsumidorVenda;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Consumidor.class)
@NamedQueries({
		@NamedQuery(name = ConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_KEY, query = ConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_QUERY) })
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Consumidor set ativo = false where id = ?")
@XmlRootElement
public class Consumidor extends BaseEntity<Long> implements IPessoa {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario usuario;

	private Date dataCadastro;

	@OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL)
	private List<ClienteConsumidor> clientes;

	@OneToMany(mappedBy = "consumidor")
	private List<ConsumidorVenda> comparas;

	public Consumidor() {
		super();
	}

	public Consumidor(Long id, Long idUsuario, String nome, String email) {
		this();
		super.setId(id);
		this.usuario = new Usuario();
		this.usuario.setId(idUsuario);
		this.usuario.setNome(nome);
		this.usuario.setEmail(email);
	}

	public Consumidor(Long id, Long idUsuario, String nome, String email, Long idTelefone, Integer ddi, Integer ddd,
			Integer numero, Operadora operadora) {
		this(id, idUsuario, nome, email);
		this.usuario.setTelefone(new Telefone());
		this.usuario.getTelefone().setId(idTelefone);
		this.usuario.getTelefone().setDdi(ddi);
		this.usuario.getTelefone().setDdd(ddd);
		this.usuario.getTelefone().setNumero(numero);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public List<ClienteConsumidor> getClientes() {
		return clientes;
	}

	public void setClientes(List<ClienteConsumidor> clientes) {
		this.clientes = clientes;
	}

	public List<ConsumidorVenda> getComparas() {
		return comparas;
	}

	public void setComparas(List<ConsumidorVenda> comparas) {
		this.comparas = comparas;
	}

	public interface ConsumidorConstant {
		String LIST_CLIENTE_CONSUMIDOR_KEY = "Consumidor.findConsumidorByCliente";
		String LIST_CLIENTE_CONSUMIDOR_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Consumidor(c.id, u.id, u.nome, u.email, t.id, t.ddi, t.ddd, t.numero, t.operadora) from Consumidor c left join c.clientes cc left join cc.consumidor cs join cs.usuario u join u.telefone t where cc.cliente = :cliente order by u.nome";
		String FIELD_USUARIO = "usuario";
		String FIELD_CLIENTE = "cliente";
	}
}
