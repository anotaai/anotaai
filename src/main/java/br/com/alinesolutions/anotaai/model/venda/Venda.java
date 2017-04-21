package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.venda.Venda.VendaConstant;

@Entity
@NamedQueries({
	@NamedQuery(name = VendaConstant.LIST_BY_PERIOD_KEY, query = VendaConstant.LIST_BY_PERIOD_QUERY) 
})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Venda set ativo = false where id = ?")
@XmlRootElement
public class Venda extends BaseEntity<Long, Venda> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "venda")
	private List<ItemVenda> produtos;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;

	@Transient
	private Double valor;

	@Transient
	private Double quantidadeItens;

	public List<ItemVenda> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ItemVenda> produtos) {
		this.produtos = produtos;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getQuantidadeItens() {
		return quantidadeItens;
	}

	public void setQuantidadeItens(Double quantidadeItens) {
		this.quantidadeItens = quantidadeItens;
	}

	public interface VendaConstant {
		String LIST_BY_PERIOD_KEY = "Venda.listByPeriod";
		String LIST_BY_PERIOD_QUERY = "select cv.venda from ClienteVenda cv join cv.venda v where cv.cliente = :cliente and v.dataVenda BETWEEN :startDate AND :endDate";
		String FIND_BY_ID_KEY = "Venda.fimdById";
		String FIND_BY_ID_QUERY = "Venda.fimdById";
	}

}
