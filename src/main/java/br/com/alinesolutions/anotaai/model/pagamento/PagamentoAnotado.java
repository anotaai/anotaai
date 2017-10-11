package br.com.alinesolutions.anotaai.model.pagamento;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPagamento;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update PagamentoAnotado set ativo = false where id = ?")
@XmlRootElement
public class PagamentoAnotado extends BaseEntity<Long, PagamentoAnotado> implements IPagamento {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private Pagamento pagamento;

	@Transient
	@Override
	public TipoPagamento getTipoPagamento() {
		return TipoPagamento.ANOTADO;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	private FolhaCaderneta folhaCaderneta;

	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

}
