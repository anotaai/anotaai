package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.pagamento.IPagamento;
import br.com.alinesolutions.anotaai.model.pagamento.PagamentoAVista;
import br.com.alinesolutions.anotaai.model.pagamento.PagamentoAnotado;

@Entity
@NamedQueries({
	
})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update PagamentoVenda set ativo = false where id = ?")
public class PagamentoVenda extends BaseEntity<Long, PagamentoVenda> {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne
	private Venda venda;
	
	@NotNull
	@Any(metaColumn = @Column(name = "tipo_pagamento", length = 16), fetch = FetchType.LAZY)
	@AnyMetaDef(
		idType = "long", metaType = "string",
		metaValues = {
			@MetaValue(targetEntity = PagamentoAVista.class, value = "AVISTA"),
			@MetaValue(targetEntity = PagamentoAnotado.class, value = "ANOTADO")
		}
	)
	@JoinColumn(name = "pagamento_id")
	private IPagamento pagamento;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public IPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(IPagamento pagamento) {
		this.pagamento = pagamento;
	}

}
