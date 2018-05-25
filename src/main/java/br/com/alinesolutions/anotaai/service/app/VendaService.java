package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.infra.AnotaaiUtil;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.StatusMovimentacao;
import br.com.alinesolutions.anotaai.metadata.model.domain.StatusVenda;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.BaseEntity.BaseEntityConstant;
import br.com.alinesolutions.anotaai.model.produto.IMovimentacao;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.produto.MovimentacaoProduto;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.produto.Produto.ProdutoConstant;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Cliente.ClienteConstant;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor.ClienteConsumidorConstant;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.CadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
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

	@Inject
	@Any
	private Event<IMovimentacao> eventMovimentacao;

	@EJB
	private AppService appService;
	
	@EJB
	private EstoqueService estoqueService;

	@EJB
	private ResponseUtil responseUtil;

	public ResponseEntity<VendaAVistaAnonima> createAnonymousSale(VendaAVistaAnonima vendaAVistaAnonima) throws AppException {
		return null;
	}

	public ResponseEntity<Venda> createConsumerSale(VendaAVistaConsumidor vendaAVistaConsumidor) throws AppException {

		Caderneta caderneta = em.getReference(Caderneta.class, vendaAVistaConsumidor.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta().getId());
		ClienteConsumidor clienteConsumidor = em.getReference(ClienteConsumidor.class, vendaAVistaConsumidor.getFolhaCadernetaVenda().getFolhaCaderneta().getClienteConsumidor().getId());

		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, clienteConsumidor);
		FolhaCadernetaVenda venda = new FolhaCadernetaVenda();
		venda.setFolhaCaderneta(folha);
		folha.getVendas().add(venda);
		vendaAVistaConsumidor.setFolhaCadernetaVenda(new FolhaCadernetaVenda());
		vendaAVistaConsumidor.getFolhaCadernetaVenda().getVenda().setInicioVenda(AnotaaiUtil.getInstance().now());
		vendaAVistaConsumidor.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		em.persist(venda);

		return null;
	}

	public ResponseEntity<VendaAnotadaConsumidor> createAppointmentBookSale(VendaAnotadaConsumidor vendaAnotada) throws AppException {
		ResponseEntity<VendaAnotadaConsumidor> responseEntity = new ResponseEntity<>(vendaAnotada);
		responseEntity.addMessage(IMessage.VENDA_SUCESSO, TipoMensagem.SUCCESS);
		validarCamposObrigatorios(vendaAnotada);
		VendaAnotadaConsumidor novaVendaAnotada = new VendaAnotadaConsumidor();
		ClienteConsumidor clienteConsumidor = getClienteConsumidor(vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getClienteConsumidor());
		Caderneta caderneta = getCaderneta(vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta());
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, clienteConsumidor);
		novaVendaAnotada.setFolhaCadernetaVenda(getFolhaCadernetaVenda(vendaAnotada.getFolhaCadernetaVenda(), caderneta));
		novaVendaAnotada.getFolhaCadernetaVenda().setFolhaCaderneta(folha);
		Venda venda = vendaAnotada.getFolhaCadernetaVenda().getVenda();
		novaVendaAnotada.getFolhaCadernetaVenda().setVenda(getVenda(venda));
		novaVendaAnotada.getFolhaCadernetaVenda().getVenda().setProdutos(getProdutos(venda.getProdutos(), novaVendaAnotada.getFolhaCadernetaVenda().getVenda()));
		folha.getVendas().add(vendaAnotada.getFolhaCadernetaVenda());
		novaVendaAnotada.getFolhaCadernetaVenda().getVenda().setConclusaoVenda(AnotaaiUtil.getInstance().now());
		novaVendaAnotada.getFolhaCadernetaVenda().getVenda().setStatusVenda(StatusVenda.FINALIZADA);
		em.persist(novaVendaAnotada);
		return responseEntity;
	}

	private ResponseEntity<? extends BaseEntity<?, ?>> buildResponseEntity() {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	public ResponseEntity<CadernetaVenda> createSale(Caderneta caderneta) throws AppException {
		Caderneta cadernetaDB = getCaderneta(caderneta);
		Venda venda = new Venda();
		venda.setInicioVenda(AnotaaiUtil.getInstance().now());
		venda.setStatusVenda(StatusVenda.EM_ANDAMENTO);
		CadernetaVenda cadernetaVenda = new CadernetaVenda();
		cadernetaVenda.setCaderneta(cadernetaDB);
		cadernetaVenda.setVenda(venda);
		em.persist(cadernetaVenda);
		cadernetaVenda.setCaderneta(caderneta);
		ResponseEntity<CadernetaVenda> responseEntity = new ResponseEntity<>(cadernetaVenda);
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	public ResponseEntity<ItemVenda> adicionarProduto(ItemVenda itemVenda) {
		ItemVenda novoItemVenda = new ItemVenda();
		novoItemVenda.setVenda(getVenda(itemVenda.getVenda()));
		Produto produtoDB = getProduto(itemVenda.getMovimentacaoProduto().getProduto());
		novoItemVenda.setPrecoCusto(estoqueService.recuperarEstoque(produtoDB).getPrecoCusto());
		novoItemVenda.setPrecoVenda(produtoDB.getPrecoVenda());
		novoItemVenda.setMovimentacaoProduto(new MovimentacaoProduto());
		novoItemVenda.getMovimentacaoProduto().setQuantidade(itemVenda.getMovimentacaoProduto().getQuantidade());
		novoItemVenda.getMovimentacaoProduto().setProduto(produtoDB);
		novoItemVenda.getMovimentacaoProduto().setStatusMovimentacao(StatusMovimentacao.REGISTRADA);
		em.persist(novoItemVenda);
		itemVenda.setId(novoItemVenda.getId());
		itemVenda.getMovimentacaoProduto().setId(novoItemVenda.getMovimentacaoProduto().getId());
		ResponseEntity<ItemVenda> responseEntity = new ResponseEntity<>(itemVenda);
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}
	
	public ResponseEntity<FolhaCadernetaVenda> adicionarConsumidor(FolhaCadernetaVenda folhaCadernetaVenda) {
		Venda venda = folhaCadernetaVenda.getVenda();
		Venda vendaDB = getVenda(venda);
		ClienteConsumidor clienteConsumidor = folhaCadernetaVenda.getFolhaCaderneta().getClienteConsumidor();
		ClienteConsumidor clienteConsumidorDB = getClienteConsumidor(clienteConsumidor);
		Caderneta caderneta = folhaCadernetaVenda.getFolhaCaderneta().getCaderneta();
		Caderneta cadernetaDB = getCaderneta(caderneta);
		FolhaCaderneta folhaCaderneta = folhaCadernetaService.recuperarFolhaCaderneta(cadernetaDB, clienteConsumidorDB);
		FolhaCadernetaVenda novaFolhaCadernetaVenda = new FolhaCadernetaVenda();
		novaFolhaCadernetaVenda.setVenda(vendaDB);
		novaFolhaCadernetaVenda.setFolhaCaderneta(folhaCaderneta);
		em.persist(novaFolhaCadernetaVenda);
		folhaCadernetaVenda.setId(novaFolhaCadernetaVenda.getId());
		folhaCadernetaVenda.getFolhaCaderneta().setId(novaFolhaCadernetaVenda.getFolhaCaderneta().getId());
		ResponseEntity<FolhaCadernetaVenda> responseEntity = new ResponseEntity<>(folhaCadernetaVenda);
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	private ClienteConsumidor getClienteConsumidor(ClienteConsumidor clienteConsumidor) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Boolean.FALSE);
		responseEntity.addMessage(IMessage.VENDA_ERRO_CONSUMIDORNAOCADASTRADA, TipoMensagem.ERROR);
		if (clienteConsumidor != null && clienteConsumidor.getId() != null) {
			try {
				final ClienteConsumidor clienteConsumidorBD = em.getReference(ClienteConsumidor.class, clienteConsumidor.getId());
				TypedQuery<ClienteConsumidor> query = em.createNamedQuery(ClienteConsumidorConstant.FIND_BY_CLIENTE_KEY, ClienteConsumidor.class);
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, appService.getCliente());
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE_CONSUMIDOR, clienteConsumidorBD);
				query.getSingleResult();
				return clienteConsumidorBD;
			} catch (NoResultException e) {
				throw new AppException(responseEntity);
			}
		} else {
			throw new AppException(responseEntity);
		}
	}

	private List<ItemVenda> getProdutos(List<ItemVenda> produtos, Venda venda) throws AppException {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		final List<ItemVenda> produtosBD = new ArrayList<>();
		produtos.stream().forEach(itemVenda -> {
			try {
				ItemVenda itemVendaBD = em.find(ItemVenda.class, itemVenda.getId());
				if (!itemVendaBD.getVenda().equals(venda)) {
					throw new AppException(responseEntity);
				} else if (!itemVenda.getMovimentacaoProduto().equals(itemVenda.getMovimentacaoProduto())) {
					throw new AppException(responseEntity);
				} else if (!itemVendaBD.getMovimentacaoProduto().getProduto().equals(itemVenda.getMovimentacaoProduto().getProduto())) {
					throw new AppException(responseEntity);
				}
				produtosBD.add(itemVendaBD);
			} catch (NoResultException e) {
				throw new AppException(responseEntity);
			}
		});
		return produtosBD;
	}
	
	private Produto getProduto(Produto produto) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Boolean.FALSE);
		responseEntity.addMessage(IMessage.VENDA_ERRO_PRODUTONAOCADASTRADA, TipoMensagem.ERROR);
		if (produto != null && produto.getId() != null) {
			try {
				final Produto vendaDB = em.find(Produto.class, produto.getId());
				TypedQuery<Produto> query = em.createNamedQuery(ProdutoConstant.PRODUTO_BY_CLIENTE_KEY, Produto.class);
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, appService.getCliente());
				query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, produto.getId());
				query.getSingleResult();
				return vendaDB;
			} catch (NoResultException e) {
				throw new AppException(responseEntity);
			}
		} else {
			throw new AppException(responseEntity);
		}
	}

	private Venda getVenda(Venda venda) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Boolean.FALSE);
		responseEntity.addMessage(IMessage.VENDA_ERRO_VENDANAOCADASTRADA, TipoMensagem.ERROR);
		if (venda != null && venda.getId() != null) {
			if (venda.getStatusVenda().equals(StatusVenda.EM_ANDAMENTO)) {
				try {
					final Venda vendaDB = em.find(Venda.class, venda.getId());
					TypedQuery<Cliente> query = em.createNamedQuery(ClienteConstant.FIND_BY_VENDA_KEY, Cliente.class);
					query.setParameter(BaseEntityConstant.FIELD_VENDA, vendaDB);
					Cliente clienta = query.getSingleResult();
					if (!clienta.equals(appService.getCliente())) {
						throw new AppException(responseEntity);
					}
					return vendaDB;
				} catch (NoResultException e) {
					throw new AppException(responseEntity);
				}
			} else {
				throw new AppException(responseEntity);
			}
		} else {
			throw new AppException(responseEntity);
		}
	}

	private Caderneta getCaderneta(Caderneta caderneta) throws AppException {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Boolean.FALSE);
		responseEntity.addMessage(IMessage.VENDA_ERRO_CADERNETANAOCADASTRADA, TipoMensagem.ERROR, caderneta.getDescricao());
		try {
			final Caderneta cadernetaDB = em.find(Caderneta.class, caderneta.getId());
			if (!cadernetaDB.getCliente().getId().equals(appService.getCliente().getId())) {
				throw new AppException(responseEntity);
			}
			return cadernetaDB;
		} catch (NoResultException e) {
			throw new AppException(responseEntity);
		}
	}
	
	private FolhaCadernetaVenda getFolhaCadernetaVenda(FolhaCadernetaVenda folhaCadernetaVenda, Caderneta caderneta) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>(Boolean.FALSE);
		responseEntity.addMessage(IMessage.VENDA_ERRO_VENDAINVALIDA, TipoMensagem.ERROR);
		try {
			FolhaCadernetaVenda folhaCadernetaVendaBD = em.find(FolhaCadernetaVenda.class, folhaCadernetaVenda.getId());
			if (!folhaCadernetaVendaBD.getFolhaCaderneta().getCaderneta().equals(caderneta)) {
				throw new AppException(responseEntity);
			}
			return folhaCadernetaVendaBD;
		} catch (NoResultException e) {
			throw new AppException(responseEntity);
		}
	}

	private void validarCamposObrigatorios(VendaAnotadaConsumidor vendaAnotada) {
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = buildResponseEntity();
		responseEntity.setIsValid(
			vendaAnotada != null && 
			vendaAnotada.getFolhaCadernetaVenda() != null && 
			vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta() != null && 
			vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta() != null &&
			vendaAnotada.getFolhaCadernetaVenda().getFolhaCaderneta().getCaderneta().getId() != null && 
			vendaAnotada.getFolhaCadernetaVenda().getVenda() != null && vendaAnotada.getFolhaCadernetaVenda().getVenda().getProdutos() != null && 
			!vendaAnotada.getFolhaCadernetaVenda().getVenda().getProdutos().isEmpty()
		);
		if (!responseEntity.getIsValid()) {
			responseEntity.addMessage(IMessage.VENDA_OBRIGATORIO_CAMPOSNAOINFORMADOS, TipoMensagem.ERROR, Constant.App.LONG_TIME_VIEW);
		}
		finalizeOrThrows(responseEntity);
	}

	private void finalizeOrThrows(ResponseEntity<? extends BaseEntity<?, ?>> responseEntity) throws AppException {
		if (responseEntity.getIsValid() != null && !responseEntity.getIsValid()) {
			throw new AppException(responseEntity);
		}
	}
}
