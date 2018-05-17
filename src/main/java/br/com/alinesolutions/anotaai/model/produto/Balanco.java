package br.com.alinesolutions.anotaai.model.produto;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoBalanco;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Balanco.class)
@NamedQueries({

})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Balanco set ativo = false where id = ?")
@XmlRootElement
public class Balanco extends BaseEntity<Long, Balanco> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.DETACH)
	private Cliente cliente;

	private ZonedDateTime data;

	@Enumerated(EnumType.ORDINAL)
	private TipoBalanco tipo;

	private String motivo;

	@OneToMany(mappedBy = "balanco", cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	private List<ItemBalanco> produtos;

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

	public TipoBalanco getTipo() {
		return tipo;
	}

	public void setTipo(TipoBalanco tipo) {
		this.tipo = tipo;
	}

	public List<ItemBalanco> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ItemBalanco> produtos) {
		this.produtos = produtos;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
