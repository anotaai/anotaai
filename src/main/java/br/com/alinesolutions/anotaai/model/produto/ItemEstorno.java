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

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoItemMovimentacao;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;

/**
 * Item utilizado para devolver itens de entrada equivocada
 * 
 * @author gleidsonmoura
 *
 */
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
	public TipoItemMovimentacao getTipoItemMovimentacao() {
		return TipoItemMovimentacao.ITEM_ESTORNO;
	}
	
	@Override
	public void atualizarQuantidadeEstoque(Estoque estoque) {
		TipoMovimentacao.SAIDA.atualizarEstoque(estoque, this);
	}

	@ManyToOne
	private Estorno estorno;

	/**
	 * Atualiza a quantidade de estoque removendo a quantidade de itens desta
	 * movimentacao
	 */
	@ManyToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private MovimentacaoProduto movimentacaoProduto;

	public ItemEstorno() {

	}

	public MovimentacaoProduto getMovimentacaoProduto() {
		return movimentacaoProduto;
	}

	public void setMovimentacaoProduto(MovimentacaoProduto movimentacaoProduto) {
		this.movimentacaoProduto = movimentacaoProduto;
	}

	public Estorno getEstorno() {
		return estorno;
	}

	public void setEstorno(Estorno estorno) {
		this.estorno = estorno;
	}

}
