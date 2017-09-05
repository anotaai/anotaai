package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.pagamento.PagamentoConsumidor;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = VendaAVistaConsumidor.class)
@NamedQueries({})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update VendaAVistaConsumidor set ativo = false where id = ?")
@XmlRootElement
public class VendaAVistaConsumidor extends BaseEntity<Long, VendaAVistaConsumidor> implements IVendaConsumidor {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	public TipoVenda getTipoVenda() {
		return TipoVenda.A_VISTA_CONSUMIDOR;
	}

	@ManyToOne(optional = false)
	private Venda venda;

	@ManyToOne(optional = false)
	private FolhaCaderneta folhaCaderneta;

	@ManyToOne(optional = false)
	private PagamentoConsumidor pagamento;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	@Override
	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	@Override
	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

}
