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

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.SituacaoConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor.ClienteConsumidorConstant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ClienteConsumidor.class)
@NamedQueries({
		@NamedQuery(name = ClienteConsumidorConstant.FIND_BY_TELEFONE_KEY, query = ClienteConsumidorConstant.FIND_BY_TELEFONE_QUERY),
		@NamedQuery(name = ClienteConsumidorConstant.COUNT_USUARIO_KEY, query = ClienteConsumidorConstant.COUNT_USUARIO_QUERY) })
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

	private Date dataAssociacao;

	@Enumerated(EnumType.ORDINAL)
	private SituacaoConsumidor situacao;

	public ClienteConsumidor() {
		super();
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

	public interface ClienteConsumidorConstant {
		String FIELD_CLIENTE = "cliente";
		String FIELD_CONSUMIDOR = "consumidor";
		String FIND_BY_TELEFONE_KEY = "ClienteConsumidor.findByClienteAndConsumidorFotTel";
		String FIND_BY_TELEFONE_QUERY = "select new br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor(cc.id, cl.id, ucl.id, ucl.nome, ucl.email, co.id, uco.id, uco.nome, uco.email) from ClienteConsumidor cc join cc.cliente cl join cl.usuario ucl join cc.consumidor co join co.usuario uco join uco.telefone t where t.ddi = :ddi and t.ddd = :ddd and t.numero = :numero and cl = :cliente";
		String COUNT_USUARIO_KEY = "ClienteConsumidor.countUsuario";
		String COUNT_USUARIO_QUERY = "select count(cc) from ClienteConsumidor cc join cc.consumidor c join c.usuario u where cc.consumidor.usuario = :usuario";
	}
}
