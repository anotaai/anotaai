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

@DiscriminatorValue("ENTRADA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemEntrada.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemEntrada set ativo = false where id = ?")
@XmlRootElement
public class ItemEntrada extends BaseEntity<Long, ItemEntrada> implements IMovimentacao {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private EntradaMercadoria entradaMercadoria;

	/**
	 * Atualiza a quantidade de estoque somando a quantidade de itens desta
	 * movimentacao
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	private MovimentacaoProduto movimentacaoProduto;

	public ItemEntrada() {

	}

	public ItemEntrada(Double precoCusto) {
		setPrecoCusto(precoCusto);
	}

	public ItemEntrada(Long id, Double precoCusto,Long idMovimentacao, Long quantidade , Long idProduto, String descricaoProduto) {
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
