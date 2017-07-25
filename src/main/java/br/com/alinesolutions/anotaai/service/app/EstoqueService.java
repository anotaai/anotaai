package br.com.alinesolutions.anotaai.service.app;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.domain.TipoAtualizacaoEstoque;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.util.Constant;

/**
 * Session Bean implementation class EstoqueService
 */
@Stateless
public class EstoqueService {
	

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@Asynchronous
	public void send(@Observes List<IMovimentacao> itensMovimentacao) {
		try {
			itensMovimentacao.stream().forEach(item -> {
				inserirEstoque(item);
			});
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void inserirEstoque(IMovimentacao itemEntrada)  throws AppException  {
		TypedQuery<Estoque> estoqueQuery = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
		estoqueQuery.setParameter("id", itemEntrada.getMovimentacaoProduto().getProduto().getId());
		Estoque estoque = estoqueQuery.getSingleResult();		
		itemEntrada.getMovimentacaoProduto().setTipoMovimentacao(TipoMovimentacao.ENTRADA);
		TipoAtualizacaoEstoque.ACRESCENTA.atualizarEstoque(estoque, itemEntrada.getMovimentacaoProduto());
	}
	
	
	
	
}
