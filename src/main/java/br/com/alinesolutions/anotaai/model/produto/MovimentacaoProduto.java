package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update MovimentacaoProduto set ativo = false where id = ?")
@XmlRootElement
public class MovimentacaoProduto extends BaseEntity<Long, MovimentacaoProduto> {

	private static final long serialVersionUID = 1L;

	private Long quantidade;

	@ManyToOne(cascade = {})
	private Produto produto;

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
