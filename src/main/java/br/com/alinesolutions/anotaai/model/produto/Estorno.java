package br.com.alinesolutions.anotaai.model.produto;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@NamedQueries({

})

@DiscriminatorValue("ENTRADA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Estorno.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update EntradaMercadoria set ativo = false where id = ?")
@XmlRootElement
public class Estorno extends BaseEntity<Long, Estorno> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "estorno", cascade = { CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST })
	private List<ItemEstorno> itens;

	private Date dataEstorno;

	@ManyToOne(optional = false)
	private EntradaMercadoria entradaMercadoria;

	public List<ItemEstorno> getItens() {
		return itens;
	}

	public void setItens(List<ItemEstorno> itens) {
		this.itens = itens;
	}

	public Date getDataEstorno() {
		return dataEstorno;
	}

	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public EntradaMercadoria getEntradaMercadoria() {
		return entradaMercadoria;
	}

	public void setEntradaMercadoria(EntradaMercadoria entradaMercadoria) {
		this.entradaMercadoria = entradaMercadoria;
	}

}
