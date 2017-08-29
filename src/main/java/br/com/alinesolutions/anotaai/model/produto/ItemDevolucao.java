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
import br.com.alinesolutions.anotaai.model.domain.MotivoDevolucao;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;

/**
 * Mercadorias que foram compradas e posteriormente devolvidas pelos consumidores
 * @author Gleidson
 *
 */
@DiscriminatorValue("ENTRADA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = ItemDevolucao.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemDevolucao set ativo = false where id = ?")
@XmlRootElement
public class ItemDevolucao extends BaseEntity<Long, ItemDevolucao> implements IMovimentacao {

	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	public TipoItemMovimentacao getTipoItemMovimentacao() {
		return TipoItemMovimentacao.ITEM_DEVOLUCAO;
	}
	
	@Override
	public void atualizarQuantidadeEstoque(Estoque estoque) {
		TipoMovimentacao.ENTRADA.atualizarEstoque(estoque, this);
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Devolucao devolucao;

	/**
	 * Atualiza a quantidade de estoque somando a quantidade de itens desta
	 * movimentacao
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private MovimentacaoProduto movimentacaoProduto;

	/**
	 * preco que a mercadoria foi comprada
	 */
	private Double precoCusto;

	@Enumerated(EnumType.ORDINAL)
	private MotivoDevolucao motivo;

	private String descricao;

	public MovimentacaoProduto getMovimentacaoProduto() {
		return movimentacaoProduto;
	}

	public void setMovimentacaoProduto(MovimentacaoProduto movimentacaoProduto) {
		this.movimentacaoProduto = movimentacaoProduto;
	}

	public Devolucao getDevolucao() {
		return devolucao;
	}

	public void setDevolucao(Devolucao devolucao) {
		this.devolucao = devolucao;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public MotivoDevolucao getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoDevolucao motivo) {
		this.motivo = motivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
