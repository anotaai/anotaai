package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.metadata.model.domain.LocalVenda;

/**
 * As vendas podem ser a vista ou anotadas, esta interface define que todo tido
 * de venda referese a uma venda
 * 
 * @author gleidson
 *
 */
@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	@Type(value = CadernetaVenda.class, name = "CADERNETA"),
	@Type(value = FolhaCadernetaVenda.class, name = "FOLHA_CADERNETA")
})
public interface IVenda {
	
	Venda getVenda();
	
	void setVenda(Venda venda);
	
	@Transient
	LocalVenda getLocalVenda();

}
