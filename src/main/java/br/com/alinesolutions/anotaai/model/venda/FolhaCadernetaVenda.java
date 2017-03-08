package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@NamedQueries({})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update FolhaCadernetaVenda set ativo = false where id = ?")
@XmlRootElement
public class FolhaCadernetaVenda extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Venda venda;

	@ManyToOne
	private FolhaCaderneta folhaCaderneta;

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

}
