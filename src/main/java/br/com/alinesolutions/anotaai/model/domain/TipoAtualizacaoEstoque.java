package br.com.alinesolutions.anotaai.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.MovimentacaoProduto;
import br.com.alinesolutions.anotaai.model.util.EnumSerialize;

@EnumSerialize
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoAtualizacaoEstoque {

	ACRESCENTA("Acrescenta") {
		@Override
		public void atualizarEstoque(Estoque estoque, MovimentacaoProduto movimentacao) {
			Long quantidadeEstoque = estoque.getQuantidadeEstoque();
			Long quantidadeMovimentacao = movimentacao.getQuantidade();
			long novaQuantidadeEstoque = quantidadeEstoque + quantidadeMovimentacao * movimentacao.getTipoMovimentacao().getAtualizador();
			
			if(novaQuantidadeEstoque < 0){
				novaQuantidadeEstoque = 0;
			}
				
			estoque.setQuantidadeEstoque(novaQuantidadeEstoque);
		}
	},
	SUBSTITUI("Substitui") {
		@Override
		public void atualizarEstoque(Estoque estoque, MovimentacaoProduto movimentacao) {
			estoque.setQuantidadeEstoque(movimentacao.getQuantidade());
		}
		
	};

	private TipoAtualizacaoEstoque(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public abstract void atualizarEstoque(Estoque estoque, MovimentacaoProduto movimentacao);

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
	public static TipoAtualizacaoEstoque fromObject(JsonNode node) {
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
