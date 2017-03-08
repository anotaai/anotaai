package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoAtualizacaoEstoque;
import br.com.alinesolutions.anotaai.model.domain.TipoMovimentacao;

@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update MovimentacaoProduto set ativo = false where id = ?")
@XmlRootElement
public class MovimentacaoProduto extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.ORDINAL)
	private TipoMovimentacao tipoMovimentacao;
	
	@Enumerated(EnumType.ORDINAL)
	private TipoAtualizacaoEstoque tipoAtualizacao;
	
	private Long quantidade;
	
	@ManyToOne(cascade=CascadeType.DETACH)
	private Produto produto;

	public TipoMovimentacao getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
