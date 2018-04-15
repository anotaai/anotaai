package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class ProdutoGrupoProdutoService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Context
	private HttpServletRequest request;

	@Resource
	private SessionContext sessionContext;

	public ResponseEntity<ProdutoGrupoProduto> create(ProdutoGrupoProduto produtoGrupoProduto) throws AppException {
		Cliente cliente = appService.getCliente();
		ResponseEntity<ProdutoGrupoProduto> responseEntity = new ResponseEntity<>();

		validarProdutoGrupoProduto(cliente, produtoGrupoProduto);
		em.persist(produtoGrupoProduto);

		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setEntity(produtoGrupoProduto);
		responseEntity.addMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, produtoGrupoProduto.getProduto().getDescricao());
		return responseEntity;
	}

	private void validarProdutoGrupoProduto(Cliente cliente, ProdutoGrupoProduto produtoGrupoProduto)
			throws AppException {
		try {
			TypedQuery<Produto> queryProduto = null;
			TypedQuery<GrupoProduto> queryGrupoProduto = null;
			queryProduto = em.createNamedQuery(Produto.ProdutoConstant.PRODUTO_BY_ID_KEY, Produto.class);
			queryProduto.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produtoGrupoProduto.getProduto().getId());
			queryProduto.setParameter(Constant.Entity.CLIENTE, cliente);
			queryProduto.getSingleResult();
			queryGrupoProduto = em.createNamedQuery(GrupoProduto.GrupoProdutoConstant.FIND_BY_ID_KEY,
					GrupoProduto.class);
			queryGrupoProduto.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID,
					produtoGrupoProduto.getGrupoProduto().getId());
			queryGrupoProduto.setParameter(Constant.Entity.CLIENTE, cliente);
			queryGrupoProduto.getSingleResult();
		} catch (NoResultException e) {
			ResponseEntity<?> responseEntity = new ResponseEntity<>();
			responseEntity.addMessage(IMessage.ERRO_ILLEGALARGUMENT, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
			throw new AppException(responseEntity);
		}

	}

	public ResponseEntity<ProdutoGrupoProduto> deleteById(Long id) throws AppException {
		ResponseEntity<ProdutoGrupoProduto> entity = new ResponseEntity<>();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteGrupoProduto = null;
		ProdutoGrupoProduto produtoGrupoProduto = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_PRODUTO_GRUPO_PRODUTO_KEY,
					Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			clienteGrupoProduto = query.getSingleResult();
			entity.setIsValid(clienteGrupoProduto.equals(clienteLogado));
			if (entity.getIsValid()) {
				produtoGrupoProduto = em.find(ProdutoGrupoProduto.class, id);
				em.remove(produtoGrupoProduto);
				entity.addMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, produtoGrupoProduto.getProduto().getDescricao());
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

}
