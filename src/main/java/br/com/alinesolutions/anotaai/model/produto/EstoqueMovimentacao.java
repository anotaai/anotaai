package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

/**
 * Classe para agrupar todas as movimentacoes de um produto
 * @author gleidson
 *
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = EstoqueMovimentacao.class)
@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update EstoqueMovimentacao set ativo = false where id = ?")
@XmlRootElement
public class EstoqueMovimentacao extends BaseEntity<Long, EstoqueMovimentacao> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private Estoque estoque;

	@Any(metaColumn = @Column(name = "tipo_movimentacao", length = 16), fetch = FetchType.LAZY)
	@AnyMetaDef(
		idType = "long", metaType = "string", 
		metaValues = { 
			@MetaValue(targetEntity = ItemBalanco.class, value = "BALANCO"), 
			@MetaValue(targetEntity = ItemEntrada.class, value = "ENTRADA"),
			@MetaValue(targetEntity = ItemVenda.class, value = "VENDA"),
			@MetaValue(targetEntity = ItemQuebra.class, value = "QUEBRA"),
			@MetaValue(targetEntity = ItemDevolucao.class, value = "DEVOLUCAO"),
			@MetaValue(targetEntity = ItemEstorno.class, value = "ESTORNO")
		}
	)
	@JoinColumn(name="movimentacao_id")
	private IMovimentacao movimentacao;

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public IMovimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(IMovimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}
}
