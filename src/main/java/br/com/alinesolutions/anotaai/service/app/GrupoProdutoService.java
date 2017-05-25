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
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto.GrupoProdutoConstant;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto.ProdutoGrupoProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class GrupoProdutoService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Resource
	private SessionContext sessionContext;

	public ResponseEntity<GrupoProduto> create(GrupoProduto grupoProduto) throws AppException {
		TypedQuery<GrupoProduto> q = null;
		Cliente cliente = appService.getCliente();
		ResponseEntity<GrupoProduto> responseEntity = new ResponseEntity<>();
		try {
			validarGrupoProduto(grupoProduto);
			q = em.createNamedQuery(GrupoProdutoConstant.FIND_BY_NOME_KEY, GrupoProduto.class);
			q.setParameter(GrupoProdutoConstant.FIELD_NOME, grupoProduto.getNome());
			q.setParameter(Constant.Entity.SETOR, grupoProduto.getSetor());
			q.setParameter(Constant.Entity.CLIENTE, cliente);
			grupoProduto = q.getSingleResult();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(new AnotaaiMessage(Constant.Message.ENTIDADE_JA_CADASTRADA,
					TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW, grupoProduto.getNome()));
		} catch (NoResultException e) {
			grupoProduto.getSetor().setCliente(cliente);
			em.persist(grupoProduto);
			GrupoProduto grupoNovo = new GrupoProduto(grupoProduto.getId(), grupoProduto.getNome(),
					grupoProduto.getDescricao());
			responseEntity.setIsValid(Boolean.TRUE);
			responseEntity.setEntity(grupoNovo);
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_GRAVADA_SUCESSO,
					TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, grupoProduto.getNome()));
		}
		return responseEntity;
	}

	private void validarGrupoProduto(GrupoProduto grupoProduto) throws AppException {
		ResponseEntity<GrupoProduto> responseEntity = new ResponseEntity<>();
		if (grupoProduto.getDescricao() == null || grupoProduto.getDescricao().equals("")) {
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(new AnotaaiMessage(Constant.Message.CAMPO_OBRIGATORIO_NAO_INFORMADO, TipoMensagem.ERROR,
					Constant.Message.DEFAULT_TIME_VIEW, "Nome"));
		}
		if (grupoProduto.getSetor() == null) {
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(new AnotaaiMessage(Constant.Message.CAMPO_OBRIGATORIO_NAO_INFORMADO, TipoMensagem.ERROR,
					Constant.Message.DEFAULT_TIME_VIEW, "Setor"));
		}
		if (!responseEntity.getIsValid()) {
			responseEntity.setIsValid(Boolean.FALSE);
			throw new AppException(responseEntity);
		}
	}

	public ResponseEntity<GrupoProduto> deleteById(Long id) throws AppException {
		ResponseEntity<GrupoProduto> entity = new ResponseEntity<>();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteGrupoProduto = null;
		GrupoProduto grupoProduto = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_GRUPO_PRODUTO_KEY,
					Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			clienteGrupoProduto = query.getSingleResult();
			entity.setIsValid(clienteGrupoProduto.equals(clienteLogado));
			if (entity.getIsValid()) {
				grupoProduto = em.find(GrupoProduto.class, id);
				em.remove(grupoProduto);
				entity.setMessages(new ArrayList<>());
				entity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_DELETADA_SUCESSO,
						TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, grupoProduto.getNome()));
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public ResponseEntity<GrupoProduto> deleteProdutoGrupoProdutoById(Long idProdutoGrupoProduto) throws AppException {
		ResponseEntity<GrupoProduto> entity = new ResponseEntity<>();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteGrupoProduto = null;
		ProdutoGrupoProduto produtoGrupoProduto = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_PRODUTO_GRUPO_PRODUTO_KEY,
					Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, idProdutoGrupoProduto);
			clienteGrupoProduto = query.getSingleResult();
			entity.setIsValid(clienteGrupoProduto.equals(clienteLogado));
			if (entity.getIsValid()) {
				produtoGrupoProduto = em.find(ProdutoGrupoProduto.class, produtoGrupoProduto);
				em.remove(produtoGrupoProduto);
				entity.setMessages(new ArrayList<>());
				entity.getMessages()
						.add(new AnotaaiMessage(Constant.Message.ENTIDADE_DELETADA_SUCESSO, TipoMensagem.SUCCESS,
								Constant.Message.DEFAULT_TIME_VIEW, produtoGrupoProduto.getProduto().getDescricao()));
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public List<GrupoProduto> listAll(Integer startPosition, Integer maxResult) throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<GrupoProduto> findAllQuery = em.createNamedQuery(GrupoProdutoConstant.LIST_ALL_KEY,
				GrupoProduto.class);
		findAllQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<GrupoProduto> results = findAllQuery.getResultList();
		return results;
	}

	public ResponseEntity<GrupoProduto> update(Long id, GrupoProduto entity) throws AppException {
		GrupoProduto grupoProduto = null;
		AnotaaiMessage message = null;
		ResponseEntity<GrupoProduto> responseEntity = new ResponseEntity<>();
		AppException appException = null;
		if (entity != null && id != null && id.equals(entity.getId())) {
			grupoProduto = em.find(GrupoProduto.class, id);
			mergeGrupoProduto(entity, grupoProduto);
			entity = em.merge(grupoProduto);
			message = new AnotaaiMessage(Constant.Message.ENTIDADE_EDITADA_SUCESSO, TipoMensagem.SUCCESS,
					Constant.Message.DEFAULT_TIME_VIEW, grupoProduto.getNome());
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(message);
		} else {
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(new AnotaaiMessage(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR,
					Constant.Message.KEEP_ALIVE_TIME_VIEW));
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}

	private void mergeGrupoProduto(GrupoProduto oldGrupoProduto, GrupoProduto newGrupoProduto) {
		newGrupoProduto.setNome(oldGrupoProduto.getNome());
		newGrupoProduto.setDescricao(oldGrupoProduto.getDescricao());
	}

	private List<ProdutoGrupoProduto> carregarProduots(GrupoProduto grupoProduto) throws AppException {
		Cliente cliente = appService.getCliente();
		List<ProdutoGrupoProduto> produtos = new ArrayList<>();
		try {
			TypedQuery<ProdutoGrupoProduto> query = em.createNamedQuery(ProdutoGrupoProdutoConstant.ALL_BY_GRUPO_KEY,
					ProdutoGrupoProduto.class);
			query.setParameter(ProdutoGrupoProdutoConstant.FIELD_GRUPO_PRODUTO, grupoProduto);
			query.setParameter(Constant.Entity.CLIENTE, cliente);
			produtos.addAll(query.getResultList());
			grupoProduto.setProdutos(produtos);
		} catch (Exception e) {
			ResponseEntity<GrupoProduto> entity = new ResponseEntity<>();
			entity.setIsValid(Boolean.FALSE);
			responseUtil.buildIllegalArgumentException(entity);
		}
		return produtos;
	}

	public List<GrupoProduto> recuperarPorDescricao(String nomeSetor, Integer startPosition, Integer maxResult, Long idSetor) throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<GrupoProduto> query = em.createNamedQuery(GrupoProdutoConstant.ALL_BY_SETOR_KEY, GrupoProduto.class);
		query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, idSetor);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		query.setParameter(GrupoProdutoConstant.FIELD_NOME, new StringBuilder("%").append(nomeSetor).append("%").toString());
		List<GrupoProduto> produtos = query.getResultList();
		return produtos;
	}

	public ResponseEntity<GrupoProduto> findById(Long id) throws AppException {
		ResponseEntity<GrupoProduto> entity = new ResponseEntity<>();
		Cliente cliente = appService.getCliente();
		try {
			TypedQuery<GrupoProduto> query = em.createNamedQuery(GrupoProdutoConstant.FIND_BY_ID_KEY, GrupoProduto.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			query.setParameter(Constant.Entity.CLIENTE, cliente);
			GrupoProduto grupoProduto = query.getSingleResult();
			List<ProdutoGrupoProduto> produtos = carregarProduots(grupoProduto);
			grupoProduto.setProdutos(produtos);
			entity.setEntity(grupoProduto);
			entity.setIsValid(Boolean.TRUE);
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

}
