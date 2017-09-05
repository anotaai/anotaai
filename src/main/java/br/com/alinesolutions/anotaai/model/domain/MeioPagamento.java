package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MeioPagamento {
	
	BOLETO("Boleto"),
	CARTAO_CREDITO("Cartão de Crédito"),
	CARTAO_DEBITO("Cartão de débito"),
	DEPOSITO_EM_CONTA("Depósito em conta"),
	DINHEIRO("Dinheiro"),
	ESCAMBO("Escambo"),
	PAYPAL("Paypal");
	
	private String descricao;
	
	private MeioPagamento(String descricao) {
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
	public static MeioPagamento fromObject(JsonNode node) {
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
