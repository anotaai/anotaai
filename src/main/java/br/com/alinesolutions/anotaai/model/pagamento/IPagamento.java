package br.com.alinesolutions.anotaai.model.pagamento;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPagamento;
import br.com.alinesolutions.anotaai.model.domain.MeioPagamento;

/**
 * As vendas podem ser a vista ou anotadas, esta interface define que todo tido
 * de venda referese a uma venda
 * 
 * @author gleidson
 *
 */
@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = PagamentoAnonimo.class, name = "ANONIMO"),
	@Type(value = PagamentoConsumidor.class, name = "FOLHA_CADERNETA")
})
public interface IPagamento {

	TipoPagamento getTipoPagamento();
	
	Date getDataPagamento();
	
	void setDataPagamento(Date dataPagamento);

	Double getValorRecebido();

	void setValorRecebido(Double valorRecebido);
	
	Double getValorPagamento();

	void setValorPagamento(Double valorPagamento);

	MeioPagamento getMeioPagamento();
	
	void setMeioPagamento(MeioPagamento meioPagamento);
	
	
}
