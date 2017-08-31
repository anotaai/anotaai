package br.com.alinesolutions.anotaai.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.model.venda.IVenda;
import br.com.alinesolutions.anotaai.model.venda.Venda;
import br.com.alinesolutions.anotaai.service.app.VendaService;

@Path("/venda")
public class VendaEndpoint {
	
	@EJB
	private VendaService vendaService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(IVenda entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Venda> responseEntity = null;
		try {
			responseEntity = vendaService.create(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
 		ResponseEntity<?> responseEntity = null;
		try {
			responseEntity = vendaService.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<?> responseEntity = null;
		try {
			responseEntity = vendaService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@RolesAllowed("CLIENTE")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Venda> listAll(@QueryParam("dataInicial") String dataInicialStr, @QueryParam("dataFinal") String dataFinalStr) {
		
		DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.set(Calendar.DAY_OF_MONTH, 0);
		dataInicial.set(Calendar.MINUTE, 0);
		dataInicial.set(Calendar.SECOND, 0);
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.set(Calendar.DAY_OF_MONTH, 23);
		dataFinal.set(Calendar.MINUTE, 59);
		dataFinal.set(Calendar.SECOND, 59);
		try {
			dataInicial.setTime(formatador.parse(dataInicialStr));
			dataFinal.setTime(formatador.parse(dataFinalStr));	
			List<Venda> listAll = vendaService.listAll(dataInicial.getTime(), dataFinal.getTime());
			return listAll;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}

	@RolesAllowed("CLIENTE")
	@Path("/search")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public List<Produto> search(@QueryParam("dataInicial") String dataInicial, @QueryParam("dataFinal") String dataFinal) {
		
		return null;
	}
	
}
