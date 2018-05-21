package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.EntityBuilder;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;
import br.com.alinesolutions.anotaai.model.produto.EstoqueMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.produto.MovimentacaoProduto;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.venda.Venda;

/**
 * Session Bean implementation class EstoqueService
 */
@Stateless
@AccessTimeout(value = 60, unit = TimeUnit.SECONDS)
public class EstoqueService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	public Estoque recuperarEstoque(Produto produto) {
		TypedQuery<Estoque> estoqueQuery = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
		estoqueQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produto.getId());
		return estoqueQuery.getSingleResult();
	}

	@Asynchronous
	public void atualizarEstoque(@Observes IMovimentacao itemMovimentacao) {
		final List<EstoqueMovimentacao> estoquesMovimentacao = new ArrayList<>();
		final List<IMovimentacao> itensMovimentacaoComReceita = new ArrayList<>();
		atualizarEstoqueProdutoSemReceita(itemMovimentacao, estoquesMovimentacao, itensMovimentacaoComReceita);
		itensMovimentacaoComReceita.forEach(itemMovimentacaoComReceita -> {
			itemMovimentacaoComReceita.getMovimentacaoProduto().setQuantidade(calcularQuantidadeProdutoComReceita(itemMovimentacao.getMovimentacaoProduto().getProduto()));
			atualizar(itemMovimentacaoComReceita);
		});
	}

	private Long calcularQuantidadeProdutoComReceita(Produto produto) {
		return 0l;
	}

	/**
	 * Atualiza o estoque dos itens que nao possuem itens de receita
	 * @param itemMovimentacao
	 * @param estoquesMovimentados
	 */
	private void atualizarEstoqueProdutoSemReceita(IMovimentacao itemMovimentacao, List<EstoqueMovimentacao> estoquesSemReceita, List<IMovimentacao> itensMovimentacaoComReceita) {
		Produto produto = em.find(Produto.class, itemMovimentacao.getMovimentacaoProduto().getProduto().getId());
		if (produto.getItensReceita() != null && !produto.getItensReceita().isEmpty()) {
			produto.getItensReceita().forEach(itemReceita -> {
				IMovimentacao novoItemMovimentacao = EntityBuilder.getInstance().cloneMovimentacao(itemMovimentacao);
				MovimentacaoProduto movimentacao = new MovimentacaoProduto();
				movimentacao.setProduto(itemReceita.getIngrediente());
				movimentacao.setQuantidade(itemReceita.getQuantidade().longValue() * itemMovimentacao.getMovimentacaoProduto().getQuantidade());
				novoItemMovimentacao.setMovimentacaoProduto(movimentacao);
				itensMovimentacaoComReceita.add(criarMovimentacaoProdutoComReceita(itemMovimentacao, produto));
				atualizarEstoqueProdutoSemReceita(novoItemMovimentacao, estoquesSemReceita, itensMovimentacaoComReceita);
			});
		} else {
			EstoqueMovimentacao estoqueMovimentacao = atualizar(itemMovimentacao);
			estoquesSemReceita.add(estoqueMovimentacao);
		}
	}

	private IMovimentacao criarMovimentacaoProdutoComReceita(IMovimentacao itemMovimentacao, Produto produto) {
		IMovimentacao novoItemMovimentacao = EntityBuilder.getInstance().cloneMovimentacao(itemMovimentacao);
		MovimentacaoProduto movimentacao = new MovimentacaoProduto();
		movimentacao.setProduto(produto);
		novoItemMovimentacao.setMovimentacaoProduto(movimentacao);
		return novoItemMovimentacao;
	}

	private EstoqueMovimentacao atualizar(IMovimentacao itemMovimentacao) {
		Estoque estoque = recuperarEstoque(itemMovimentacao.getMovimentacaoProduto().getProduto());
		EstoqueMovimentacao estoqueMovimentacao = new EstoqueMovimentacao();
		estoqueMovimentacao.setEstoque(estoque);
		estoqueMovimentacao.setMovimentacao(itemMovimentacao);
		estoque.getMovimentacoes().add(estoqueMovimentacao);
		itemMovimentacao.atualizarQuantidadeEstoque(estoque);
		atualizarItemMovimentacao(estoqueMovimentacao);
		em.persist(itemMovimentacao);
		em.persist(estoqueMovimentacao);
		return estoqueMovimentacao;
	}
	
	private void atualizarItemMovimentacao(EstoqueMovimentacao estoqueMovimentacao) {
		switch (estoqueMovimentacao.getMovimentacao().getTipoItemMovimentacao()) {
		case ITEM_BALANCO:

			break;
		case ITEM_DEVOLUCAO:

			break;
		case ITEM_ENTRADA:
			estoqueMovimentacao.getEstoque().setPrecoCusto(((ItemEntrada) estoqueMovimentacao.getMovimentacao()).getPrecoCusto());
			break;
		case ITEM_ESTORNO:

			break;
		case ITEM_QUEBRA:

			break;
		case ITEM_VENDA:			
			ItemVenda itemVenda = (ItemVenda) estoqueMovimentacao.getMovimentacao();
			itemVenda.setVenda(em.find(Venda.class, itemVenda.getVenda().getId()));
			itemVenda.setPrecoCusto(estoqueMovimentacao.getEstoque().getPrecoCusto());
			itemVenda.setPrecoVenda(estoqueMovimentacao.getEstoque().getProduto().getPrecoVenda());
			break;
		default:
			break;
		}
	}

}
