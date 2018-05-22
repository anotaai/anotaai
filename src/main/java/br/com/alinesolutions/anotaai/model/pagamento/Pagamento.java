package br.com.alinesolutions.anotaai.model.pagamento;


import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.MeioPagamento;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Pagamento set ativo = false where id = ?")
public class Pagamento extends BaseEntity<Long, Pagamento> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private ZonedDateTime dataPagamento;

	@NotNull
	private Double valorRecebido;

	@NotNull
	private Double valorPagamento;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private MeioPagamento meioPagamento;

	public ZonedDateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(ZonedDateTime dataPagamento) {
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

}
