package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.LocalVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@NamedQueries({

})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update FolhaCadernetaVenda set ativo = false where id = ?")
@XmlRootElement
public class FolhaCadernetaVenda extends BaseEntity<Long, FolhaCadernetaVenda> implements IVenda {

	private static final long serialVersionUID = 1L;

	@Override
	public LocalVenda getLocalVenda() {
		return LocalVenda.FOLHA_CADERNETA;
	}
	
	@NotNull
	@ManyToOne(cascade={CascadeType.DETACH})
	private FolhaCaderneta folhaCaderneta;

	@NotNull
	@ManyToOne(cascade = CascadeType.DETACH)
	private Venda venda;

	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
