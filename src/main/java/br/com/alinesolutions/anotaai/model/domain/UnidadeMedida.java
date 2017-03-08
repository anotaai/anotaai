package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnidadeMedida {
	
	CAIXA("Caixa", "CX"),
	COLHER("Colher", "CL"),
	COPO("Copo 250 ML", "CP"),
	DUZIA("Duzia", "DZ"),
	GRAMA("Gramas", "G"),
	LATA("Lata", "LT"),
	LITRO("Litro", "L"),
	ML("Mililitro", "ML"),
	QUILO("Quilo", "KG"),
	UNIDADE("Unidade", "UN");
	
	private String descricao;
	private String sigla;
	
	private UnidadeMedida(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getSigla() {
		return sigla;
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
	public static UnidadeMedida fromObject(JsonNode node) {
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
