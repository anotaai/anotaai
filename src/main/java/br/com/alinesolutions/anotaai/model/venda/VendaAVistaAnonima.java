package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = VendaAVistaAnonima.class)
@NamedQueries({})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update VendaAVistaConsumidor set ativo = false where id = ?")
@XmlRootElement
public class VendaAVistaAnonima extends BaseEntity<Long, VendaAVistaAnonima> implements IVenda {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private Venda venda;

	@ManyToOne(optional = false)
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY)
	private ConsumidorVenda consumidor;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ConsumidorVenda getConsumidor() {
		return consumidor;
	}

	public void setConsumidor(ConsumidorVenda consumidor) {
		this.consumidor = consumidor;
	}

	@Override
	public TipoVenda getTipoVenda() {
		return TipoVenda.A_VISTA_ANONIMA;
	}

}
