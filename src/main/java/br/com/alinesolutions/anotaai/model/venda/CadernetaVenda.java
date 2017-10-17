package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@NamedQueries({

})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update CadernetaVenda set ativo = false where id = ?")
public class CadernetaVenda extends BaseEntity<Long, CadernetaVenda> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(cascade={CascadeType.DETACH})
	private Caderneta caderneta;

	@NotNull
	@ManyToOne(cascade=CascadeType.DETACH)
	private Venda venda;

	public Caderneta getCaderneta() {
		return caderneta;
	}

	public void setCaderneta(Caderneta caderneta) {
		this.caderneta = caderneta;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
