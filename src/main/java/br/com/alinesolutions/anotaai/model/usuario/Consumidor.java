package br.com.alinesolutions.anotaai.model.usuario;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPessoa;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.Operadora;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor.ConsumidorConstant;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Consumidor.class)
@NamedQueries({
	@NamedQuery(name = ConsumidorConstant.FIND_CLIENTE_KEY, query = ConsumidorConstant.FIND_CLIENTE_QUERY),
	@NamedQuery(name = ConsumidorConstant.FIND_BY_NOME_COUNT, query = ConsumidorConstant.FIND_BY_NOME_QUERY_COUNT),
	@NamedQuery(name = ConsumidorConstant.LIST_ALL_COUNT, query = ConsumidorConstant.LIST_ALL_QUERY_COUNT),
	@NamedQuery(name = ConsumidorConstant.FIND_BY_TELEFONE_KEY, query = ConsumidorConstant.FIND_BY_TELEFONE_QUERY)
})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Consumidor set ativo = false where id = ?")
@XmlRootElement
public class Consumidor extends BaseEntity<Long, Consumidor> implements IPessoa {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario usuario;

	private Date dataCadastro;

	@OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL)
	private List<ClienteConsumidor> clientes;

	@OneToMany(mappedBy = "folhaCaderneta")
	private List<FolhaCadernetaVenda> comparas;

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

	public Consumidor(Long id, Long idUsuario, String nome, String email, Long idTelefone, Integer ddi, Integer ddd, Integer numero, Operadora operadora) {
		this(id, idUsuario, nome, email);
		this.usuario.setTelefone(new Telefone());
		this.usuario.getTelefone().setId(idTelefone);
		this.usuario.getTelefone().setDdi(ddi);
		this.usuario.getTelefone().setDdd(ddd);
		this.usuario.getTelefone().setNumero(numero);
	}

	@Override
	@Transient
	public TipoPessoa getTipoPessoa() {
		return TipoPessoa.CONSUMIDOR;
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

	public List<FolhaCadernetaVenda> getComparas() {
		return comparas;
	}

	public void setComparas(List<FolhaCadernetaVenda> comparas) {
		this.comparas = comparas;
	}

	public interface ConsumidorConstant {
		String FIND_CLIENTE_KEY = "Consumidor.findClienteByConsumidor";
		String FIND_CLIENTE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.Cliente(cliente.id, cliente.nomeComercial) from Consumidor consumidor left join consumidor.clientes cc left join cc.cliente cliente where cliente = :cliente and consumidor = :consumidor";
		
		String FIELD_USUARIO = "usuario";

		String FIND_BY_NOME_COUNT = "Consumidor.findByNameCount";
		String LIST_ALL_COUNT = "Consumidor.listAllCount";

		String FIND_BY_NOME_QUERY_COUNT = "select count(c.id) from Consumidor c left join c.clientes cc left join cc.consumidor cs join cs.usuario u join u.telefone t where cc.cliente = :cliente and u.nome =:nome and cc.situacao = :situacao";
		String LIST_ALL_QUERY_COUNT = "select count(c.id) from Consumidor c left join c.clientes cc left join cc.consumidor cs join cs.usuario u join u.telefone t where cc.cliente = :cliente and cc.situacao = :situacao";
		String FIND_BY_TELEFONE_KEY = "Consumidor.findByTelefone";
		String FIND_BY_TELEFONE_QUERY = "select c from Consumidor c left join c.usuario u left join u.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero";

	}
}
