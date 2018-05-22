package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.StatusMovimentacao;
import br.com.alinesolutions.anotaai.model.BaseEntity;

@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update MovimentacaoProduto set ativo = false where id = ?")
@XmlRootElement
public class MovimentacaoProduto extends BaseEntity<Long, MovimentacaoProduto> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(value = 1L)
	private Long quantidade;

	@NotNull
	@ManyToOne(cascade = {})
	private Produto produto;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private StatusMovimentacao statusMovimentacao;

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

	public StatusMovimentacao getStatusMovimentacao() {
		return statusMovimentacao;
	}

	public void setStatusMovimentacao(StatusMovimentacao statusMovimentacao) {
		this.statusMovimentacao = statusMovimentacao;
	}

}
