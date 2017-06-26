package br.com.alinesolutions.anotaai.model.produto;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@DiscriminatorValue("ENTRADA")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = EntradaMercadoria.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update EntradaMercadoria set ativo = false where id = ?")
@XmlRootElement
public class EntradaMercadoria extends BaseEntity<Long, EntradaMercadoria> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "entradaMercadoria", cascade = { CascadeType.REMOVE, CascadeType.MERGE })
	private List<ItemEntrada> itens;

	private Date dataEntrada;

	public List<ItemEntrada> getItens() {
		return itens;
	}

	public void setItens(List<ItemEntrada> itens) {
		this.itens = itens;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

}
