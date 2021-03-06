package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = VendaAnotadaConsumidor.class)
@NamedQueries({})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update VendaAnotadaConsumidor set ativo = false where id = ?")
@XmlRootElement
public class VendaAnotadaConsumidor extends BaseEntity<Long, VendaAnotadaConsumidor> implements IVendaFolha {

	private static final long serialVersionUID = 1L;
	
	@Override
	@Transient
	public TipoVenda getTipoVenda() {
		return TipoVenda.ANOTADA_CONSUMIDOR;
	}

	@NotNull
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	private FolhaCadernetaVenda folhaCadernetaVenda;

	@Override
	public FolhaCadernetaVenda getFolhaCadernetaVenda() {
		return folhaCadernetaVenda;
	}

	@Override
	public void setFolhaCadernetaVenda(FolhaCadernetaVenda folhaCadernetaVenda) {
		this.folhaCadernetaVenda = folhaCadernetaVenda;
	}


}
