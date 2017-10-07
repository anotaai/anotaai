package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.produto.MovimentacaoProduto;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
import br.com.alinesolutions.anotaai.model.venda.IVendaAnonima;
import br.com.alinesolutions.anotaai.model.venda.IVendaConsumidor;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaAnonima;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaConsumidor;
import br.com.alinesolutions.anotaai.model.venda.VendaAnotadaConsumidor;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;

@Stateless
public class VendaService {

	@Inject
	private EntityManager em;

	@Inject
	@Email
	private AnotaaiSendMessage sender;

	@EJB
	private FolhaCadernetaService folhaCadernetaService;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	public ResponseEntity<VendaAVistaAnonima> createAnonymousSale(VendaAVistaAnonima vendaAVistaAnonima) throws AppException {
		validateSale(vendaAVistaAnonima);
		return null;
	}

	public ResponseEntity<Venda> createConsumerSale(VendaAVistaConsumidor vendaAVistaConsumidor) throws AppException {
		Caderneta caderneta = vendaAVistaConsumidor.getFolhaCaderneta().getCaderneta();
		Consumidor consumidor = vendaAVistaConsumidor.getFolhaCaderneta().getConsumidor();
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
		FolhaCadernetaVenda venda = new FolhaCadernetaVenda();
		venda.setFolhaCaderneta(folha);
		folha.getVendas().add(venda);
		vendaAVistaConsumidor.getVenda().setDataVenda(new Date());
		vendaAVistaConsumidor.setFolhaCaderneta(folha);
		venda.setVenda(vendaAVistaConsumidor);
		em.persist(venda);

		return null;
	}

	public ResponseEntity<VendaAnotadaConsumidor> createAppointmentBookSale(VendaAnotadaConsumidor vendaAnotada) throws AppException {
		
		Caderneta caderneta = em.getReference(Caderneta.class, vendaAnotada.getFolhaCaderneta().getCaderneta().getId());
		Consumidor consumidor = em.getReference(Consumidor.class, vendaAnotada.getFolhaCaderneta().getConsumidor().getId());

		vendaAnotada.getFolhaCaderneta().setCaderneta(caderneta);
		vendaAnotada.getFolhaCaderneta().setConsumidor(consumidor);
		validateSale((IVendaConsumidor)vendaAnotada);
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
		FolhaCadernetaVenda folhaCadernetaVenda = new FolhaCadernetaVenda();
		folhaCadernetaVenda.setVenda(vendaAnotada);
		folhaCadernetaVenda.setFolhaCaderneta(folha);
		
		folha.getVendas().add(folhaCadernetaVenda);
		vendaAnotada.setFolhaCaderneta(folha);
		vendaAnotada.getVenda().setDataVenda(new Date());
		
		createSale(vendaAnotada, folhaCadernetaVenda);
		ResponseEntity<VendaAnotadaConsumidor> responseEntity = new ResponseEntity<>();
		responseEntity.setEntity(vendaAnotada);
		return responseEntity;
	}
	
	private void validateSale(IVenda iVenda) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		if (iVenda.getVenda() == null) {
			responseEntity.addMessage(IMessage.Message.VENDA_OBRIGATORIA, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
		} else if (iVenda.getVenda().getProdutos() == null || iVenda.getVenda().getProdutos().isEmpty()) {
			responseEntity.addMessage(IMessage.Message.ITEM_VENDA_OBRIGATORIO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
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

	private void finalizeOrThrows(ResponseEntity<? extends BaseEntity<?, ?>> responseEntity) {
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
		if (responseEntity.getMessages() == null) {
			responseEntity.setMessages(new ArrayList<>());
		}
		if (target.getMessages() != null) {
			responseEntity.getMessages().addAll(target.getMessages());
		}
	}

	private void validateProcucts(List<ItemVenda> produtos) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		AtomicInteger index = new AtomicInteger();
		produtos.stream().forEach(itemVenda -> {
			String indexStr = String.valueOf(index.getAndIncrement());
			if (itemVenda.getMovimentacaoProduto() == null) {
				responseEntity.addMessage(IMessage.Message.MOVIMENTACAO_PRODUTO_OBRIGATORIA, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW, indexStr);
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
			responseEntity.addMessage(IMessage.Message.PRODUTO_OBRIGATORIO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW, index);
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
				} else if (movimentacaoProduto.getQuantidade() <= 0) {
					responseEntity.addMessage(IMessage.Message.QUANTIDADE_VENDIDA, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW, produto.getDescricao() != null ? produto.getDescricao() : "");
					responseEntity.setIsValid(Boolean.FALSE);
				}
			} catch (NoResultException e) {
				//TODO ANOTAAI disparar evento de suspeita de fraude (venda de produto inexistente ou associado a outro vendedor)
				responseEntity.addMessage(IMessage.Message.PRODUTO_NAO_CADASTRADO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW, produto.getDescricao() != null ? produto.getDescricao() : "");
				responseEntity.setIsValid(Boolean.FALSE);
			}
		}
		finalizeOrThrows(responseEntity);
	}
	
	private void validateSale(IVendaAnonima vendaAnonima) {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		try {
			validateSale((IVenda)vendaAnonima);
		} catch (AppException e) {
			mergeErrorMessages(responseEntity, e.getResponseEntity());
		}
		finalizeOrThrows(responseEntity);
	}

	private void validateSale(IVendaConsumidor iVenda) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		try {
			validateSale((IVenda)iVenda);
		} catch (AppException e) {
			mergeErrorMessages(responseEntity, e.getResponseEntity());
		}
		Consumidor consumidor = iVenda.getFolhaCaderneta().getConsumidor();
		if (consumidor == null) {
			responseEntity.addMessage(IMessage.Message.CONSUMIDOR_OBRIGATORIO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
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
				responseEntity.addMessage(IMessage.Message.CONSUMIDOR_OBRIGATORIO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
				responseEntity.setIsValid(Boolean.FALSE);
			}
		}
		finalizeOrThrows(responseEntity);
	}

	private void createSale(IVenda vendaAnotada, FolhaCadernetaVenda folhaCadernetaVenda) {
		em.persist(vendaAnotada);
		em.persist(folhaCadernetaVenda);
	}

}
