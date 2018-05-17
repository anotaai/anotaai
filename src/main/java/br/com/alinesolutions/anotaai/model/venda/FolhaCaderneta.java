package br.com.alinesolutions.anotaai.model.venda;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta.FolhaCadernetaConstant;

@NamedQueries({
	@NamedQuery(name=FolhaCadernetaConstant.FIND_FOLHA_VIGENTE, query=FolhaCadernetaConstant.FIND_FOLHA_VIGENTE_QUERY)
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update FolhaCaderneta set ativo = false where id = ?")
@XmlRootElement
public class FolhaCaderneta extends BaseEntity<Long, FolhaCaderneta> {

	private static final long serialVersionUID = 1L;

	@JsonManagedReference(value="vendas")
	@OneToMany(mappedBy = "folhaCaderneta")
	private List<FolhaCadernetaVenda> vendas;
	
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	private Caderneta caderneta;

	@NotNull
	private LocalDate dataInicio;

	@NotNull
	private LocalDate dataTermino;

	@NotNull
	private ZonedDateTime dataCriacao;

	@NotNull
	@ManyToOne()
	private ClienteConsumidor clienteConsumidor;

	public ClienteConsumidor getClienteConsumidor() {
		return clienteConsumidor;
	}

	public void setClienteConsumidor(ClienteConsumidor clienteConsumidor) {
		this.clienteConsumidor = clienteConsumidor;
	}

	public List<FolhaCadernetaVenda> getVendas() {
		return vendas;
	}

	public void setVendas(List<FolhaCadernetaVenda> vendas) {
		this.vendas = vendas;
	}

	public Caderneta getCaderneta() {
		return caderneta;
	}

	public void setCaderneta(Caderneta caderneta) {
		this.caderneta = caderneta;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(LocalDate dataTermino) {
		this.dataTermino = dataTermino;
	}

	public ZonedDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(ZonedDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public interface FolhaCadernetaConstant {
		String FIND_FOLHA_VIGENTE = "FolhaCaderneta.findFolhaVigente";
		String FIND_FOLHA_VIGENTE_QUERY = "select folha from FolhaCaderneta folha where :hoje between folha.dataInicio and folha.dataTermino";
	}

}
