package br.com.alinesolutions.anotaai.service;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiViewException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.util.Constant;

@Singleton
@Startup
public class ResponseUtil {

	public void buildIllegalArgumentException(ResponseEntity entity) {
		AnotaaiViewException exception = new AnotaaiViewException(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR,
				Constant.Message.KEEP_ALIVE_TIME_VIEW);
		entity.setIsValid(Boolean.FALSE);
		entity.setException(exception);
	}

	
}
