package br.com.alinesolutions.anotaai.model.produto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@Type(value = ItemBalanco.class, name="balanco"),
	@Type(value = ItemEntrada.class, name="entrada"),
	@Type(value = ItemQuebra.class, name="quebra"),
	@Type(value = ItemVenda.class, name="venda"),
	@Type(value = ItemDevolucao.class, name="devolucao")
})
public interface IMovimentacao {
	
	MovimentacaoProduto getMovimentacao();

	void setMovimentacao(MovimentacaoProduto movimentacao);

}
