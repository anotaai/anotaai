package br.com.alinesolutions.anotaai.model.produto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Estoque.class)
@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Estoque set ativo = false where id = ?")
@XmlRootElement
public class Estoque extends BaseEntity<Long, Estoque> {

	private static final long serialVersionUID = 1L;

	@OneToOne(cascade=CascadeType.DETACH, orphanRemoval=false, mappedBy="estoque")
	private Produto produto;
	
	private Long quantidadeEstoque;
	private Double precoCusto;
	
	@OneToMany(mappedBy="estoque")
	private List<EstoqueMovimentacao> movimentacoes;

	public Long getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Long quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<EstoqueMovimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<EstoqueMovimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	
	

}
