package br.com.alinesolutions.anotaai.metadata.model;

import java.util.List;

public class ResponseList {

	private List<?> itens;

	private Integer qtdTotalItens;

	public List<?> getItens() {
		return itens;
	}

	public void setItens(List<?> itens) {
		this.itens = itens;
	}

	public Integer getQtdTotalItens() {
		return qtdTotalItens;
	}

	public void setQtdTotalItens(Integer qtdTotalItens) {
		this.qtdTotalItens = qtdTotalItens;
	}

}
