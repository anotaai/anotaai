package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoMovimentacao {

	ENTRADA("EntradaMercadoria", 1, TipoAtualizacaoEstoque.ACRESCENTA),
	SAIDA("Saída", -1, TipoAtualizacaoEstoque.ACRESCENTA),
	ALTERACAO("Alteração", 0, TipoAtualizacaoEstoque.SUBSTITUI);

	private TipoMovimentacao(String descricao, Integer atualizador, TipoAtualizacaoEstoque tipoAtualizacao) {
		this.descricao = descricao;
		this.atualizador = atualizador;
		this.tipoAtualizacao = tipoAtualizacao;
	}

	private String descricao;

	private Integer atualizador;
	
	private TipoAtualizacaoEstoque tipoAtualizacao;

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
	
	public TipoAtualizacaoEstoque getTipoAtualizacao() {
		return tipoAtualizacao;
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
