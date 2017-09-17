package br.com.alinesolutions.anotaai.model.venda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
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

	@ManyToOne()
	private FolhaCaderneta folhaCaderneta;

	@Any(metaColumn = @Column(name = "tipo_venda", length = 16), fetch = FetchType.LAZY)
	@AnyMetaDef(
		idType = "long", metaType = "string", 
		metaValues = { 
			@MetaValue(targetEntity = VendaAVistaConsumidor.class, value = "A_VISTA_CONSUMIDOR"), 
			@MetaValue(targetEntity = VendaAnotadaConsumidor.class, value = "ANOTADA_CONSUMIDOR")
		}
	)
	@JoinColumn(name="venda_id")
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
