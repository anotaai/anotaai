package br.com.alinesolutions.anotaai.model.venda;

import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ConfiguracaoCaderneta set ativo = false where id = ?")
public class ConfiguracaoCaderneta extends BaseEntity<Long, ConfiguracaoCaderneta> {

	private static final long serialVersionUID = 1L;

	private Integer qtdDiasDuracaoFolha;

	private Date dataInicioAnotacao;

	public Integer getQtdDiasDuracaoFolha() {
		return qtdDiasDuracaoFolha;
	}

	public void setQtdDiasDuracaoFolha(Integer qtdDiasDuracaoFolha) {
		this.qtdDiasDuracaoFolha = qtdDiasDuracaoFolha;
	}

	public Date getDataInicioAnotacao() {
		return dataInicioAnotacao;
	}

	public void setDataInicioAnotacao(Date dataInicioAnotacao) {
		this.dataInicioAnotacao = dataInicioAnotacao;
	}

}
