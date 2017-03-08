package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoMovimentacao {

	ENTRADA("Entrada", 1),
	SAIDA("Saída", -1);

	private TipoMovimentacao(String descricao, Integer atualizador) {
		this.descricao = descricao;
		this.atualizador = atualizador;
	}

	private String descricao;

	private Integer atualizador;

	public String getDescricao() {
		return descricao;
	}

	/**
	 * Os valores sao colocados sempre positivos, o valor a ser incluido deve
	 * ser multiplicado pelo atulizador
	 * 
	 * @return
	 */
	public Integer getAtualizador() {
		return atualizador;
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
	public static TipoMovimentacao fromObject(JsonNode node) {
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
