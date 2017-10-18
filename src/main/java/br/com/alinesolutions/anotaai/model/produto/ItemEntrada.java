package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoItemMovimentacao;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada.ItemEntradaConstant;

@Entity
@NamedQueries({
	@NamedQuery(name = ItemEntradaConstant.ITEM_ENTRADA_BY_PRODUTO_KEY, query = ItemEntradaConstant.ITEM_ENTRADA_BY_PRODUTO_QUERY)
})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemEntrada set ativo = false where id = ?")
public class ItemEntrada extends BaseEntity<Long, ItemEntrada> implements IMovimentacao {

	private static final long serialVersionUID = 1L;
	
	public ItemEntrada() {
		super();
	}

	@Override
	@Transient
	public TipoItemMovimentacao getTipoItemMovimentacao() {
		return TipoItemMovimentacao.ITEM_ENTRADA;
	}
	
	@Override
	public void atualizarQuantidadeEstoque(Estoque estoque) {
		TipoMovimentacao.ENTRADA.atualizarEstoque(estoque, this);
	}
	
	@ManyToOne
	private EntradaMercadoria entradaMercadoria;

	/**
	 * Atualiza a quantidade de estoque somando a quantidade de itens desta
	 * movimentacao
	 */
	@ManyToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private MovimentacaoProduto movimentacaoProduto;
	
	@Transient
	private Boolean estornar;

	public ItemEntrada(Double precoCusto) {
		setPrecoCusto(precoCusto);
	}

	public ItemEntrada(Long id, Double precoCusto, Long idMovimentacao, Long quantidade, Long idProduto, String descricaoProduto) {
		setId(id);
		this.precoCusto = precoCusto;
		MovimentacaoProduto movimentacaoProduto = new MovimentacaoProduto();
		Produto produto = new Produto();
		produto.setId(idProduto);
		produto.setDescricao(descricaoProduto);
		movimentacaoProduto.setId(idMovimentacao);
		movimentacaoProduto.setProduto(produto);
		movimentacaoProduto.setQuantidade(quantidade);
		setMovimentacaoProduto(movimentacaoProduto);
	}

	/**
	 * preco que a mercadoria foi comprada
	 */
	private Double precoCusto;

	public MovimentacaoProduto getMovimentacaoProduto() {
		return movimentacaoProduto;
	}

	public void setMovimentacaoProduto(MovimentacaoProduto movimentacaoProduto) {
		this.movimentacaoProduto = movimentacaoProduto;
	}

	public EntradaMercadoria getEntradaMercadoria() {
		return entradaMercadoria;
	}

	public void setEntradaMercadoria(EntradaMercadoria entradaMercadoria) {
		this.entradaMercadoria = entradaMercadoria;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}
	
	public Boolean getEstornar() {
		return estornar;
	}
	
	public void setEstornar(Boolean estornar) {
		this.estornar = estornar;
	}
	
	public interface ItemEntradaConstant {
		String ITEM_ENTRADA_BY_PRODUTO_KEY = "ItemEntrada.findByProduto";
		String ITEM_ENTRADA_BY_PRODUTO_QUERY = "select ie from ItemEntrada ie join ie.movimentacaoProduto m join m.produto p where p = :produto";
	}

}
