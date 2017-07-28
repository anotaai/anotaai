package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoMovimentacao {

	ENTRADA("EntradaMercadoria", 1) {
		@Override
		public void atualizarEstoque(Estoque estoque, IMovimentacao movimentacao) {
			Long quantidadeEstoque = estoque.getQuantidadeEstoque();
			Long quantidadeMovimentacao = movimentacao.getMovimentacaoProduto().getQuantidade();
			long novaQuantidadeEstoque = quantidadeEstoque + quantidadeMovimentacao * getAtualizador();
			
			if(novaQuantidadeEstoque < 0){
				novaQuantidadeEstoque = 0;
			}
			estoque.setQuantidadeEstoque(novaQuantidadeEstoque);
		}
	},
	
	SAIDA("Saída", -1) {
		@Override
		public void atualizarEstoque(Estoque estoque, IMovimentacao movimentacao) {
			Long quantidadeEstoque = estoque.getQuantidadeEstoque();
			Long quantidadeMovimentacao = movimentacao.getMovimentacaoProduto().getQuantidade();
			long novaQuantidadeEstoque = quantidadeEstoque + quantidadeMovimentacao * getAtualizador();
			if(novaQuantidadeEstoque < 0){
				novaQuantidadeEstoque = 0;
			}
			estoque.setQuantidadeEstoque(novaQuantidadeEstoque);
		}
	},
	
	ALTERACAO("Alteração", 0) {
		@Override
		public void atualizarEstoque(Estoque estoque, IMovimentacao movimentacao) {
			estoque.setQuantidadeEstoque(movimentacao.getMovimentacaoProduto().getQuantidade());
		}
	};

	private TipoMovimentacao(String descricao, Integer atualizador) {
		this.descricao = descricao;
		this.atualizador = atualizador;
	}

	private String descricao;

	private Integer atualizador;
	
	public abstract void atualizarEstoque(Estoque estoque, IMovimentacao movimentacao);

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
