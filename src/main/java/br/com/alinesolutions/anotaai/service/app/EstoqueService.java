package br.com.alinesolutions.anotaai.service.app;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.EstoqueMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.util.Constant;

/**
 * Session Bean implementation class EstoqueService
 */
@Stateless
public class EstoqueService {
	
	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	
	@Asynchronous
	public void sendItemMovimentacao(@Observes IMovimentacao itemMovimentacao) {
		Estoque estoque = atualizarEstoque(itemMovimentacao);
		switch (itemMovimentacao.getTipoItemMovimentacao()) {
			case ITEM_BALANCO:
				
				break;
			case ITEM_DEVOLUCAO:

				break;
			case ITEM_ENTRADA:
				estoque.setPrecoCusto(((ItemEntrada)itemMovimentacao).getPrecoCusto());
				break;
			case ITEM_ESTORNO:
				
				break;
			case ITEM_QUEBRA:
				
				break;
			case ITEM_VENDA:
				break;

			default:
				break;
		}
		
		EstoqueMovimentacao estoqueMovimentacao = new EstoqueMovimentacao();
		estoqueMovimentacao.setEstoque(estoque);
		estoqueMovimentacao.setMovimentacao(itemMovimentacao);
		em.persist(estoqueMovimentacao);
		
	}

	public Estoque atualizarEstoque(IMovimentacao itemMovimentacao)  throws AppException  {
		TypedQuery<Estoque> estoqueQuery = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
		final Produto produto = itemMovimentacao.getMovimentacaoProduto().getProduto();
		estoqueQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produto.getId());
		Estoque estoque = estoqueQuery.getSingleResult();		
		itemMovimentacao.atualizarQuantidadeEstoque(estoque);
		return estoque;
	}

}
