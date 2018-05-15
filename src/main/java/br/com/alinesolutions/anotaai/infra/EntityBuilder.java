package br.com.alinesolutions.anotaai.infra;

import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemBalanco;
import br.com.alinesolutions.anotaai.model.produto.ItemDevolucao;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;
import br.com.alinesolutions.anotaai.model.produto.ItemEstorno;
import br.com.alinesolutions.anotaai.model.produto.ItemQuebra;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;

public class EntityBuilder {

	private static EntityBuilder instance;

	static {
		instance = new EntityBuilder();
	}
	
	private EntityBuilder() {
		super();
	}
	
	public static EntityBuilder getInstance() {
		return instance;
	}
	
	public IMovimentacao cloneMovimentacao(IMovimentacao movimentacao) {
		IMovimentacao iMovimentacao = null;
		switch (movimentacao.getTipoItemMovimentacao()) {
			case ITEM_BALANCO:
				iMovimentacao = new ItemBalanco();
				break;
			case ITEM_DEVOLUCAO:
				iMovimentacao = new ItemDevolucao();
				break;
			case ITEM_ENTRADA:
				iMovimentacao = new ItemEntrada();
				break;
			case ITEM_ESTORNO:
				iMovimentacao = new ItemEstorno();
				break;
			case ITEM_QUEBRA:
				iMovimentacao = new ItemQuebra();
				break;
			case ITEM_VENDA:
				ItemVenda itemVenda = new ItemVenda();
				itemVenda.setVenda(((ItemVenda) movimentacao).getVenda());
				iMovimentacao = itemVenda;
				break;
		}
		return iMovimentacao;
	}

}
