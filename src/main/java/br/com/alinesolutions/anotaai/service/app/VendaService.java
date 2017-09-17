package br.com.alinesolutions.anotaai.service.app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
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
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(null/* Selecionar */, vendaAVistaConsumidor.getFolhaCaderneta().getConsumidor());
		FolhaCadernetaVenda venda = new FolhaCadernetaVenda();
		venda.setFolhaCaderneta(folha);
		/* na venda a vista consumidor deve conter o pagamento */
		venda.setVenda(vendaAVistaConsumidor);
		em.persist(venda);
		createSale(vendaAVistaConsumidor);

		return null;
	}

	public ResponseEntity<Venda> createAppointmentBookSale(VendaAnotadaConsumidor vendaAnotada) throws AppException {
		
		Caderneta caderneta = vendaAnotada.getFolhaCaderneta().getCaderneta();
		Consumidor consumidor = vendaAnotada.getFolhaCaderneta().getConsumidor();
		FolhaCaderneta folha = folhaCadernetaService.recuperarFolhaCaderneta(caderneta, consumidor);
		FolhaCadernetaVenda venda = new FolhaCadernetaVenda();
		venda.setFolhaCaderneta(folha);
		venda.setVenda(vendaAnotada);
		//em.persist(venda);
		
		return null;
	}

}
