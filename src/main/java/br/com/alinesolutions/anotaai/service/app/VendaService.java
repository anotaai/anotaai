package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Disponibilidade;
import br.com.alinesolutions.anotaai.model.produto.ItemReceita;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class VendaService {

	@Inject
	private EntityManager em;

	@Inject
	@Email
	private AnotaaiSendMessage sender;

	@EJB
	private AppService appService;
	
	@EJB
	private ResponseUtil responseUtil;

	public ResponseEntity<Venda> findById(Long id) throws AppException {
		Cliente cliente = appService.getCliente();
		ResponseEntity<Venda> responseEntity = new ResponseEntity<>();
		TypedQuery<Venda> query = em.createNamedQuery(Venda.VendaConstant.FIND_BY_ID_KEY, Venda.class);
		query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
		query.setParameter(Constant.Entity.CLIENTE, cliente);
		Venda venda = query.getSingleResult();
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setEntity(venda);
		return responseEntity;
	}

	public ResponseEntity<Produto> create(Produto produto) throws AppException {
		TypedQuery<Produto> q = null;
		Cliente cliente = appService.getCliente();
		ResponseEntity<Produto> responseEntity = new ResponseEntity<>();
		try {
			q = em.createNamedQuery(ProdutoConstant.PRODUTO_BY_CODIGO_KEY, Produto.class);
			q.setParameter(ProdutoConstant.FIELD_CODIGO, produto.getCodigo());
			q.setParameter(Constant.Entity.CLIENTE, cliente);
			q.getSingleResult();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(Constant.Message.ENTIDADE_JA_CADASTRADA, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW, produto.getCodigo().toString());
		} catch (NoResultException e) {
			for (Disponibilidade disponibilidade : produto.getDiasDisponibilidade()) {
				disponibilidade.setProduto(produto);
			}
			for (ItemReceita itemReceita : produto.getItensReceita()) {
				itemReceita.setProduto(produto);
			}
			produto.setCliente(cliente);
			produto.setEhInsumo(produto.getEhInsumo() != null ? produto.getEhInsumo() : Boolean.FALSE);
			produto.getEstoque().setProduto(produto);
			produto.getEstoque().setQuantidadeEstoque(0L);
			em.persist(produto);
			responseEntity.setIsValid(Boolean.TRUE);
			responseEntity.setEntity(produto.clone());
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_GRAVADA_SUCESSO,
					TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, produto.getDescricao()));
		}
		return responseEntity;
	}

	public ResponseEntity<?> deleteById(Long id) throws AppException {
		ResponseEntity<?> entity = new ResponseEntity<>();
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
				entity.getMessages().add(new AnotaaiMessage(Constant.Message.ENTIDADE_DELETADA_SUCESSO,
						TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, produto.getDescricao()));
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public List<Venda> listAll(Date dataInicial, Date dataFinal) throws AppException {
		Cliente cliente = appService.getCliente();
		TypedQuery<Venda> findAllQuery = em.createNamedQuery(Venda.VendaConstant.LIST_BY_PERIOD_KEY, Venda.class);
		findAllQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		findAllQuery.setParameter("startDate", dataInicial);
		findAllQuery.setParameter("endDate", dataFinal);
		final List<Venda> results = findAllQuery.getResultList();
		for (Venda venda : results) {
			Hibernate.initialize(venda.getProdutos());
		}
		return results;
	}

}
