package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoVenda {
	
	A_VISTA_ANONIMA("A vista anônima"),
	ANOTADA_CONSUMIDOR("Anotada consumidor"),
	A_VISTA_CONSUMIDOR("A vista consumidor");
	
	private String descricao;
	
	private TipoVenda(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	// TODO - Adicionar metodos dinamicamente
	public String getType() {
		return this.toString();
	}

	public String getPropertieKey() {
		StringBuilder sb = new StringBuilder("enum.");
		sb.append(this.getClass().getName()).append(".");
		sb.append(toString());
		return sb.toString().toLowerCase();
	}

	@JsonCreator
	public static TipoVenda fromObject(JsonNode node) {
		String type = null;
		if (node.getNodeType().equals(JsonNodeType.STRING)) {
			type = node.asText();
		} else {
			if (!node.has("type")) {
				throw new IllegalArgumentException();
			}
			type = node.get("type").asText();
		}
		return valueOf(type);
	}
	
}
