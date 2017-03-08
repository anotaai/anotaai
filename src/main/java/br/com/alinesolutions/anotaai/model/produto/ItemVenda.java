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
import br.com.alinesolutions.anotaai.model.venda.Venda;

@DiscriminatorValue("VENDA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemVenda.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemVenda set ativo = false where id = ?")
@XmlRootElement
public class ItemVenda extends BaseEntity<Long> implements IMovimentacao {

	private static final long serialVersionUID = 1L;

	/**
	 * Custo da mercadoria no momento em qeu foi vendida
	 */
	private Double precoCusto;

	/**
	 * Preco de venda quando o produto foi vendido
	 */
	private Double precoVenda;

	@ManyToOne(cascade = CascadeType.DETACH)
	private Venda venda;

	/**
	 * Atualiza o estoque removendo os itens desta venda
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private MovimentacaoProduto movimentacao;

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public MovimentacaoProduto getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoProduto movimentacao) {
		this.movimentacao = movimentacao;
	}

}
