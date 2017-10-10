package br.com.alinesolutions.anotaai.model;

import java.io.IOException;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	public T clone() {
		try {
			ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
			Type type = parameterizedType.getActualTypeArguments()[1];
			Gson gson = new GsonBuilder().create();
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addSerializer(new CustomSerializer(TypeFactory.defaultInstance().constructType(type)));
			mapper.registerModule(module);
			String entityStr = mapper.writeValueAsString(this);
			T clone = gson.fromJson(entityStr, type);
			return clone;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static class CustomSerializer extends StdSerializer<BaseEntity<?, ?>> implements ContextualSerializer {
		
		private final JavaType _type;
		
		protected CustomSerializer(JavaType type) {
			super(type);
			_type = type;
		}

		private JavaType valueType;
		
		@Override
		public void serialize(BaseEntity<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			System.out.println(valueType);
		}

		//https://www.programcreek.com/java-api-examples/index.php?api=com.fasterxml.jackson.databind.ser.ContextualSerializer
		//https://github.com/FasterXML/jackson-datatypes-collections/blob/master/guava/src/main/java/com/fasterxml/jackson/datatype/guava/ser/TableSerializer.java
		@Override
		public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
			return provider.findValueSerializer(_type, property);
		}
		
//		public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
//			
//			TypeSerializer typeSer = _valueTypeSerializer;
//			
//			if (typeSer != null) {
//				typeSer = typeSer.forProperty(property);
//			}
//			/*
//			 * 29-Sep-2012, tatu: Actually, we need to do much more contextual
//			 * checking here since we finally know for sure the property, and it
//			 * may have overrides
//			 */
//			JsonSerializer<?> ser = null;
//			// First: if we have a property, may have property-annotation
//			// overrides
//			if (property != null) {
//				AnnotatedMember m = property.getMember();
//				if (m != null) {
//					Object serDef = provider.getAnnotationIntrospector().findContentSerializer(m);
//					if (serDef != null) {
//						ser = provider.serializerInstance(m, serDef);
//					}
//				}
//			}
//			if (ser == null) {
//				ser = _elementSerializer;
//			}
//			if (ser == null) {
//				// 30-Sep-2012, tatu: One more thing -- if explicit content type
//				// is annotated,
//				// we can consider it a static case as well.
//				if (_elementType != null) {
//					if (_staticTyping || hasContentTypeAnnotation(provider, property)) {
//						ser = provider.findValueSerializer(_elementType, property);
//					}
//				}
//			} else if (ser instanceof ContextualSerializer) {
//				ser = ((ContextualSerializer) ser).createContextual(provider, property);
//			}
//			if ((ser != _elementSerializer) || (property != _property) || _valueTypeSerializer != typeSer) {
//				return withResolved(property, typeSer, ser);
//			}
//			return this;
//		}
	}
}