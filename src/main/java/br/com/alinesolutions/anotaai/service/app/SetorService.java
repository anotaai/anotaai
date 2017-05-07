package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto.GrupoProdutoConstant;
import br.com.alinesolutions.anotaai.model.produto.Setor;
import br.com.alinesolutions.anotaai.model.produto.Setor.SetorConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class SetorService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Resource
	private SessionContext sessionContext;

	public ResponseEntity create(Setor setor) throws AppException {
		TypedQuery<Setor> q = null;
		Cliente cliente = appService.getCliente();
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			q = em.createNamedQuery(SetorConstant.FIND_BY_NOME_KEY, Setor.class);
			q.setParameter(GrupoProdutoConstant.FIELD_NOME, setor.getNome());
			q.setParameter(Constant.Entity.CLIENTE, cliente);
			setor = q.getSingleResult();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(Constant.Message.ENTIDADE_JA_CADASTRADA, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW, setor.getNome());
		} catch (NoResultException e) {
			setor.setCliente(cliente);
			em.persist(setor);
			Setor setorNovo = new Setor(setor.getId(), setor.getNome(), setor.getDescricao());
			responseEntity.setIsValid(Boolean.TRUE);
			responseEntity.setEntity(setorNovo);
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_GRAVADA_SUCESSO,
					TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, setor.getNome()));
		}
		return responseEntity;
	}

	public ResponseEntity deleteById(Long id) throws AppException {
		ResponseEntity entity = new ResponseEntity();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteSetor = null;
		Setor setor = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_SETOR_KEY, Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			clienteSetor = query.getSingleResult();
			entity.setIsValid(clienteSetor.equals(clienteLogado));
			if (entity.getIsValid()) {
				setor = em.find(Setor.class, id);
				em.remove(setor);
				entity.setMessages(new ArrayList<>());
				entity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_DELETADA_SUCESSO,
						TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, setor.getNome()));
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public List<Setor> listAll(Integer startPosition, Integer maxResult) throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<Setor> findAllQuery = em.createNamedQuery(SetorConstant.LIST_ALL_KEY, Setor.class);
		findAllQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Setor> results = findAllQuery.getResultList();
		return results;
	}

	public ResponseEntity update(Long id, Setor entity) throws AppException {
		Setor setor = null;
		AnotaaiMessage message = null;
		ResponseEntity responseEntity = new ResponseEntity();
		AppException appException = null;
		if (entity != null && id != null && id.equals(entity.getId())) {
			setor = em.find(Setor.class, id);
			mergeSetor(entity, setor);
			entity = em.merge(setor);
			message = new AnotaaiMessage(Constant.Message.ENTIDADE_EDITADA_SUCESSO, TipoMensagem.SUCCESS,
					Constant.Message.DEFAULT_TIME_VIEW, setor.getNome());
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(message);
		} else {
			responseEntity.addMessage(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR,
					Constant.Message.KEEP_ALIVE_TIME_VIEW);
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}

	private void mergeSetor(Setor oldSetor, Setor newSetor) {
		newSetor.setNome(oldSetor.getNome());
		newSetor.setDescricao(oldSetor.getDescricao());
	}

	public ResponseEntity findById(Long id) throws AppException {
		ResponseEntity entity = new ResponseEntity();
		Cliente cliente = appService.getCliente();
		try {
			TypedQuery<Setor> query = em.createNamedQuery(SetorConstant.FIND_BY_ID_KEY, Setor.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			query.setParameter(Constant.Entity.CLIENTE, cliente);
			entity.setEntity(query.getSingleResult());
			entity.setIsValid(Boolean.TRUE);
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

}
