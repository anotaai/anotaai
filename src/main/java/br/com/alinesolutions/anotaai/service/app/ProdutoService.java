package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Disponibilidade;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto;
import br.com.alinesolutions.anotaai.model.produto.ItemReceita;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.GeradorCodigoInterno;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class ProdutoService {

	@Inject
	private EntityManager em;

	@Inject
	@Email
	private AnotaaiSendMessage sender;

	@EJB
	private AppService appService;
	
	@EJB
	private ResponseUtil responseUtil;
	
	@EJB
	private GeradorCodigoInterno geradorCodigo;

	public ResponseEntity<Produto> findById(Long id) throws AppException {
		Cliente cliente = appService.getCliente();
		ResponseEntity<Produto> responseEntity = new ResponseEntity<>();
		TypedQuery<Produto> query = em.createNamedQuery(Produto.ProdutoConstant.EDIT_KEY, Produto.class);
		query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
		Produto produto = query.getSingleResult();
		responseEntity.setIsValid(produto.getCliente().equals(cliente));
		if (responseEntity.getIsValid()) {
			produto.setItensReceita(loadItensReceitaAction(produto));
			produto.setDiasDisponibilidade(loadDiasDisponibilidadeAction(produto));
			produto.setGrupos(loadGrupos(produto));
			responseEntity.setEntity(produto);
		} else {
			responseUtil.buildIllegalArgumentException(responseEntity);
		}
		return responseEntity;
	}

	public ResponseEntity<Produto> create(Produto produto) throws AppException {
		TypedQuery<Produto> q = null;
		Cliente cliente = appService.getCliente();
		ResponseEntity<Produto> responseEntity = new ResponseEntity<>();
		produto.setCodigo(geradorCodigo.gerarCodigoProduto(cliente));
		
		try {
			q = em.createNamedQuery(ProdutoConstant.PRODUTO_BY_CODIGO_BARRAS_KEY, Produto.class);
			q.setParameter(ProdutoConstant.FIELD_CODIGO, produto.getCodigoBarras());
			q.setParameter(Constant.Entity.CLIENTE, cliente);
			q.getSingleResult();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(IMessage.PRODUTO_JACADASTRADO, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW, produto.getCodigoBarras().toString());
		} catch (NoResultException e) {
			for (Disponibilidade disponibilidade : produto.getDiasDisponibilidade()) {
				disponibilidade.setProduto(produto);
			}
			for (ItemReceita itemReceita : produto.getItensReceita()) {
				itemReceita.setProduto(produto);
			}
			
			for (ProdutoGrupoProduto produtoGrupoProduto : produto.getGrupos()) {
				produtoGrupoProduto.setProduto(produto);
			}
			produto.setCliente(cliente);
			produto.setEhInsumo(produto.getEhInsumo() != null ? produto.getEhInsumo() : Boolean.FALSE);
			produto.setEstoque(new Estoque());
			produto.getEstoque().setProduto(produto);
			produto.getEstoque().setQuantidadeEstoque(0L);
			em.persist(produto);
			Produto produtoNovo = new Produto(produto.getId(), produto.getDescricao(), produto.getDescricaoResumida(),
					produto.getPrecoVenda(), produto.getIconClass(), produto.getEstoque().getId(), null, null,
					produto.getCodigo(), produto.getUnidadeMedida(),produto.getTipoArmazenamento());
			responseEntity.setIsValid(Boolean.TRUE);
			responseEntity.setEntity(produtoNovo);
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, produto.getDescricao()));
		}
		return responseEntity;
	}

	public ResponseEntity<Produto> deleteById(Long id) throws AppException {
		ResponseEntity<Produto> entity = new ResponseEntity<>();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteProduto = null;
		Produto produto = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_PRODUTO_KEY, Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			clienteProduto = query.getSingleResult();
			entity.setIsValid(clienteProduto.equals(clienteLogado));
			if (entity.getIsValid()) {
				produto = em.find(Produto.class, id);
				em.remove(produto);
				entity.setMessages(new ArrayList<>());
				entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO,
						TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, produto.getDescricao()));
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public List<Produto> searchByDescricao(String query, String insumoFilter, Integer startPosition, Integer maxResult, List<Long> excludes)
			throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<Produto> findProdutos = em.createNamedQuery(Produto.ProdutoConstant.LIST_SEARCH_BY_DESCRICAO_KEY,
				Produto.class);
		findProdutos.setParameter(Constant.Persistence.QUERY, query);
		findProdutos.setParameter(Constant.Entity.CLIENTE, cliente);
		// evita erro de excecucao, caso nao haja elementos na lista
		excludes.add(0L);
		findProdutos.setParameter(Constant.Persistence.LIST_PARAM, excludes);
		if (startPosition != null) {
			findProdutos.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findProdutos.setMaxResults(maxResult);
		}
		final List<Produto> results = findProdutos.getResultList();
		
		if("S".equals(insumoFilter)) {			
			Iterator<Produto> iterator = results.iterator();
			Produto produto = null;
			while (iterator.hasNext()) {
				produto = iterator.next();
				if(produto.getEhInsumo()){
					iterator.remove();
				}
			}
		}
		return results;
	}

	public List<Produto> searchByGrupoProduto(String query, Integer startPosition, Integer maxResult,
			Long idGrupoProduto) throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<Produto> findAllQuery = em.createNamedQuery(Produto.ProdutoConstant.LIST_SEARCH_BY_GRUPO_PRODUTO_KEY,
				Produto.class);
		findAllQuery.setParameter(Constant.Persistence.QUERY, query);
		findAllQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		GrupoProduto grupoProduto = new GrupoProduto();
		grupoProduto.setId(idGrupoProduto);
		findAllQuery.setParameter(Constant.Entity.GRUPO_PRODUTO, grupoProduto);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Produto> results = findAllQuery.getResultList();
		return results;
	}

	public ResponseEntity<Produto> listAll(Integer startPosition, Integer maxResult, String descricao) throws AppException {
		 
		Cliente cliente = appService.getCliente();
		TypedQuery<Produto> produtoQuery = null;
		
		if(!"".equals(descricao)) {
			produtoQuery =  em.createNamedQuery(Produto.ProdutoConstant.FIND_BY_NOME_KEY, Produto.class);
			produtoQuery.setParameter("descricao", descricao);
		} else  {
			produtoQuery =  em.createNamedQuery(Produto.ProdutoConstant.LIST_ALL_KEY, Produto.class);
		}
		
		produtoQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		ResponseEntity<Produto> responseEntity = new ResponseEntity<>();
		ResponseList<Produto> responseList = new ResponseList<Produto>();
		responseEntity.setList(responseList);
		if (startPosition != null) {
			produtoQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			produtoQuery.setMaxResults(maxResult);
		}
		final List<Produto> results = produtoQuery.getResultList();
		responseList.setItens(results);
		
		TypedQuery<Long> countAll = null;
		
		if(!"".equals(descricao)) {
			countAll  = em.createNamedQuery(Produto.ProdutoConstant.FIND_BY_NOME_COUNT, Long.class);
			countAll.setParameter("descricao", descricao);
		} else {
			countAll  = em.createNamedQuery(Produto.ProdutoConstant.LIST_ALL_COUNT, Long.class);
		}
		 
		countAll.setParameter(Constant.Entity.CLIENTE, cliente);
		responseList.setQtdTotalItens(countAll.getSingleResult());
		
		return responseEntity;
	}

	/**
	 * @param id
	 * @param entity
	 * @return
	 * @throws AppException
	 */
	public ResponseEntity<Produto> update(Long id, Produto entity) throws AppException {
		Produto produtoUpdate = em.find(Produto.class, entity.getId());
		updateDisponibilidade(produtoUpdate,entity);
		updateItensReceita(produtoUpdate,entity);
		updateGrupos(produtoUpdate,entity);
		produtoUpdate.setCodigo(entity.getCodigo());
		produtoUpdate.setEhInsumo(entity.getEhInsumo() != null ? entity.getEhInsumo() : Boolean.FALSE);
		produtoUpdate.setDescricao(entity.getDescricao());
		produtoUpdate.setDescricaoResumida(entity.getDescricaoResumida());
		produtoUpdate.setUnidadeMedida(entity.getUnidadeMedida());
		produtoUpdate.setPrecoVenda(entity.getPrecoVenda());
		produtoUpdate.setTipoArmazenamento(entity.getTipoArmazenamento());
		produtoUpdate.setEhInsumo(entity.getEhInsumo() != null ? entity.getEhInsumo() : Boolean.FALSE);
		em.merge(produtoUpdate);
		ResponseEntity<Produto> responseEntity = new ResponseEntity<>(entity);
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.addMessage(new AnotaaiMessage(IMessage.ENTIDADE_EDICAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, produtoUpdate.getDescricao()));
		return responseEntity;
	}
	
	private void updateDisponibilidade(Produto produtoUpdate, Produto entity) {

		Iterator<Disponibilidade> iterator = produtoUpdate.getDiasDisponibilidade().iterator();
		Disponibilidade disp = null;
		while (iterator.hasNext()) {
			disp = iterator.next();
			if (!entity.getDiasDisponibilidade().contains(disp)) {
				em.remove(disp);
				iterator.remove();
			}
		}
		for (Disponibilidade disponibilidade : entity.getDiasDisponibilidade()) {
			if (disponibilidade.getId() == null) {
				disponibilidade.setProduto(produtoUpdate);
				em.persist(disponibilidade);
				produtoUpdate.getDiasDisponibilidade().add(disponibilidade);
			}
		}

	}
	
	private void updateGrupos(Produto produtoUpdate, Produto entity) {

		if (produtoUpdate.getGrupos() == null || produtoUpdate.getGrupos().isEmpty()) {

			produtoUpdate.setGrupos(new ArrayList<>());

			for (ProdutoGrupoProduto produtoGrupoProduto : entity.getGrupos()) {
				produtoGrupoProduto.setProduto(new Produto(produtoUpdate.getId()));
				produtoUpdate.getGrupos().add(produtoGrupoProduto);
			}

		} else {

			Iterator<ProdutoGrupoProduto> iteratorGrupos = produtoUpdate.getGrupos().iterator();
			ProdutoGrupoProduto g = null;

			while (iteratorGrupos.hasNext()) {
				g = iteratorGrupos.next();
				if (!entity.getGrupos().contains(g)) {
					em.remove(g);
					iteratorGrupos.remove();
				}
			}

			for (ProdutoGrupoProduto produtoGrupoProduto : entity.getGrupos()) {
				if (produtoGrupoProduto.getId() == null) {
					produtoGrupoProduto.setProduto(new Produto(produtoUpdate.getId()));
					produtoUpdate.getGrupos().add(produtoGrupoProduto);
				} else {
					for (ProdutoGrupoProduto produtoGrupoProdutoUpdate : produtoUpdate.getGrupos()) {
						if (produtoGrupoProdutoUpdate.getId().equals(produtoGrupoProduto.getId())) {
							produtoGrupoProdutoUpdate.setEhPrincipal(produtoGrupoProduto.getEhPrincipal());
							break;
						}
					}
				}
			}

		}

	}
	
	private void updateItensReceita(Produto produtoUpdate, Produto entity) {

		if (produtoUpdate.getItensReceita() == null || produtoUpdate.getItensReceita().isEmpty()) {

			produtoUpdate.setItensReceita(new ArrayList<>());

			for (ItemReceita itemReceita : entity.getItensReceita()) {
				itemReceita.setProduto(produtoUpdate);
				produtoUpdate.getItensReceita().add(itemReceita);
			}

		} else {

			Iterator<ItemReceita> iteratorReceitas = produtoUpdate.getItensReceita().iterator();
			ItemReceita i = null;

			while (iteratorReceitas.hasNext()) {
				i = iteratorReceitas.next();
				if (!entity.getItensReceita().contains(i)) {
					em.remove(i);
					iteratorReceitas.remove();
				}
			}

			for (ItemReceita itemReceita : entity.getItensReceita()) {
				if (itemReceita.getId() == null) {
					itemReceita.setProduto(produtoUpdate);
					produtoUpdate.getItensReceita().add(itemReceita);
				}

			}
		}

	}

	public ResponseEntity<Produto> loadItensReceita(Produto produto) throws AppException {
		ResponseEntity<Produto> entity = new ResponseEntity<>();
		try {
			List<ItemReceita> itensReceita = loadItensReceitaAction(produto);
			produto.setItensReceita(itensReceita);
			entity.setEntity(produto);
			entity.setIsValid(Boolean.TRUE);
		} catch (Exception e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	private List<ItemReceita> loadItensReceitaAction(Produto produto) {
		Cliente cliente = appService.getCliente();
		TypedQuery<ItemReceita> query = em.createNamedQuery(ProdutoConstant.ITEM_RECEITA_BY_PRODUTO_KEY,
				ItemReceita.class);
		query.setParameter(Constant.Entity.PRODUTO, produto);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		List<ItemReceita> itensReceita = query.getResultList();
		for (ItemReceita itemReceita : itensReceita) {
			itemReceita.setProduto(produto);
		}
		return itensReceita;
	}
	
	private List<ProdutoGrupoProduto> loadGrupos(Produto produto) {
		Cliente cliente = appService.getCliente();
		TypedQuery<ProdutoGrupoProduto> query = em.createNamedQuery(ProdutoConstant.GRUPO_PRODUTO_BY_PRODUTO_KEY,ProdutoGrupoProduto.class);
		query.setParameter(Constant.Entity.PRODUTO, produto);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		List<ProdutoGrupoProduto> grupos = query.getResultList();
		return grupos;
	}

	public ResponseEntity<Produto> loadDisponibilidades(Produto produto) throws AppException {
		ResponseEntity<Produto> entity = new ResponseEntity<>();
		try {
			List<Disponibilidade> diasDisponibilidade = loadDiasDisponibilidadeAction(produto);
			produto.setDiasDisponibilidade(diasDisponibilidade);
			entity.setEntity(produto);
			entity.setIsValid(Boolean.TRUE);
		} catch (Exception e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	private List<Disponibilidade> loadDiasDisponibilidadeAction(Produto produto) {
		Cliente cliente = appService.getCliente();
		TypedQuery<Disponibilidade> query = em.createNamedQuery(ProdutoConstant.DISPONIBILIDADE_BY_PRODUTO_KEY,
				Disponibilidade.class);
		query.setParameter(Constant.Entity.PRODUTO, produto);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		List<Disponibilidade> diasDisponibilidade = query.getResultList();
		for (Disponibilidade disponibilidade : diasDisponibilidade) {
			disponibilidade.setProduto(produto);
		}
		return diasDisponibilidade;
	}

	public ResponseEntity<ItemReceita> addItemReceita(ItemReceita itemReceita) {
		em.persist(itemReceita);
		ResponseEntity<ItemReceita> responseEntity = new ResponseEntity<>(itemReceita);
		responseEntity.setIsValid(Boolean.TRUE);
		AnotaaiMessage message = new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS,
				Constant.App.DEFAULT_TIME_VIEW, itemReceita.getIngrediente().getDescricao());
		responseEntity.addMessage(message);
		return responseEntity;
	}

	public ResponseEntity<ItemReceita> editItemReceita(ItemReceita itemReceita) {
		ItemReceita old = em.find(ItemReceita.class, itemReceita.getId());
		old.setQuantidade(itemReceita.getQuantidade());
		em.merge(old);
		ResponseEntity<ItemReceita> responseEntity = new ResponseEntity<>(itemReceita);
		responseEntity.setIsValid(Boolean.TRUE);
		AnotaaiMessage message = new AnotaaiMessage(IMessage.ENTIDADE_EDICAO_SUCESSO, TipoMensagem.SUCCESS,
				Constant.App.DEFAULT_TIME_VIEW, itemReceita.getIngrediente().getDescricao());
		responseEntity.addMessage(message);
		return responseEntity;
	}

	public ResponseEntity<ItemReceita> deleteItemReceita(ItemReceita itemReceita) {
		ItemReceita old = em.find(ItemReceita.class, itemReceita.getId());
		em.remove(old);
		ResponseEntity<ItemReceita> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.TRUE);
		AnotaaiMessage message = new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO, TipoMensagem.SUCCESS,
				Constant.App.DEFAULT_TIME_VIEW, itemReceita.getIngrediente().getDescricao());
		responseEntity.addMessage(message);
		return responseEntity;
	}

}
