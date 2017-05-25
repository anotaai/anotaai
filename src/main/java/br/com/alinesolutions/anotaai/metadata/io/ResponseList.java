package br.com.alinesolutions.anotaai.metadata.io;

import java.util.List;

import br.com.alinesolutions.anotaai.model.BaseEntity;

public class ResponseList <T extends BaseEntity<?, ?>> {

	private List<T> itens;

	private Long qtdTotalItens;

	public List<T> getItens() {
		return itens;
	}

	public void setItens(List<T> itens) {
		this.itens = itens;
	}

	public Long getQtdTotalItens() {
		return qtdTotalItens;
	}

	public void setQtdTotalItens(Long qtdTotalItens) {
		this.qtdTotalItens = qtdTotalItens;
	}

}
