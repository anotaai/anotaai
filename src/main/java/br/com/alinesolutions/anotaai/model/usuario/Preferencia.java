package br.com.alinesolutions.anotaai.model.usuario;

import javax.persistence.Entity;

import com.sun.istack.NotNull;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Produto;

@Entity
public class Preferencia extends BaseEntity<Long, Produto> {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Integer itensPerPage;

	public Integer getItensPerPage() {
		return itensPerPage;
	}

	public void setItensPerPage(Integer itensPerPage) {
		this.itensPerPage = itensPerPage;
	}

}
