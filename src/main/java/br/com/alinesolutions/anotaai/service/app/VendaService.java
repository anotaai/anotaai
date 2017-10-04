package br.com.alinesolutions.anotaai.service.app;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
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

	private void createSale(IVenda sale) throws AppException {
		em.persist(sale);
	}

	public ResponseEntity<Venda> createAnonymousSale(VendaAVistaAnonima vendaAVistaAnonima) throws AppException {
		createSale(vendaAVistaAnonima);
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

	public ResponseEntity<Venda> createAppointmentBookSale(VendaAnotadaConsumidor vendaAnotada) throws AppException {
		
		Caderneta caderneta = vendaAnotada.getFolhaCaderneta().getCaderneta();
		Consumidor consumidor = vendaAnotada.getFolhaCaderneta().getConsumidor();
		try {
			vendaAnotada.getFolhaCaderneta().setConsumidor(em.getReference(Consumidor.class, consumidor.getId()));
			vendaAnotada.getFolhaCaderneta().setCaderneta(em.getReference(Caderneta.class, caderneta.getId()));
			validateSale((IVenda)vendaAnotada);
			FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
			FolhaCadernetaVenda folhaCadernetaVenda = new FolhaCadernetaVenda();
			folhaCadernetaVenda.setVenda(vendaAnotada);
			folhaCadernetaVenda.setFolhaCaderneta(folha);
			
			folha.getVendas().add(folhaCadernetaVenda);
			vendaAnotada.setFolhaCaderneta(folha);
			vendaAnotada.getVenda().setDataVenda(new Date());
			
			createSale(vendaAnotada, folhaCadernetaVenda);
		} catch (AppException e) {
			
		}
		return null;
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
		if (responseEntity.getIsValid() != null) {
			throw new AppException(responseEntity);
		}
	}

	private ResponseEntity<? extends BaseEntity<?, ?>> buildResponseEntity() {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	private void mergeErrorMessages(ResponseEntity<? extends BaseEntity<?, ?>> responseEntity, ResponseEntity<?> target) {
		responseEntity.getMessages().addAll(target.getMessages());
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
				Produto produtoStorage = em.find(Produto.class, produto.getId());
				if (!produtoStorage.getCliente().equals(appService.getCliente())) {
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
				Consumidor consumidorStorage = em.find(Consumidor.class, consumidor.getId());
				if (!consumidorStorage.getClientes().contains(appService.getCliente())) {
					responseEntity.addMessage(IMessage.Message.CONSUMIDOR_OBRIGATORIO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW);
					responseEntity.setIsValid(Boolean.FALSE);
				}
			} catch (NoResultException e) {
				// TODO: handle exception
			}
		}
		finalizeOrThrows(responseEntity);
		
	}

	private void createSale(IVenda vendaAnotada, FolhaCadernetaVenda folhaCadernetaVenda) {
		em.persist(vendaAnotada);
		em.persist(folhaCadernetaVenda);
	}

}
