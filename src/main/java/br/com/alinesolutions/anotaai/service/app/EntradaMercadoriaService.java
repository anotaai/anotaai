package br.com.alinesolutions.anotaai.service.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria;
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria.EntradaMercadoriaConstant;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;
import br.com.alinesolutions.anotaai.model.produto.ItemEntrada;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.GeradorCodigoInterno;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class EntradaMercadoriaService {
	
	@EJB
	private AppService appService;

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;
	
	@Inject
	@Any
	private Event<ItemEntrada> event;
	
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

	private void loadItensEntrada(EntradaMercadoria entradaMercadoria) {

		TypedQuery<ItemEntrada> query = em.createNamedQuery(EntradaMercadoriaConstant.ITEM_ENTRADA_BY_ENTRADA_KEY,ItemEntrada.class);
		query.setParameter("entradaMercadoria", entradaMercadoria);
		List<ItemEntrada> itensEntrada = query.getResultList();
		entradaMercadoria.setItens(itensEntrada);

	}

	public ResponseEntity<EntradaMercadoria> listAll(Integer startPosition, Integer maxResult, String nome,String dataEntradaStr) {

		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		TypedQuery<EntradaMercadoria> entradaMercadoriaQuery = null;
		Date dataEntrada = null;

		if (!"".equals(dataEntradaStr)) {

			DateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");

			try {
				dataEntrada = formatador.parse(dataEntradaStr);
			} catch (ParseException e) {
				responseEntity.setIsValid(Boolean.FALSE);
				responseEntity.addMessage(IMessage.ILLEGAL_ARGUMENT, TipoMensagem.ERROR, IMessage.KEEP_ALIVE_TIME_VIEW);
				return responseEntity;
			}
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
		entradaMercadoria.setDataEntrada(appService.addDayHtml5Date(entradaMercadoria.getDataEntrada()));
		updateItemEntrada(entradaMercadoria);
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		entradaMercadoria.setCodigo(geradorCodigo.gerarCodigoEntradaMercadoria(appService.getCliente()));
		entradaMercadoria.setCliente(appService.getCliente());
		em.persist(entradaMercadoria);
		publish(entradaMercadoria.getItens());
		EntradaMercadoria e = new EntradaMercadoria(entradaMercadoria.getId());
		responseEntity.setEntity(e);	
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setMessages(new ArrayList<>());
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ENTRADA_MERCADORIA));
		return responseEntity;
	}
	
	private void publish(List<ItemEntrada> itens) {
		itens.stream().forEach(item -> {
			event.fire(item);
		});
	}

	public void removerEstoque(ItemEntrada itemEntrada) throws AppException {
		TypedQuery<Estoque> estoqueQuery = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
		estoqueQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, itemEntrada.getMovimentacaoProduto().getProduto().getId());
		Estoque estoque = estoqueQuery.getSingleResult();
		//TODO - implementar extorno de produto
		em.merge(estoque);
	}
	
	
	public void updateItemEntrada(EntradaMercadoria entradaMercadoria) {
		
		entradaMercadoria.getItens().stream().forEach(e -> {
			e.setEntradaMercadoria(entradaMercadoria);
		});
		
	}
	 

	public ResponseEntity<EntradaMercadoria> update(Long id, EntradaMercadoria entradaMercadoria) throws AppException {
		
		entradaMercadoria.setDataEntrada(appService.addDayHtml5Date(entradaMercadoria.getDataEntrada()));
		
		ResponseEntity<EntradaMercadoria> responseEntity = new ResponseEntity<>();
		
		EntradaMercadoria entradaMercadoriaUpdate = em.find(EntradaMercadoria.class, entradaMercadoria.getId());
		entradaMercadoriaUpdate.setDataEntrada(entradaMercadoria.getDataEntrada());
		Iterator<ItemEntrada> iterator = entradaMercadoriaUpdate.getItens().iterator();
		
		ItemEntrada itemEntradaRemove = null;
		
		while (iterator.hasNext()) {
			itemEntradaRemove = iterator.next();
			if (!entradaMercadoria.getItens().contains(itemEntradaRemove)) {
				removerEstoque(itemEntradaRemove);
				em.remove(itemEntradaRemove);
				iterator.remove();
			}
		}
		
		
		updateItemEntrada(entradaMercadoria);
		
		publish(entradaMercadoria.getItens());
		
		entradaMercadoria.setCliente(appService.getCliente());
				
		entradaMercadoriaUpdate.setDataEntrada(entradaMercadoria.getDataEntrada());
	
		em.merge(entradaMercadoriaUpdate);
		
		EntradaMercadoria e = new EntradaMercadoria(entradaMercadoria.getId());
		responseEntity.setEntity(e);	
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setMessages(new ArrayList<>());
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ENTRADA_MERCADORIA));
		
		return responseEntity;
	}

	
	public ResponseEntity<EntradaMercadoria> deleteById(Long id) throws AppException {
		ResponseEntity<EntradaMercadoria> entity = new ResponseEntity<>();

		try {
			EntradaMercadoria entradaMercadoria  = em.find(EntradaMercadoria.class, id);
			entity.setIsValid(Boolean.TRUE);
			
			if(entradaMercadoria.getItens() != null && !entradaMercadoria.getItens().isEmpty()) {
				for (ItemEntrada itemEntrada : entradaMercadoria.getItens()) {
					removerEstoque(itemEntrada);
				}
			}
			em.remove(entradaMercadoria);
			entity.setMessages(new ArrayList<>());
			entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO,TipoMensagem.SUCCESS, IMessage.DEFAULT_TIME_VIEW, EntradaMercadoriaConstant.ENTRADA_MERCADORIA));
			 
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

}
