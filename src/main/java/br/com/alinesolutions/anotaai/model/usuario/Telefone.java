package br.com.alinesolutions.anotaai.model.usuario;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.Operadora;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Telefone.class)
@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update Telefone set ativo = false where id = ?")
@Table(uniqueConstraints={
		//TODO - criar constraint para nao gravar telefones repetidos
		//@UniqueConstraint(columnNames="ddi_tel, ddd_tel, numero_tel")
})
@XmlRootElement
public class Telefone extends BaseEntity<Long, Telefone> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer ddi;

	@NotNull
	private Integer ddd;

	@NotNull
	private Integer numero;
	
	public String getPhoneNumber() {
		return new StringBuilder(ddi).append(ddd).append(numero).toString();
	}

	@Enumerated(EnumType.ORDINAL)
	private Operadora operadora;

	public Telefone() {
		super();
	}
	
	public Telefone(Integer ddi, Integer ddd, Integer numero) {
		this.ddi = ddi;
		this.ddd = ddd;
		this.numero = numero;
	}

	public Integer getDdi() {
		return ddi;
	}

	public void setDdi(Integer ddi) {
		this.ddi = ddi;
	}

	public Integer getDdd() {
		return ddd;
	}

	public void setDdd(Integer ddd) {
		this.ddd = ddd;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public interface TelefoneConstant {

		String FIELD_DDI = "ddi";
		String FIELD_DDD = "ddd";
		String FIELD_NUMERO = "numero";
		
	}
	
	@Override
	public void clone(Telefone entity) {
		super.clone(entity);
		if (entity != null) {
			if (entity.getDdi() != null) {
				this.ddi = entity.getDdi();
			}
			if (entity.getDdd() != null) {
				this.ddd = entity.getDdd();
			}
			if (entity.getNumero() != null) {
				this.numero = entity.getNumero();
			}
		}
	}
}

