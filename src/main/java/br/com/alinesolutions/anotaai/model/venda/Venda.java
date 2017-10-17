package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.StatusVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;

@Entity
@NamedQueries({

})
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Venda set ativo = false where id = ?")
@XmlRootElement
public class Venda extends BaseEntity<Long, Venda> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
	private List<ItemVenda> produtos;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;

	@Transient
	private Double valor;

	@Transient
	private Double quantidadeItens;
	
	@Enumerated(EnumType.ORDINAL)
	private StatusVenda statusVenda;

	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
	private List<PagamentoVenda> pagamentos;

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
	
	public StatusVenda getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(StatusVenda statusVenda) {
		this.statusVenda = statusVenda;
	}

	public List<PagamentoVenda> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoVenda> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public interface VendaConstant {
		String FIND_BY_ID_KEY = "Venda.fimdById";
		String FIND_BY_ID_QUERY = "Venda.fimdById";
	}

}
