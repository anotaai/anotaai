package br.com.alinesolutions.anotaai.model.produto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoItemMovimentacao;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@Type(value = ItemBalanco.class, name="ITEM_BALANCO"),
	@Type(value = ItemEntrada.class, name="ITEM_ENTRADA"),
	@Type(value = ItemQuebra.class, name="ITEM_QUEBRA"),
	@Type(value = ItemVenda.class, name="ITEM_VENDA"),
	@Type(value = ItemDevolucao.class, name="ITEM_DEVOLUCAO"),
	@Type(value = ItemEstorno.class, name="ITEM_ESTORNO")
})
public interface IMovimentacao {
	
	MovimentacaoProduto getMovimentacaoProduto();

	void setMovimentacaoProduto(MovimentacaoProduto movimentacao);
	
	void atualizarQuantidadeEstoque(Estoque estoque);

	TipoItemMovimentacao getTipoItemMovimentacao();
	
}
