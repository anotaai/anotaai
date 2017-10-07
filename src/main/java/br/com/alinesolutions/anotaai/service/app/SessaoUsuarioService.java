package br.com.alinesolutions.anotaai.service.app;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.SessaoUsuario.SessaoUsuarioConstant;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto.GrupoProdutoConstant;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class SessaoUsuarioService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	public Response create(SessaoUsuario entity) {
		em.persist(entity);
		return Response.created(UriBuilder.fromResource(SessaoUsuarioService.class).path(String.valueOf(entity.getId())).build()).build();
	}

	public Response deleteById(@PathParam("id") Long id) {
		SessaoUsuario entity = em.find(SessaoUsuario.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	public Response findById(@PathParam("id") Long id) {
		TypedQuery<SessaoUsuario> findByIdQuery = em.createQuery("SELECT DISTINCT s FROM SessaoUsuario s LEFT JOIN FETCH s.usuario WHERE s.id = :entityId ORDER BY s.id", SessaoUsuario.class);
		findByIdQuery.setParameter("entityId", id);
		SessaoUsuario entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	public List<SessaoUsuario> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		TypedQuery<SessaoUsuario> findAllQuery = em.createQuery("SELECT DISTINCT s FROM SessaoUsuario s LEFT JOIN FETCH s.usuario ORDER BY s.id", SessaoUsuario.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<SessaoUsuario> results = findAllQuery.getResultList();
		return results;
	}

	public void resetSession(SessaoUsuario sessaoUsuario) {
		Query query = em.createNamedQuery(SessaoUsuarioConstant.RESET_SESSION_KEY);
		query.setParameter(GrupoProdutoConstant.FIELD_SESSION_ID, sessaoUsuario.getSessionID());
		query.setParameter(GrupoProdutoConstant.FIELD_ULTIMO_ACESSO, new Date());
		query.executeUpdate();
	}

	public ResponseEntity<?> isActive(String sessionID) {
		ResponseEntity<?> responseEntity = null;
		TypedQuery<Long> query = em.createNamedQuery(SessaoUsuarioConstant.COUNT_KEY, Long.class);
		query.setParameter(SessaoUsuario.SessaoUsuarioConstant.FIELD_SESSION_ID, sessionID);
		Long qtd = query.getSingleResult();
		responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(qtd > 0);
		if (!responseEntity.getIsValid()) {
			responseEntity.addMessage(IMessage.SECURITY_SECURITY_SESSION_TIMEOUT, TipoMensagem.ERROR, Constant.App.LONG_TIME_VIEW);
		}
		return responseEntity;
	}

	public SessaoUsuario getSessionActive(String sessionID) throws AppException {
		SessaoUsuario sessaoUsuario = null;
		TypedQuery<SessaoUsuario> querySessaoUsuario = em.createNamedQuery(SessaoUsuario.SessaoUsuarioConstant.FIND_BY_SESSIONID_KEY, SessaoUsuario.class);
		querySessaoUsuario.setParameter(SessaoUsuario.SessaoUsuarioConstant.FIELD_SESSION_ID, sessionID);
		try {
			sessaoUsuario = querySessaoUsuario.getSingleResult();
		} catch (NoResultException e) {
			ResponseEntity<?> responseEntity = new ResponseEntity<>();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(IMessage.SECURITY_SECURITY_SESSION_TIMEOUT, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW);
			throw new AppException(responseEntity );
		}
		return sessaoUsuario;
	}

}
