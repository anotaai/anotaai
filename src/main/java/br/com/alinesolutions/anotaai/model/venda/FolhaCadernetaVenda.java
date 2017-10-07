package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

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
@SQLDelete(sql = "update FolhaCadernetaVenda set ativo = false where id = ?")
@XmlRootElement
public class FolhaCadernetaVenda extends BaseEntity<Long, FolhaCadernetaVenda> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade=CascadeType.ALL)
	private FolhaCaderneta folhaCaderneta;

	@Any(metaColumn = @Column(name = "tipo_venda", length = 32), fetch = FetchType.LAZY, optional=false)
	@AnyMetaDef(
		idType = "long", metaType = "string", 
		metaValues = { 
			@MetaValue(targetEntity = VendaAVistaConsumidor.class, value = "A_VISTA_CONSUMIDOR"), 
			@MetaValue(targetEntity = VendaAnotadaConsumidor.class, value = "ANOTADA_CONSUMIDOR"),
			@MetaValue(targetEntity = VendaAVistaAnonima.class, value = "A_VISTA_ANONIMA")
		}
	)
	@Cascade({ org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name="venda_id", nullable = false)
	private IVendaConsumidor venda;

	public FolhaCaderneta getFolhaCaderneta() {
		return folhaCaderneta;
	}

	public void setFolhaCaderneta(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}

	public IVendaConsumidor getVenda() {
		return venda;
	}

	public void setVenda(IVendaConsumidor venda) {
		this.venda = venda;
	}

}
