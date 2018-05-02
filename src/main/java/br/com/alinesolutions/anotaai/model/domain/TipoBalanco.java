package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import br.com.alinesolutions.anotaai.model.util.EnumSerialize;

@EnumSerialize
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoBalanco {

	/**
	 * Balanco para atualizar a quantidade de mercadorias pontuais
	 */
	MOVIMENTACAO("Movimentacao"),
	
	/**
	 * Balanco para contagem de mercadorias gerais
	 */
	ACERTO("Acerto"),
	
	/**
	 * Balanco para correcao de entrada de mercadoria
	 * */
	CORRECAO_ENTRADA("Correção de Entrada");
	
	private TipoBalanco(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
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
	public static TipoBalanco fromObject(JsonNode node) {
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
