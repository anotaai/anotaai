package br.com.alinesolutions.anotaai.infra;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.TransactionScoped;
import javax.ws.rs.core.Context;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.message.qualifier.SMS;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ShardingResourceFactory {

	@Resource
	private SessionContext sessionContext;

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager entityManager;

	@Inject
	@Email
	private AnotaaiSendMessage senderMail;

	@Inject
	@SMS
	private AnotaaiSendMessage senderSMS;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Context
	private HttpServletRequest request;

	@Produces
	@TransactionScoped
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public SessionContext getSessionContext() {
		return sessionContext;
	}

	public AnotaaiSendMessage getSenderMail() {
		return senderMail;
	}

	public AnotaaiSendMessage getSenderSMS() {
		return senderSMS;
	}

	public AppService getAppService() {
		return appService;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public ResponseUtil getResponseUtil() {
		return responseUtil;
	}
	

}
