package br.com.alinesolutions.anotaai.model.pagamento;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPagamento;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.MeioPagamento;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = PagamentoConsumidor.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update PagamentoConsumidor set ativo = false where id = ?")
@XmlRootElement
public class PagamentoConsumidor extends BaseEntity<Long, PagamentoConsumidor> implements IPagamento {

	private static final long serialVersionUID = 1L;

	@Transient
	@Override
	public TipoPagamento getTipoPagamento() {
		return TipoPagamento.CONSUMIDOR;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;

	private Double valorRecebido;

	private Double valorPagamento;

	private MeioPagamento meioPagamento;

	@ManyToOne(cascade = CascadeType.DETACH)
	private FolhaCaderneta folhaCaderneta;

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Double getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(Double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

}
