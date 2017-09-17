package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.pagamento.PagamentoConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = FolhaCaderneta.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update FolhaCaderneta set ativo = false where id = ?")
@XmlRootElement
public class FolhaCaderneta extends BaseEntity<Long, FolhaCaderneta> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "folhaCaderneta")
	private List<FolhaCadernetaVenda> vendas;

	@OneToMany(mappedBy = "folhaCaderneta", cascade = CascadeType.ALL)
	private List<PagamentoConsumidor> pagamentos;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	private Caderneta caderneta;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataTermino;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@ManyToOne(cascade = CascadeType.ALL)
	private Consumidor consumidor;

	public Consumidor getConsumidor() {
		return consumidor;
	}

	public void setConsumidor(Consumidor consumidor) {
		this.consumidor = consumidor;
	}

	public List<PagamentoConsumidor> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoConsumidor> pagamentos) {
		this.pagamentos = pagamentos;
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

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

}
