package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Pagamento.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Pagamento set ativo = false where id = ?")
@XmlRootElement
public class Pagamento extends BaseEntity<Long, Pagamento> {

	private static final long serialVersionUID = 1L;

	private Date dataPagamento;
	
	private Double valor;

	@ManyToOne(cascade = CascadeType.DETACH)
	private FolhaCaderneta folhaCaderneta;

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public FolhaCaderneta getComanda() {
		return folhaCaderneta;
	}

	public void setComanda(FolhaCaderneta folhaCaderneta) {
		this.folhaCaderneta = folhaCaderneta;
	}
}
