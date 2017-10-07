package br.com.alinesolutions.anotaai.service;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.util.Constant;

@Singleton
@Startup
public class ResponseUtil {

	public void buildIllegalArgumentException(ResponseEntity<?> entity) {
		entity.addMessage(IMessage.ILLEGAL_ARGUMENT, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW);
		entity.setIsValid(Boolean.FALSE);
	}

	
}
