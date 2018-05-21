package br.com.alinesolutions.anotaai.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.produto.ItemVenda;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCadernetaVenda;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaAnonima;
import br.com.alinesolutions.anotaai.model.venda.VendaAVistaConsumidor;
import br.com.alinesolutions.anotaai.model.venda.VendaAnotadaConsumidor;
import br.com.alinesolutions.anotaai.rest.util.JacksonConfig;
import br.com.alinesolutions.anotaai.service.app.VendaService;

@RolesAllowed(Constant.Role.CLIENTE)
@Path("/venda")
public class VendaEndpoint {

	private static final Logger log;

	static {
		log = Logger.getLogger(JacksonConfig.class.getName());
	}
	
	@EJB
	private VendaService vendaService;

	@POST
	@Path("/createanonymoussale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAnonymousSale(VendaAVistaAnonima entity) {
		ResponseBuilder builder = null;
		ResponseEntity<VendaAVistaAnonima> responseEntity = null;
		try {
			responseEntity = vendaService.createAnonymousSale(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			log.log(Level.SEVERE, "/createanonymoussale", e);
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@POST
	@Path("/createconsumersale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createConsumerSale(VendaAVistaConsumidor entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Venda> responseEntity = null;
		try {
			responseEntity = vendaService.createConsumerSale(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@POST
	@Path("/initsale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initSale(Caderneta caderneta) {
		ResponseBuilder builder = null;
		try {
			builder = Response.ok(vendaService.createSale(caderneta));
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@POST
	@Path("/addproduct")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(ItemVenda itemVenda) {
		ResponseBuilder builder = null;
		try {
			builder = Response.ok(vendaService.adicionarProduto(itemVenda));
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@POST
	@Path("/createappointmentbooksale")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAppointmentBookSale(VendaAnotadaConsumidor entity) {
		ResponseBuilder builder = null;
		try {
			builder = Response.ok(vendaService.createAppointmentBookSale(entity));
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@POST
	@Path("/addconsumer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addConsumer(FolhaCadernetaVenda entity) {
		ResponseBuilder builder = null;
		try {
			builder = Response.ok(new ResponseEntity<>(entity));
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

}
