package br.com.alinesolutions.anotaai.model.produto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.DiaSemana;

@NamedQueries({

})
@Entity
@XmlRootElement
public class Disponibilidade extends BaseEntity<Long, Disponibilidade> {

	private static final long serialVersionUID = 1L;

	public Disponibilidade() {
		super();
	}

	public Disponibilidade(Long id, DiaSemana dia, Long idProduto, String descricaoProduto) {
		super();
		setId(id);
		this.dia = dia;
		this.produto = new Produto();
		this.produto.setId(idProduto);
		this.produto.setDescricao(descricaoProduto);
	}

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private DiaSemana dia;

	@NotNull
	@JsonBackReference(value = "diasDisponibilidade")
	@ManyToOne(cascade = CascadeType.DETACH)
	private Produto produto;

	public DiaSemana getDia() {
		return dia;
	}

	public void setDia(DiaSemana dia) {
		this.dia = dia;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public interface DisponibilidadeConstant {

	}

}
