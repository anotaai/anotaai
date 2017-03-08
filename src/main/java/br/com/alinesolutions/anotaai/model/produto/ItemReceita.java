package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ItemReceita set ativo = false where id = ?")
@XmlRootElement
public class ItemReceita extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@JsonBackReference(value="itensReceita")
	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto ingrediente;

	private Integer quantidade;

	public ItemReceita() {
		super();
	}

	public ItemReceita(Long id, Integer quantidade, Long idIngrediente, String descricaoIngrediente, Long idProduto,
			String descricaoProduto) {
		super();
		this.setId(id);
		this.quantidade = quantidade;
		this.ingrediente = new Produto();
		this.ingrediente.setId(idIngrediente);
		this.ingrediente.setDescricao(descricaoIngrediente);
		this.produto = new Produto();
		this.produto.setId(idProduto);
		this.produto.setDescricao(descricaoProduto);
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Produto ingrediente) {
		this.ingrediente = ingrediente;
	}

}
