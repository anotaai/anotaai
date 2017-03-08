package br.com.alinesolutions.anotaai.model.util;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;

@NamedQueries({})
@Entity
public class ArquivoTemporario extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private SessaoUsuario sessaoUsuario;

	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private Arquivo arquivo;

	private String uuid;

	public SessaoUsuario getSessaoUsuario() {
		return sessaoUsuario;
	}

	@XmlTransient
	public Arquivo getArquivo() {
		return arquivo;
	}

	public String getUuid() {
		return uuid;
	}

	public void setSessaoUsuario(SessaoUsuario sessaoUsuario) {
		this.sessaoUsuario = sessaoUsuario;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
