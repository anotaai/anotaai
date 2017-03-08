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
import br.com.alinesolutions.anotaai.model.domain.SituacaoItemBalanco;

/**
 * Item de um balanco, depois que o resultado for lancado nao serah mais
 * possivel excluir, terah que fazer um novo balanco para atualizar o valor
 * 
 * @author gleidson
 *
 */
@DiscriminatorValue("BALANCO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemBalanco.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemBalanco set ativo = false where id = ?")
@XmlRootElement
public class ItemBalanco extends BaseEntity<Long> implements IMovimentacao {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.DETACH)
	private Balanco balanco;

	/**
	 * Esta movientacao devera substituir o valor atual do estoque caso a
	 * contagem seja divergente da quantidade atual do estoque
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private MovimentacaoProduto movimentacao;

	/**
	 * Quantidade do estoque no inicio do balanco
	 */
	private Long quantidadeEstoque;

	/**
	 * Quantidade do estoque contada
	 */
	private Long quantidadeContada;

	private SituacaoItemBalanco situacao;

	public MovimentacaoProduto getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoProduto movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Balanco getBalanco() {
		return balanco;
	}

	public void setBalanco(Balanco balanco) {
		this.balanco = balanco;
	}

	public SituacaoItemBalanco getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoItemBalanco situacao) {
		this.situacao = situacao;
	}

	public Long getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Long quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Long getQuantidadeContada() {
		return quantidadeContada;
	}

	public void setQuantidadeContada(Long quantidadeContada) {
		this.quantidadeContada = quantidadeContada;
	}

}
