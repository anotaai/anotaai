package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoMensagem {

	ERROR("error", Icon.ERROR),
	SUCCESS("success", Icon.DONE),
	INFO("info", Icon.INFO),
	WARNING("warning", Icon.WARNING);
	
	private String descricao;
	private Icon icon;
	
	private TipoMensagem(String descricao, Icon icon) {
		this.descricao = descricao;
		this.icon = icon;
	}
	
	public Icon getIcon() {
		return icon;
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
	public static TipoMensagem fromObject(JsonNode node) {
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
