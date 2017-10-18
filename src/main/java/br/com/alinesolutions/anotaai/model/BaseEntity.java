package br.com.alinesolutions.anotaai.model;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@MappedSuperclass
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
public abstract class BaseEntity<ID, T extends BaseEntity<?, ?>> implements Serializable {

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;

	@JsonIgnore
	private Boolean ativo;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Boolean equal = Boolean.TRUE;
		if (this == obj) {
			equal = Boolean.TRUE;
		} else {
			if (obj == null) {
				equal = Boolean.FALSE;
			} else if (getClass() != obj.getClass()) {
				equal = Boolean.FALSE;
			} else {
				BaseEntity<?, ?> other = (BaseEntity<?, ?>) obj;
				if (id == null) {
					if (other.id != null) {
						equal = Boolean.FALSE;
					}
				} else if (!id.equals(other.id)) {
					equal = Boolean.FALSE;
				}
			}
		}
		return equal;
	}

	@Override
	public String toString() {
		String simpleName = getClass().getSimpleName();
		return new StringBuilder(simpleName).append("[").append(id).append("]").toString();
	}

	@PrePersist
	protected void onPrePersist() {
		ativo = Boolean.TRUE;
	}

	public interface BaseEntityConstant {
		String FIELD_ID = "id";
		String FIELD_CLIENTE = "cliente";
		String FIELD_CONSUMIDOR = "consumidor";
		String FIELD_PRODUTO = "produto";
	}

	/**
	 * Metodo utilizado na edicao de objetos, o objeto original é recuperado do
	 * banco e invoca este metodo passando o objeto que foi manipulado na tela.
	 * 
	 * Todos os atributos nao nulos do objeto passado como parametro serao copiados
	 * para o objeto que invocou o metodos
	 * 
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public T clone() {
		try {
			ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
			Type type = parameterizedType.getActualTypeArguments()[1];
			ObjectMapper mapper = new ObjectMapper();
			String entityStr = mapper.writeValueAsString(this);
			T clone = mapper.readValue(entityStr, (Class<T>) type);
			return clone;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}