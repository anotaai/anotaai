package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;
import java.util.List;

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

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Caderneta set ativo = false where id = ?")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Caderneta.class)
public class Caderneta extends BaseEntity<Long, Caderneta> {

	private static final long serialVersionUID = 1L;

	/**
	 * O Cliente pode anotar em locais diferentes, e para cada local ele pode ter uma caderneta diferente
	 * 
	 * Ex, caderneta da faculdade
	 *     caderneta do trabalho
	 */
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	private Date dataAbertura;

	@Temporal(TemporalType.DATE)
	private Date dataFechamento;
	
	@OneToMany(mappedBy="caderneta")
	private List<FolhaCaderneta> folhas;

	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public List<FolhaCaderneta> getFolhas() {
		return folhas;
	}

	public void setFolhas(List<FolhaCaderneta> folhas) {
		this.folhas = folhas;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
