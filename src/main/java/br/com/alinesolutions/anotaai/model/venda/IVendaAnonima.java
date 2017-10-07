package br.com.alinesolutions.anotaai.model.venda;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.model.pagamento.PagamentoAnonimo;

/**
 * As vendas podem ser a vista ou anotadas, esta interface define que todo tido
 * de venda referese a uma venda
 * 
 * @author gleidson
 *
 */
@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = VendaAVistaAnonima.class, name = "A_VISTA_ANONIMA")
})
public interface IVendaAnonima extends IVenda {
	
	Caderneta getCaderneta();
	
	void setCaderneta(Caderneta caderneta);
	
	List<PagamentoAnonimo> getPagamentos();
	
	void setPagamentos(List<PagamentoAnonimo> pagamentos);

}
