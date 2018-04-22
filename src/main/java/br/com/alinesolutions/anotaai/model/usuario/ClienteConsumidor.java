package br.com.alinesolutions.anotaai.model.usuario;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.infra.ReferencedNamedQuery;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.Operadora;
import br.com.alinesolutions.anotaai.model.domain.SituacaoConsumidor;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor.ClienteConsumidorConstant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ClienteConsumidor.class)
@NamedQueries({
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_TELEFONE_KEY, query = ClienteConsumidorConstant.FIND_BY_TELEFONE_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_ID_KEY, query = ClienteConsumidorConstant.FIND_BY_ID_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_NOME_KEY, query = ClienteConsumidorConstant.FIND_BY_NOME_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_NOME_CONSUMIDOR_KEY, query = ClienteConsumidorConstant.FIND_BY_NOME_CONSUMIDOR_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_KEY, query = ClienteConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.LOAD_BY_CONSUMIDOR_KEY, query = ClienteConsumidorConstant.LOAD_BY_CONSUMIDOR_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.COUNT_USUARIO_KEY, query = ClienteConsumidorConstant.COUNT_USUARIO_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_CLIENTE_KEY, query = ClienteConsumidorConstant.FIND_BY_CLIENTE_QUERY) })
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ClienteConsumidor set ativo = false where id = ?")
@XmlRootElement
public class ClienteConsumidor extends BaseEntity<Long, ClienteConsumidor> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private Cliente cliente;

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
	private Consumidor consumidor;
	
	private String nomeConsumidor;

	private Date dataAssociacao;

	@Enumerated(EnumType.ORDINAL)
	private SituacaoConsumidor situacao;

	public ClienteConsumidor() {
		super();
	}
	
	@ReferencedNamedQuery(namedQuerys= {
		ClienteConsumidorConstant.FIND_BY_CLIENTE_KEY
	})
	public ClienteConsumidor(Long id) {
		this();
		this.setId(id);
	}
	
	public ClienteConsumidor(Long id,Long idUsuarioConsumidor, String nomeUsuarioConsumidor,String emailUsuarioConsumidor,
			Long idTelefoneConsumidor,Integer ddd, Integer ddi, Integer numero, SituacaoUsuario situacaoUsuario) {
		this();
		this.setId(id);
		this.setConsumidor(new Consumidor());
		this.consumidor.setUsuario(new Usuario(idUsuarioConsumidor, nomeUsuarioConsumidor, emailUsuarioConsumidor,idTelefoneConsumidor,ddd,ddi,numero, situacaoUsuario));
	}

	public ClienteConsumidor(Long id, Long idUsuario, String nome, String email, Long idTelefone, Integer ddi, Integer ddd, Integer numero, Operadora operadora) {
		this(id);
		this.setConsumidor(new Consumidor());
		this.getConsumidor().setUsuario(new Usuario());
		Usuario usuario = this.getConsumidor().getUsuario();
		usuario.setId(idUsuario);
		this.setNomeConsumidor(nome);
		usuario.setTelefone(new Telefone());
		usuario.getTelefone().setId(idTelefone);
		usuario.getTelefone().setDdi(ddi);
		usuario.getTelefone().setDdd(ddd);
		usuario.getTelefone().setNumero(numero);
	}

	
	public ClienteConsumidor(Long id, Long idCliente, Long idUsuarioCliente, String nomeUsuarioCliente,
			String emailUsuarioCliente, Long idConsumidor, Long idUsuarioConsumidor, String nomeUsuarioConsumidor,
			String emailUsuarioConsumidor) {
		this();
		this.setId(id);
		this.cliente = new Cliente();
		this.cliente.setId(idCliente);
		this.cliente.setUsuario(new Usuario(idUsuarioCliente, nomeUsuarioCliente, emailUsuarioCliente));
		this.setConsumidor(new Consumidor());
		this.consumidor.setId(idConsumidor);
		this.consumidor.setUsuario(new Usuario(idUsuarioConsumidor, nomeUsuarioConsumidor, emailUsuarioConsumidor));
	}

	@ReferencedNamedQuery(namedQuerys = { 
		ClienteConsumidorConstant.FIND_BY_NOME_CONSUMIDOR_KEY
	})
	public ClienteConsumidor(Long id, String nomeConsumidor) {
		setId(id);
		this.nomeConsumidor = nomeConsumidor;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Consumidor getConsumidor() {
		return consumidor;
	}

	public void setConsumidor(Consumidor consumidor) {
		this.consumidor = consumidor;
	}

	public Date getDataAssociacao() {
		return dataAssociacao;
	}

	public void setDataAssociacao(Date dataAssociacao) {
		this.dataAssociacao = dataAssociacao;
	}

	public SituacaoConsumidor getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoConsumidor situacao) {
		this.situacao = situacao;
	}
	
	public String getNomeConsumidor() {
		return nomeConsumidor;
	}

	public void setNomeConsumidor(String nomeConsumidor) {
		this.nomeConsumidor = nomeConsumidor;
	}



	public interface ClienteConsumidorConstant {
		String FIND_BY_ID_KEY = "ClienteConsumidor.findById";
		String FIELD_CONSUMIDOR = "consumidor";
		String FIELD_SITUACAO = "situacao";
		String FIELD_NOME_CONSUMIDOR = "nomeConsumidor";
		String FIND_BY_TELEFONE_KEY = "ClienteConsumidor.findByClienteAndConsumidorFotTel";
		String FIND_BY_TELEFONE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id, cl.id, ucl.id, ucl.nome, ucl.email, co.id, uco.id, uco.nome, uco.email) from ClienteConsumidor cc join cc.cliente cl join cl.usuario ucl join cc.consumidor co join co.usuario uco join uco.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero and cl = :cliente";
		String COUNT_USUARIO_KEY = "ClienteConsumidor.countUsuario";
		String COUNT_USUARIO_QUERY = "select count(cc) from ClienteConsumidor cc join cc.consumidor c join c.usuario u where cc.consumidor.usuario = :usuario";
		String FIND_BY_ID_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id,cc.consumidor.usuario.id,cc.consumidor.usuario.nome,cc.consumidor.usuario.email,cc.consumidor.usuario.telefone.id,cc.consumidor.usuario.telefone.ddd,cc.consumidor.usuario.telefone.ddi,cc.consumidor.usuario.telefone.numero,cc.consumidor.usuario.situacao ) from ClienteConsumidor cc where cc.cliente = :cliente and cc.id = :id";

		String LIST_CLIENTE_CONSUMIDOR_KEY = "ClienteConsumidor.findConsumidorByCliente";
		String LIST_CLIENTE_CONSUMIDOR_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id, u.id, cc.nomeConsumidor, u.email, t.id, t.ddi, t.ddd, t.numero, t.operadora) from ClienteConsumidor cc left join cc.consumidor cs join cs.usuario u join u.telefone t where cc.cliente = :cliente and cc.situacao = :situacao order by u.nome";
		
		String FIND_BY_NOME_KEY = "ClienteConsumidor.findByName";
		String FIND_BY_NOME_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id, u.id, cc.nomeConsumidor, u.email, t.id, t.ddi, t.ddd, t.numero, t.operadora) from ClienteConsumidor cc left join cc.consumidor cs join cs.usuario u join u.telefone t where cc.cliente = :cliente and cc.situacao = :situacao and upper(u.nome) like upper(concat('%', :nome, '%')) order by u.nome";
		
		String FIND_BY_NOME_CONSUMIDOR_KEY = "ClienteConsumidor.findByNameConsumidor";
		String FIND_BY_NOME_CONSUMIDOR_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id, cc.nomeConsumidor) from ClienteConsumidor cc where cc.cliente = :cliente and cc.situacao = :situacao and upper(cc.nomeConsumidor) like upper(concat('%', :nomeConsumidor, '%')) order by cc.nomeConsumidor";
		
		String LOAD_BY_CONSUMIDOR_KEY = "ClienteConsumidor.loadByConsumidor";
		String LOAD_BY_CONSUMIDOR_QUERY = "select cc from ClienteConsumidor cc left join cc.cliente c where c = :cliente and cc.consumidor = :consumidor";
	
		String FIND_BY_CLIENTE_KEY = "Consumidor.findClienteByConsumidor";
		String FIND_BY_CLIENTE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id) from ClienteConsumidor cc left join cc.cliente cliente where cliente = :cliente and cc = :clienteConsumidor";
	
	}
}
