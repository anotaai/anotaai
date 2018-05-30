package br.com.alinesolutions.anotaai.model.venda;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.StatusVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Vendedor;

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

	@NotNull
	private ZonedDateTime inicioVenda;

	private ZonedDateTime conclusaoVenda;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	private Cliente cliente;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	private Vendedor vendedor;

	@Transient
	private Double valor;

	@Transient
	private Double quantidadeItens;

	@NotNull
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

	public ZonedDateTime getInicioVenda() {
		return inicioVenda;
	}

	public void setInicioVenda(ZonedDateTime inicioVenda) {
		this.inicioVenda = inicioVenda;
	}

	public ZonedDateTime getConclusaoVenda() {
		return conclusaoVenda;
	}

	public void setConclusaoVenda(ZonedDateTime conclusaoVenda) {
		this.conclusaoVenda = conclusaoVenda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public interface VendaConstant {
		String FIND_BY_ID_KEY = "Venda.fimdById";
		String FIND_BY_ID_QUERY = "Venda.fimdById";
	}

}
