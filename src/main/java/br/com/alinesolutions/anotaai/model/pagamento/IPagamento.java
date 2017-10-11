package br.com.alinesolutions.anotaai.model.pagamento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPagamento;

/**
 * As vendas podem ser a vista ou anotadas, esta interface define que todo tido
 * de venda referese a uma venda
 * 
 * @author gleidson
 *
 */
@JsonSubTypes({ 
	@Type(value = PagamentoAVista.class, name = "AVISTA"),
	@Type(value = PagamentoAnotado.class, name = "ANOTADO")
})
public interface IPagamento {
	
	TipoPagamento getTipoPagamento();

	Pagamento getPagamento();
	
	void setPagamento(Pagamento pagamento);
	
}
