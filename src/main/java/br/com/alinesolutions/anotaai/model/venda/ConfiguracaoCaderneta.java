package br.com.alinesolutions.anotaai.model.venda;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TimeZone;

@Entity
@Where(clause = "ativo = true")
@SQLDelete(sql = "update ConfiguracaoCaderneta set ativo = false where id = ?")
public class ConfiguracaoCaderneta extends BaseEntity<Long, ConfiguracaoCaderneta> {

	private static final long serialVersionUID = 1L;

	private String descricao;

	private Integer qtdDiasDuracaoFolha;

	@NotNull
	private Integer diaBase;

	@NotNull
	private TimeZone timeZone;

	/**
	 * Quando o Cliente inicia a venda este tipo aparece selecionado por padrao
	 */
	private TipoVenda tipoVendaPadrao;

	/**
	 * Este campo define quanto tempo em segundos o sistema apresenta o cupom de
	 * venda apos finalizar a venda.
	 * 
	 */
	private Integer timeoutSetupVenda;

	@OneToMany(mappedBy = "configuracao", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Caderneta> cadernetas;

	public ConfiguracaoCaderneta() {
		super();
	}

	public ConfiguracaoCaderneta(Long id) {
		setId(id);
	}

	public ConfiguracaoCaderneta(Long id, Integer qtdDiasDuracaoFolha, Integer diaBase) {
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

	public Integer getTimeoutSetupVenda() {
		return timeoutSetupVenda;
	}

	public void setTimeoutSetupVenda(Integer timeoutSetupVenda) {
		this.timeoutSetupVenda = timeoutSetupVenda;
	}

	public TipoVenda getTipoVendaPadrao() {
		return tipoVendaPadrao;
	}

	public void setTipoVendaPadrao(TipoVenda tipoVendaPadrao) {
		this.tipoVendaPadrao = tipoVendaPadrao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
