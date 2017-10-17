package br.com.alinesolutions.anotaai.model.venda;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

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
public interface IVendaCaderneta extends ITipoVenda {
	
	CadernetaVenda getCadernetaVenda();
	
	void setCadernetaVenda(CadernetaVenda caderneta);

}
