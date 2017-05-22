package br.com.alinesolutions.anotaai.metadata.model;

import java.util.List;

public class ResponseList {

	private List<?> itens;

	private Long qtdTotalItens;

	public List<?> getItens() {
		return itens;
	}

	public void setItens(List<?> itens) {
		this.itens = itens;
	}

	public Long getQtdTotalItens() {
		return qtdTotalItens;
	}

	public void setQtdTotalItens(Long qtdTotalItens) {
		this.qtdTotalItens = qtdTotalItens;
	}

}
