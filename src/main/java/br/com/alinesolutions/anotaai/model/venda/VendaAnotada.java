package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = VendaAnotada.class)
@NamedQueries({})
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update VendaAnotada set ativo = false where id = ?")
@XmlRootElement
public class VendaAnotada extends BaseEntity<Long, VendaAnotada> implements IVenda {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private Venda venda;

	@ManyToOne(optional = false)
	private FolhaCadernetaVenda folhaCaderneta;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public FolhaCadernetaVenda getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCadernetaVenda folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

}
