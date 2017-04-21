package br.com.alinesolutions.anotaai.metadata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial.AnotaaiSequenceConstant;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoCodigoInterno;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;;

@Entity
@NamedQueries({
	@NamedQuery(name = AnotaaiSequenceConstant.GET_SEQUENCE_KEY, query = AnotaaiSequenceConstant.GET_SEQUENCE_QUERY)
})
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "tipoCodigoInterno", "cliente" }) })
public class AnotaaiSequencial extends BaseEntity<Long, AnotaaiSequencial> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoCodigoInterno", length = 64)
	private TipoCodigoInterno tipoCodigoInterno;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente")
	private Cliente cliente;

	private Long sequence;

	public AnotaaiSequencial() {
		super();
	}
	
	public AnotaaiSequencial(TipoCodigoInterno tipoCodigoInterno, Cliente cliente) {
		super();
		this.tipoCodigoInterno = tipoCodigoInterno;
		this.cliente = cliente;
	}

	public TipoCodigoInterno getTipoCodigoInterno() {
		return tipoCodigoInterno;
	}

	public void setTipoCodigoInterno(TipoCodigoInterno tipoCodigoInterno) {
		this.tipoCodigoInterno = tipoCodigoInterno;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	@PrePersist
	protected void onPrePersist() {
		sequence = 1L;
		super.onPrePersist();
	}

	public interface AnotaaiSequenceConstant {

		String FIELD_TIPO_CODIGO_INTERNO = "tipoCodigoInterno";

		String GET_SEQUENCE_KEY = "AnotaaiSequencial.getSequence";
		String GET_SEQUENCE_QUERY = "select asq from AnotaaiSequencial asq where asq.cliente = :cliente and asq.tipoCodigoInterno = :tipoCodigoInterno";
	}

}