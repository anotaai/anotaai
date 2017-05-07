package br.com.alinesolutions.anotaai.metadata.model;

import javax.ejb.ApplicationException;
import javax.ejb.EJBException;

@ApplicationException
public class AppException extends EJBException {

	private static final long serialVersionUID = 1L;

	private ResponseEntity responseEntity;

	public AppException(ResponseEntity responseEntity) {
		this.responseEntity = responseEntity;
	}

	public ResponseEntity getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity responseEntity) {
		this.responseEntity = responseEntity;
	}

	
}
