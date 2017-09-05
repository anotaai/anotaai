package br.com.alinesolutions.anotaai.model.venda;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.model.BaseEntity;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ConfiguracaoCaderneta set ativo = false where id = ?")
public class ConfiguracaoCaderneta extends BaseEntity<Long, ConfiguracaoCaderneta> {

	private static final long serialVersionUID = 1L;

	private Integer qtdDiasDuracaoFolha;

	private Integer diaBase;
	
	@OneToMany(mappedBy = "configuracao",cascade = { CascadeType.ALL } , fetch = FetchType.LAZY)
	private List<Caderneta> cadernetas;
	
	public ConfiguracaoCaderneta() {
		super();
	}
	
	public ConfiguracaoCaderneta(Long id) {
		setId(id);
	}
	
	public ConfiguracaoCaderneta(Long id,Integer qtdDiasDuracaoFolha,Integer diaBase) {
		setId(id);
		this.qtdDiasDuracaoFolha = qtdDiasDuracaoFolha;
		this.diaBase = diaBase;
	}

	public Integer getQtdDiasDuracaoFolha() {
		return qtdDiasDuracaoFolha;
	}

	public void setQtdDiasDuracaoFolha(Integer qtdDiasDuracaoFolha) {
		this.qtdDiasDuracaoFolha = qtdDiasDuracaoFolha;
	}

	public Integer getDiaBase() {
		return diaBase;
	}

	public void setDiaBase(Integer diaBase) {
		this.diaBase = diaBase;
	}

	public List<Caderneta> getCadernetas() {
		return cadernetas;
	}

	public void setCadernetas(List<Caderneta> cadernetas) {
		this.cadernetas = cadernetas;
	}

}
