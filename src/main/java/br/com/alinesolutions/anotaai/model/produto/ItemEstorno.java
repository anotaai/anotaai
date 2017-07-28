package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;

@DiscriminatorValue("ESTORNO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemEstorno.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemEstorno set ativo = false where id = ?")
@XmlRootElement
public class ItemEstorno extends BaseEntity<Long, ItemEstorno> implements IMovimentacao {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void atualizarQuantidadeEstoque(Estoque estoque) {
		TipoMovimentacao.SAIDA.atualizarEstoque(estoque, this);
	}

	@ManyToOne
	private EntradaMercadoria entradaMercadoria;

	/**
	 * Atualiza a quantidade de estoque removendo a quantidade de itens desta
	 * movimentacao
	 */
	@ManyToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private MovimentacaoProduto movimentacaoProduto;

	public ItemEstorno() {

	}

	public ItemEstorno(Double precoCusto) {
		setPrecoCusto(precoCusto);
	}

	public ItemEstorno(Long id, Double precoCusto, Long idMovimentacao, Long quantidade, Long idProduto, String descricaoProduto) {
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

}
