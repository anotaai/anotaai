package br.com.alinesolutions.anotaai.model.usuario;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sun.istack.NotNull;

import br.com.alinesolutions.anotaai.i18n.Locale;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TimeZone;
import br.com.alinesolutions.anotaai.model.produto.Produto;

@Entity
public class Preferencia extends BaseEntity<Long, Produto> {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer itensPerPage;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Locale locale;

	@Enumerated(EnumType.ORDINAL)
	private TimeZone timeZone;

	public Integer getItensPerPage() {
		return itensPerPage;
	}

	public void setItensPerPage(Integer itensPerPage) {
		this.itensPerPage = itensPerPage;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
