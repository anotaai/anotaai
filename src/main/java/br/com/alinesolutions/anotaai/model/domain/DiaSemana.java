package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiaSemana {

	DOMINGO("Domingo", "Dom"),
	SEGUNA_FEIRA("Segunda Feira", "Seg"),
	TERCA_FEIRA("Terça Feira", "Ter"),
	QUARTA_FEIRA("Quarta Feira", "Qua"),
	QUINTA_FEIRA("Quinta Feira", "Qui"),
	SEXTA_FEIRA("Sexta Feira", "Sex"),
	SABADO("Sábado", "Sab");

	private DiaSemana(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	private String descricao;

	private String sigla;

	public String getDescricao() {
		return descricao;
	}

	public String getSigla() {
		return sigla;
	}
	
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
	public static DiaSemana fromObject(JsonNode node) {
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
