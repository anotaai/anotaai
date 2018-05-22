package br.com.alinesolutions.anotaai.model.produto;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Devolucao.class)
@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Devolucao set ativo = false where id = ?")
@XmlRootElement
public class Devolucao extends BaseEntity<Long, Devolucao> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(cascade = CascadeType.DETACH)
	private Cliente cliente;

	private ZonedDateTime data;

	@OneToMany(mappedBy = "devolucao", cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	private List<ItemDevolucao> produtos;

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ZonedDateTime getData() {
		return data;
	}

	public void setData(ZonedDateTime data) {
		this.data = data;
	}

	public List<ItemDevolucao> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ItemDevolucao> produtos) {
		this.produtos = produtos;
	}

}
