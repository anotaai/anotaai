package br.com.alinesolutions.anotaai.service.app;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.infra.AnotaaiUtil;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.MotivoDevolucao;
import br.com.alinesolutions.anotaai.model.produto.Devolucao;
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria;
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria.EntradaMercadoriaConstant;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemDevolucao;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.GeradorCodigoInterno;
import br.com.alinesolutions.anotaai.service.ResponseUtil;

@Stateless
public class EntradaMercadoriaService {
	
	@EJB
	private AppService appService;

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;
	
	@Inject
	@Any
	private Event<IMovimentacao> eventMovimentacao;
	
	@EJB
	private GeradorCodigoInterno geradorCodigo;

	@EJB
	private ResponseUtil responseUtil;

	public ResponseEntity<EntradaMercadoria> findById(Long id) throws AppException {
		ResponseEntity<EntradaMercadoria> entity = new ResponseEntity<>();
		try {
			TypedQuery<EntradaMercadoria> query = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_ID_KEY,EntradaMercadoria.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			entity.setEntity(query.getSingleResult());
			entity.setIsValid(Boolean.TRUE);
			loadItensEntrada(entity.getEntity());
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}
	
	public ResponseEntity<EntradaMercadoria> getCommodityForDelete(Long id) throws AppException {
		 //TODO - verificação de perfil
		return findById(id);
	}

	private void loadItensEntrada(EntradaMercadoria entradaMercadoria) {
		TypedQuery<ItemEntrada> query = em.createNamedQuery(EntradaMercadoriaConstant.ITEM_ENTRADA_BY_ENTRADA_KEY,ItemEntrada.class);
		query.setParameter("entradaMercadoria", entradaMercadoria);
		List<ItemEntrada> itensEntrada = query.getResultList();
		entradaMercadoria.setItens(itensEntrada);
	}

	public ResponseEntity<EntradaMercadoria> listAll(Integer startPosition, Integer maxResult, String nome, String dataEntradaStr) {
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		TypedQuery<EntradaMercadoria> entradaMercadoriaQuery = null;
		ZonedDateTime dataEntrada = null;
		if (!"".equals(dataEntradaStr)) {
			dataEntrada = AnotaaiUtil.getInstance().toZonedDateTime(dataEntradaStr, ZoneId.systemDefault());
		}
		if (!"".equals(nome) && dataEntrada == null) {
			entradaMercadoriaQuery = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_NOME_KEY,EntradaMercadoria.class);
			entradaMercadoriaQuery.setParameter("descricao", nome);
		} else if ("".equals(nome) && dataEntrada != null) {
			entradaMercadoriaQuery = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_DATE_KEY,EntradaMercadoria.class);
			entradaMercadoriaQuery.setParameter("dataEntrada", dataEntrada);
		} else if (!"".equals(nome) && dataEntrada != null) {
			entradaMercadoriaQuery = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_KEY,EntradaMercadoria.class);
			entradaMercadoriaQuery.setParameter("descricao", nome);
			entradaMercadoriaQuery.setParameter("dataEntrada", dataEntrada);
		} else {
			entradaMercadoriaQuery = em.createNamedQuery(EntradaMercadoriaConstant.LIST_ALL_KEY,EntradaMercadoria.class);
		}
		ResponseList<EntradaMercadoria> responseList = new ResponseList<EntradaMercadoria>();
		responseEntity.setList(responseList);
		if (startPosition != null) {
			entradaMercadoriaQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			entradaMercadoriaQuery.setMaxResults(maxResult);
		}
		final List<EntradaMercadoria> results = entradaMercadoriaQuery.getResultList();
		responseList.setItens(results);
		TypedQuery<Long> countAll = null;
		if (!"".equals(nome) && dataEntrada == null) {
			countAll = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_NOME_COUNT, Long.class);
			countAll.setParameter("descricao", nome);
		} else if ("".equals(nome) && dataEntrada != null) {
			countAll = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_DATE_COUNT, Long.class);
			countAll.setParameter("dataEntrada", dataEntrada);
		} else if (!"".equals(nome) && dataEntrada != null) {
			countAll = em.createNamedQuery(EntradaMercadoriaConstant.FIND_BY_NOME_AND_DATE_COUNT, Long.class);
			countAll.setParameter("descricao", nome);
			countAll.setParameter("dataEntrada", dataEntrada);
		} else {
			countAll = em.createNamedQuery(EntradaMercadoriaConstant.LIST_ALL_COUNT, Long.class);
		}
		responseList.setQtdTotalItens(countAll.getSingleResult());
		return responseEntity;
	}

	public ResponseEntity<EntradaMercadoria> create(EntradaMercadoria entradaMercadoria) throws AppException {
		//entradaMercadoria.setDataEntrada(appService.addDayHtml5Date(entradaMercadoria.getDataEntrada()));
		updateItemEntrada(entradaMercadoria);
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		entradaMercadoria.setCodigo(geradorCodigo.gerarCodigoEntradaMercadoria(appService.getCliente()));
		entradaMercadoria.setCliente(appService.getCliente());
		em.persist(entradaMercadoria);
		publish(entradaMercadoria.getItens());
		EntradaMercadoria e = new EntradaMercadoria(entradaMercadoria.getId());
		responseEntity.setEntity(e);
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ENTRADA_MERCADORIA));
		return responseEntity;
	}
	
	private void publish(List<ItemEntrada> itens) {
		itens.stream().forEach(item -> {
			eventMovimentacao.fire(item);
		});
	}

	public void updateItemEntrada(EntradaMercadoria entradaMercadoria) {
		entradaMercadoria.getItens().stream().forEach(e -> {
			e.setEntradaMercadoria(entradaMercadoria);
		});
	}

	public ResponseEntity<EntradaMercadoria> update(Long id, EntradaMercadoria entradaMercadoria) throws AppException {
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		EntradaMercadoria entradaMercadoriaUpdate = em.find(EntradaMercadoria.class, entradaMercadoria.getId());
		entradaMercadoriaUpdate.setDataEntrada(entradaMercadoria.getDataEntrada());
		updateItemEntrada(entradaMercadoria);
		publish(entradaMercadoria.getItens());
		entradaMercadoria.setCliente(appService.getCliente());
		entradaMercadoriaUpdate.setDataEntrada(entradaMercadoria.getDataEntrada());
		em.merge(entradaMercadoriaUpdate);
		EntradaMercadoria e = new EntradaMercadoria(entradaMercadoria.getId());
		responseEntity.setEntity(e);	
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ENTRADA_MERCADORIA));
		return responseEntity;
	}
	
	public ResponseEntity<EntradaMercadoria> rejectCommodity(EntradaMercadoria entradaMercadoria) throws AppException {
		Devolucao devolucao = new Devolucao();
		devolucao.setProdutos(new ArrayList<>());
		devolucao.setData(AnotaaiUtil.getInstance().now());
		devolucao.setCliente(appService.getCliente());
		entradaMercadoria.getItens().forEach(itemEntrada -> {
			final ItemDevolucao itemDevolucao = new ItemDevolucao();
			itemDevolucao.setDevolucao(devolucao);
			itemDevolucao.setMotivo(MotivoDevolucao.ERRO_LANCAMENTO);
			//TODO ANOTAAI continuar implementacao
			devolucao.getProdutos().add(itemDevolucao);
			eventMovimentacao.fire(itemEntrada);
		});
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		responseEntity.setEntity(new EntradaMercadoria(entradaMercadoria.getId()));
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO,TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ITEM_MERCADORIA));
		return responseEntity;
	}

}
