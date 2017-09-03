package br.com.alinesolutions.anotaai.service.app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaAnonima;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaConsumidor;
import br.com.alinesolutions.anotaai.model.venda.VendaAnotada;
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
	private AppService appService;
	
	@EJB
	private ResponseUtil responseUtil;
	

    private void createSale(IVenda sale) throws AppException {
    	
    }

	public ResponseEntity<Venda> createAnonymousSale(IVenda sale) throws AppException {
		VendaAVistaAnonima vendaAVistaAnonima = (VendaAVistaAnonima) sale;
		createSale(vendaAVistaAnonima);
		return null;
	}

	public ResponseEntity<Venda> createConsumerSale(IVenda sale) throws AppException {
		VendaAVistaConsumidor vendaAVistaConsumidor = (VendaAVistaConsumidor) sale;
		createSale(vendaAVistaConsumidor);
		return null;
	}

	public ResponseEntity<Venda> createAppointmentBookSale(IVenda sale) throws AppException {
		VendaAnotada vendaAnotada = (VendaAnotada) sale;
		createSale(vendaAnotada);
		return null;
	}

	
	
}
