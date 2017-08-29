package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoItemMovimentacao;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.MotivoQuebra;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;

@DiscriminatorValue("QUEBRA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemQuebra.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemQuebra set ativo = false where id = ?")
@XmlRootElement
public class ItemQuebra extends BaseEntity<Long, ItemQuebra> implements IMovimentacao {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	public TipoItemMovimentacao getTipoItemMovimentacao() {
		return TipoItemMovimentacao.ITEM_QUEBRA;
	}
	
	@Override
	public void atualizarQuantidadeEstoque(Estoque estoque) {
		TipoMovimentacao.SAIDA.atualizarEstoque(estoque, this);
	}
	
	@ManyToOne(cascade = CascadeType.DETACH)
	private Quebra quebra;

	/**
	 * Atualiza o estoque removendo os itens desta movimentacao
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private MovimentacaoProduto movimentacaoProduto;

	/**
	 * Custo da mercadoria no momento em qeu foi descartada
	 */
	private Double precoCusto;

	@Enumerated(EnumType.ORDINAL)
	private MotivoQuebra motivoQuebra;

	public Quebra getQuebra() {
		return quebra;
	}

	public void setQuebra(Quebra quebra) {
		this.quebra = quebra;
	}

	public MovimentacaoProduto getMovimentacaoProduto() {
		return movimentacaoProduto;
	}

	public void setMovimentacaoProduto(MovimentacaoProduto movimentacaoProduto) {
		this.movimentacaoProduto = movimentacaoProduto;
	}

	public MotivoQuebra getMotivoQuebra() {
		return motivoQuebra;
	}

	public void setMotivoQuebra(MotivoQuebra motivoQuebra) {
		this.motivoQuebra = motivoQuebra;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

}
