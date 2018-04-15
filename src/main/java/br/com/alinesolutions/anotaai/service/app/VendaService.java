package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.StatusItemVenda;
import br.com.alinesolutions.anotaai.metadata.model.domain.StatusVenda;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.BaseEntity.BaseEntityConstant;
import br.com.alinesolutions.anotaai.model.produto.Estoque;
import br.com.alinesolutions.anotaai.model.produto.Estoque.EstoqueConstant;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.produto.MovimentacaoProduto;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.ITipoVenda;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
import br.com.alinesolutions.anotaai.model.venda.IVendaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.IVendaFolha;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaAnonima;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaConsumidor;
import br.com.alinesolutions.anotaai.model.venda.VendaAnotadaConsumidor;
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
	private FolhaCadernetaService folhaCadernetaService;

	@Inject
	@Any
	private Event<IMovimentacao> eventMovimentacao;
	
	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	public ResponseEntity<VendaAVistaAnonima> createAnonymousSale(VendaAVistaAnonima vendaAVistaAnonima) throws AppException {
		validateSale(vendaAVistaAnonima);
		return null;
	}

	public ResponseEntity<Venda> createConsumerSale(VendaAVistaConsumidor vendaAVistaConsumidor) throws AppException {

		Caderneta caderneta = em.getReference(Caderneta.class, vendaAVistaConsumidor.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta().getId());
		Consumidor consumidor = em.getReference(Consumidor.class, vendaAVistaConsumidor.getFolhaCadernetaVenda().getFolhaCaderneta().getConsumidor().getId());

		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
		FolhaCadernetaVenda venda = new FolhaCadernetaVenda();
		venda.setFolhaCaderneta(folha);
		folha.getVendas().add(venda);
		vendaAVistaConsumidor.setFolhaCadernetaVenda(new FolhaCadernetaVenda());
		vendaAVistaConsumidor.getFolhaCadernetaVenda().getVenda().setDataVenda(new Date());
		vendaAVistaConsumidor.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		em.persist(venda);

		return null;
	}

	public ResponseEntity<VendaAnotadaConsumidor> createAppointmentBookSale(VendaAnotadaConsumidor vendaAnotada) throws AppException {
		
		validateRequired(vendaAnotada);
		Caderneta caderneta = em.getReference(Caderneta.class, vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta().getId());
		Consumidor consumidor = em.getReference(Consumidor.class, vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getConsumidor().getId());
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
		vendaAnotada.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		validateSale(vendaAnotada);
		updateItensVenda(vendaAnotada);
		vendaAnotada.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		Hibernate.initialize(folha.getVendas());
		folha.getVendas().add(vendaAnotada.getFolhaCadernetaVenda());
		vendaAnotada.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		vendaAnotada.getFolhaCadernetaVenda().getVenda().setDataVenda(new Date());
		vendaAnotada.getFolhaCadernetaVenda().getVenda().setStatusVenda(StatusVenda.FINALIZADA);
		createSale(vendaAnotada);
		ResponseEntity<VendaAnotadaConsumidor> responseEntity = new ResponseEntity<>();
		responseEntity.setEntity(vendaAnotada);
		return responseEntity;

	}

	private void updateItensVenda(VendaAnotadaConsumidor vendaAnotada) {
		vendaAnotada.getFolhaCadernetaVenda().getVenda().getProdutos().stream().forEach(itemVenda -> {
			Produto produto = em.find(Produto.class, itemVenda.getMovimentacaoProduto().getProduto().getId());
			itemVenda.setPrecoVenda(produto.getPrecoVenda());
			TypedQuery<Estoque> query = em.createNamedQuery(EstoqueConstant.FIND_BY_PRODUTO_KEY, Estoque.class);
			query.setParameter(BaseEntityConstant.FIELD_ID, produto.getId());
			Double precoCusto = null;
			try {
				Estoque estoque = query.getSingleResult();
				eventMovimentacao.fire(itemVenda);
				precoCusto = estoque.getPrecoCusto();
			} catch (NoResultException e) {
				precoCusto = null;
			}
			itemVenda.setVenda(vendaAnotada.getFolhaCadernetaVenda().getVenda());
			itemVenda.setPrecoCusto(precoCusto);
			itemVenda.setStatusItemVenda(StatusItemVenda.VENDIDO);
		});
	}
	
	private void validateRequired(VendaAnotadaConsumidor vendaAnotada) {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		responseEntity.setIsValid(vendaAnotada != null && vendaAnotada.getFolhaCadernetaVenda() != null && 
								  vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta() != null &&
								  vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta() != null &&
								  vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta().getId() != null &&
								  vendaAnotada.getFolhaCadernetaVenda().getVenda() != null);
		if (!responseEntity.getIsValid()) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_CAMPOSNAOINFORMADOS, TipoMensagem.ERROR, Constant.App.LONG_TIME_VIEW);
		}
		finalizeOrThrows(responseEntity);
	}

	private void validateSale(IVenda iVenda) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		if (iVenda.getVenda() == null) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_VENDA, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
		} else if (iVenda.getVenda().getProdutos() == null || iVenda.getVenda().getProdutos().isEmpty()) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_ITEMVENDA, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
		} else {
			try {
				validateProcucts(iVenda.getVenda().getProdutos());
			} catch (AppException e) {
				mergeErrorMessages(responseEntity, e.getResponseEntity());
			}
		}
		finalizeOrThrows(responseEntity);
	}

	private void finalizeOrThrows(ResponseEntity<? extends BaseEntity<?, ?>> responseEntity) throws AppException {
		if (responseEntity.getIsValid() != null && !responseEntity.getIsValid()) {
			throw new AppException(responseEntity);
		}
	}

	private ResponseEntity<? extends BaseEntity<?, ?>> buildResponseEntity() {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	private void mergeErrorMessages(ResponseEntity<? extends BaseEntity<?, ?>> responseEntity, ResponseEntity<?> target) {
		responseEntity.setIsValid(Boolean.FALSE);
		responseEntity.getMessages().addAll(target.getMessages());
		
	}

	private void validateProcucts(List<ItemVenda> produtos) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		AtomicInteger index = new AtomicInteger();
		produtos.stream().forEach(itemVenda -> {
			String indexStr = String.valueOf(index.getAndIncrement());
			if (itemVenda.getMovimentacaoProduto() == null) {
				responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_MOVIMENTACAOPRODUTO, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW, indexStr);
				responseEntity.setIsValid(Boolean.FALSE);
			} else {
				try {
					validateProductMovement(itemVenda.getMovimentacaoProduto(), indexStr);
				} catch (AppException e) {
					mergeErrorMessages(responseEntity, e.getResponseEntity());
				}
			}
		});
		finalizeOrThrows(responseEntity);
	}

	private void validateProductMovement(MovimentacaoProduto movimentacaoProduto, String index) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		if (movimentacaoProduto.getProduto() == null) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_PRODUTO, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW, index);
			responseEntity.setIsValid(Boolean.FALSE);
		} else {
			Produto produto = movimentacaoProduto.getProduto();
			try {
				TypedQuery<Cliente> query = em.createNamedQuery(ProdutoConstant.CLIENTE_BY_PRODUTO_KEY, Cliente.class);
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, appService.getCliente());
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produto.getId());
				Cliente cliente = query.getSingleResult();
				if (!cliente.equals(appService.getCliente())) {
					throw new NoResultException();
				} else if (movimentacaoProduto.getQuantidade() == null || movimentacaoProduto.getQuantidade() <= 0) {
					responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_QUANTIDADE, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW, produto.getDescricao() != null ? produto.getDescricao() : "");
					responseEntity.setIsValid(Boolean.FALSE);
				}
			} catch (NoResultException e) {
				//TODO ANOTAAI disparar evento de suspeita de fraude (venda de produto inexistente ou associado a outro vendedor)
				responseEntity.addMessage(IMessage.PRODUTO_NAOCADASTRADO, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW, produto.getDescricao() != null ? produto.getDescricao() : "");
				responseEntity.setIsValid(Boolean.FALSE);
			}
		}
		finalizeOrThrows(responseEntity);
	}
	
	private void validateSale(IVendaCaderneta vendaAnonima) {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		try {
			validateSale((IVenda)vendaAnonima);
		} catch (AppException e) {
			mergeErrorMessages(responseEntity, e.getResponseEntity());
		}
		finalizeOrThrows(responseEntity);
	}

	private void validateSale(IVendaFolha iVenda) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		try {
			validateSale(iVenda.getFolhaCadernetaVenda());
		} catch (AppException e) {
			mergeErrorMessages(responseEntity, e.getResponseEntity());
		}
		Consumidor consumidor = iVenda.getFolhaCadernetaVenda().getFolhaCaderneta().getConsumidor();
		if (consumidor == null) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_CONSUMIDOR, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
		} else {
			try {
				TypedQuery<Cliente> query = em.createNamedQuery(Consumidor.ConsumidorConstant.FIND_CLIENTE_KEY, Cliente.class);
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, appService.getCliente());
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CONSUMIDOR, consumidor);
				Cliente clienteStorage = query.getSingleResult();
				if (!clienteStorage.equals(appService.getCliente())) {
					throw new NoResultException();
				}
			} catch (NoResultException e) {
				//TODO ANOTAAI disparar evento de suspeita de fraude (venda de produto inexistente ou associado a outro vendedor)
				responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_CONSUMIDOR, TipoMensagem.ERROR, Constant.App.DEFAULT_TIME_VIEW);
				responseEntity.setIsValid(Boolean.FALSE);
			}
		}
		finalizeOrThrows(responseEntity);
	}

	private void createSale(ITipoVenda tipoVenda) {
		em.persist(tipoVenda);
	}

}
