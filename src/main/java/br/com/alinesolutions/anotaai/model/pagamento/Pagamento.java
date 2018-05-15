package br.com.alinesolutions.anotaai.model.pagamento;


import java.time.LocalDateTime;

import javax.persistence.Entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.MeioPagamento;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Pagamento set ativo = false where id = ?")
public class Pagamento extends BaseEntity<Long, Pagamento> {

	private static final long serialVersionUID = 1L;

	private LocalDateTime dataPagamento;

	private Double valorRecebido;

	private Double valorPagamento;

	private MeioPagamento meioPagamento;

	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDateTime dataPagamento) {
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
