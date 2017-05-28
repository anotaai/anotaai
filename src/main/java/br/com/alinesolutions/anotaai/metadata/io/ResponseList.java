package br.com.alinesolutions.anotaai.metadata.io;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@JsonInclude(Include.NON_NULL)
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
