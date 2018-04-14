package br.com.alinesolutions.anotaai.rest;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import br.com.alinesolutions.anotaai.model.produto.EntradaMercadoria;
import br.com.alinesolutions.anotaai.service.app.EntradaMercadoriaService;
import br.com.alinesolutions.anotaai.util.Constant;

@Path("/entradamercadoria")
public class EntradaMercadoriaEndpoint {
	
	
	@EJB
	private EntradaMercadoriaService entradaMercadoriaService;
	
	
	@RolesAllowed(Constant.Role.CLIENTE)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//TODO - Mudar para POST e alterar a data de string para Date
	public ResponseEntity<EntradaMercadoria> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String nome, @QueryParam("dataEntrada") String dataEntradaStr) {
		return entradaMercadoriaService.listAll(startPosition,maxResult,nome,dataEntradaStr);
	}
	
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {

		ResponseBuilder builder = null;
		ResponseEntity<EntradaMercadoria> responseEntity = null;
		try {
			responseEntity = entradaMercadoriaService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	
	@GET
	@Path("/getCommodityForDelete/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommodityForDelete(@PathParam("id") Long id) {

		ResponseBuilder builder = null;
		ResponseEntity<EntradaMercadoria> responseEntity = null;
		try {
			responseEntity = entradaMercadoriaService.getCommodityForDelete(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(EntradaMercadoria entity) {
		ResponseBuilder builder = null;
		ResponseEntity<EntradaMercadoria> responseEntity = null;
		try {
			responseEntity = entradaMercadoriaService.create(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
	
	@PUT
	@Path("/{id:[0-9][0-9]*}")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, EntradaMercadoria entity) {
		ResponseBuilder builder = null;
		ResponseEntity<EntradaMercadoria> responseEntity = null;
		try {
			responseEntity = entradaMercadoriaService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	@PUT
	@Path("/rejectCommodity")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rejectCommodity(EntradaMercadoria entity) {
		ResponseBuilder builder = null;
		ResponseEntity<EntradaMercadoria> responseEntity = null;
		try {
			responseEntity = entradaMercadoriaService.rejectCommodity(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	

}
