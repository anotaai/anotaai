package br.com.alinesolutions.anotaai.service.app;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoAtualizacaoEstoque;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
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
	public void sendItemEntrada(@Observes ItemEntrada itensEntrada) {
		atualizar(itensEntrada);
	}

	public void atualizar(IMovimentacao itemEntrada)  throws AppException  {
		TypedQuery<Estoque> estoqueQuery = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
		final Produto produto = itemEntrada.getMovimentacaoProduto().getProduto();
		estoqueQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produto.getId());
		Estoque estoque = estoqueQuery.getSingleResult();		
		itemEntrada.getMovimentacaoProduto().getTipoAtualizacao().atualizarEstoque(estoque, itemEntrada.getMovimentacaoProduto());
	}
		
}
