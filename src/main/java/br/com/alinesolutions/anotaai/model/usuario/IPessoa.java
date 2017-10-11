package br.com.alinesolutions.anotaai.model.usuario;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoPessoa;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@Type(value = Cliente.class, name="CLIENTE"),
	@Type(value = Consumidor.class, name="ANOTADO")
})
public interface IPessoa {

	Usuario getUsuario();

	void setUsuario(Usuario usuario);

	TipoPessoa getTipoPessoa();
	
}
