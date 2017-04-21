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

	@ManyToOne(cascade = CascadeType.ALL)
	private Entrada entrada;

	/**
	 * Atualiza a quantidade de estoque somando a quantidade de itens desta movimentacao
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private MovimentacaoProduto movimentacao;

	/**
	 * preco que a mercadoria foi comprada
	 */
	private Double precoCusto;

	public MovimentacaoProduto getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoProduto movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

}
