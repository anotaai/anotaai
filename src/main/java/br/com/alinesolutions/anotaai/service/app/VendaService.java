package br.com.alinesolutions.anotaai.service.app;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
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
import br.com.alinesolutions.anotaai.util.AnotaaiUtil;

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
			validateSale(vendaAnotada);
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

	private void validateSale(IVendaConsumidor venda) throws AppException {

		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = new ResponseEntity<>();
		Consumidor consumidor = null;
		try {
			consumidor = venda.getFolhaCaderneta().getConsumidor();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (10 > 0) {
			responseEntity.addMessage(IMessage.CONSUMIDOR_INVALIDO, TipoMensagem.ERROR, IMessage.DEFAULT_TIME_VIEW, consumidor.getUsuario().getNome());
			responseEntity.setIsValid(Boolean.FALSE);
		}
		
		
	}
	
	private void validateSale(IVenda venda) throws AppException {
		AnotaaiUtil util = AnotaaiUtil.getInstance();
		ResponseEntity<? extends BaseEntity<?, ?>> responseEntity = new ResponseEntity<>();
		
	}

	

	private void createSale(IVenda vendaAnotada, FolhaCadernetaVenda folhaCadernetaVenda) {
		em.persist(vendaAnotada);
		em.persist(folhaCadernetaVenda);
	}

}
