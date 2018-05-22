package br.com.alinesolutions.anotaai.model.pagamento;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPagamento;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaAnonima;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = PagamentoAVista.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update PagamentoAVista set ativo = false where id = ?")
@XmlRootElement
public class PagamentoAVista extends BaseEntity<Long, PagamentoAVista> implements IPagamento {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(optional = false, cascade=CascadeType.ALL)
	private Pagamento pagamento;

	@Transient
	@Override
	public TipoPagamento getTipoPagamento() {
		return TipoPagamento.A_VISTA;
	}

	@NotNull
	@ManyToOne(cascade=CascadeType.DETACH)
	private VendaAVistaAnonima venda;

	public VendaAVistaAnonima getVenda() {
		return venda;
	}

	public void setVenda(VendaAVistaAnonima venda) {
		this.venda = venda;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

}
