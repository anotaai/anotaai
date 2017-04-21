package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@Entity
@NamedQueries({

})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ClienteVenda set ativo = false where id = ?")
public class ClienteVenda extends BaseEntity<Long, ClienteVenda> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Cliente cliente;

	@ManyToOne
	private Venda venda;

	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
