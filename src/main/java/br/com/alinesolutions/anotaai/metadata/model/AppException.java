package br.com.alinesolutions.anotaai.metadata.model;

import javax.ejb.ApplicationException;
import javax.ejb.EJBException;

@ApplicationException
public class AppException extends EJBException {

	private static final long serialVersionUID = 1L;

	private AnotaaiViewException viewException;

	public AppException(AnotaaiViewException viewException) {
		this.viewException = viewException;
	}

	public AnotaaiViewException getViewException() {
		return viewException;
	}

	public void setViewException(AnotaaiViewException viewException) {
		this.viewException = viewException;
	}

}
