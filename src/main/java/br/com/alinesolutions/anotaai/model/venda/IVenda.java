package br.com.alinesolutions.anotaai.model.venda;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;

/**
 * As vendas podem ser a vista ou anotadas, esta interface define que todo tido
 * de venda referese a uma venda
 * 
 * @author gleidson
 *
 */
@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = VendaAnotada.class, name = "vendaAnotada"),
	@Type(value = VendaAVistaAnonima.class, name = "vendaAVistaAnonima"), 
	@Type(value = VendaAVistaConsumidor.class, name = "vendaAVistaConsumidor") 
})
public interface IVenda {

	Venda getVenda();

	void setVenda(Venda venda);
	
	TipoVenda getTipoVenda();

}
