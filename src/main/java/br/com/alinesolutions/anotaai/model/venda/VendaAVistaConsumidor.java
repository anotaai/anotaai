package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = VendaAVistaConsumidor.class)
@NamedQueries({})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update VendaAVistaConsumidor set ativo = false where id = ?")
@XmlRootElement
public class VendaAVistaConsumidor extends BaseEntity<Long, VendaAVistaConsumidor> implements IVenda {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private Venda venda;

	@ManyToOne(optional = false)
	private Cliente cliente;

	@ManyToOne(optional = false)
	private FolhaCadernetaVenda folhaCaderneta;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public FolhaCadernetaVenda getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCadernetaVenda folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

}
