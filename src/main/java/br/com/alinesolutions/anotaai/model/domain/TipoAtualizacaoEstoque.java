package br.com.alinesolutions.anotaai.model.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

		@Override
		public void atualizarCusto(Estoque estoque, MovimentacaoProduto movimentacao , Double custo) {
			Double novoPrecoCusto = estoque.getPrecoCusto() + custo * movimentacao.getTipoMovimentacao().getAtualizador();
			BigDecimal bd = new BigDecimal(novoPrecoCusto);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			
			if(bd.doubleValue() < 0){
				bd = new BigDecimal(0);
			}
			
			estoque.setPrecoCusto(bd.doubleValue());
		}
	},
	SUBSTITUI("Substitui") {
		@Override
		public void atualizarEstoque(Estoque estoque, MovimentacaoProduto movimentacao) {
			estoque.setQuantidadeEstoque(movimentacao.getQuantidade());
		}
		
		@Override
		public void atualizarCusto(Estoque estoque, MovimentacaoProduto movimentacao , Double custo) {
			BigDecimal bd = new BigDecimal(custo);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			estoque.setPrecoCusto(bd.doubleValue());
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
	
	public abstract void atualizarCusto(Estoque estoque, MovimentacaoProduto movimentacao , Double custo);

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
