package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;
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
	@Any(metaColumn = @Column(name = "tipo_venda", length = 32), fetch = FetchType.LAZY, optional=false)
	@AnyMetaDef(
		idType = "long", metaType = "string", 
		metaValues = { 
			@MetaValue(targetEntity = VendaAVistaAnonima.class, value = "A_VISTA_ANONIMA")
		}
	)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name="venda_id", nullable = false)
	private IVendaAnonima venda;

	public Caderneta getCaderneta() {
		return caderneta;
	}

	public void setCaderneta(Caderneta caderneta) {
		this.caderneta = caderneta;
	}

	public IVendaAnonima getVenda() {
		return venda;
	}

	public void setVenda(IVendaAnonima venda) {
		this.venda = venda;
	}

}
