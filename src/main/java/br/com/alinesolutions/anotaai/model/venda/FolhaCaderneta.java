package br.com.alinesolutions.anotaai.model.venda;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=FolhaCaderneta.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update FolhaCaderneta set ativo = false where id = ?")
@XmlRootElement
public class FolhaCaderneta extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "folhaCaderneta", cascade = CascadeType.ALL)
	private List<FolhaCadernetaVenda> vendas;

	@OneToMany(mappedBy = "folhaCaderneta", cascade = CascadeType.ALL)
	private List<Pagamento> pagamentos;
	
	@ManyToOne(cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
	private Caderneta caderneta;
	
	@ManyToOne
	private ConsumidorVenda consumidor;

	public List<FolhaCadernetaVenda> getVendas() {
		return vendas;
	}

	public void setVendas(List<FolhaCadernetaVenda> vendas) {
		this.vendas = vendas;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
}
