package br.com.alinesolutions.anotaai.model.produto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;

@NamedQueries({ @NamedQuery(name = EstoqueConstant.FIND_BY_PRODUTO_KEY, query = EstoqueConstant.FIND_BY_PRODUTO_QUERY) })
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Estoque set ativo = false where id = ?")
@XmlRootElement
public class Estoque extends BaseEntity<Long, Estoque> {

	private static final long serialVersionUID = 1L;

	public Estoque(Long id, Double precoCusto, Long quantidadeEstoque) {
		setId(id);
		this.precoCusto = precoCusto;
		this.quantidadeEstoque = quantidadeEstoque;
	}

	@NotNull
	@OneToOne(cascade = CascadeType.DETACH, orphanRemoval = false, mappedBy = "estoque")
	private Produto produto;

	@NotNull
	private Long quantidadeEstoque;

	private Double precoCusto;

	@OneToMany(mappedBy = "estoque", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<EstoqueMovimentacao> movimentacoes;

	public Long getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Long quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<EstoqueMovimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<EstoqueMovimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public Estoque() {

	}

	public interface EstoqueConstant {
		String FIND_BY_PRODUTO_KEY = "Estoque.findByProduto";
		String FIND_BY_PRODUTO_QUERY = "select e from Estoque e where e.produto.id = :id";
	}

}
