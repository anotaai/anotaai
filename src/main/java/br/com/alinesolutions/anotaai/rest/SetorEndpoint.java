package br.com.alinesolutions.anotaai.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import br.com.alinesolutions.anotaai.model.produto.Setor;
import br.com.alinesolutions.anotaai.service.app.SetorService;

@Path("/setor")
public class SetorEndpoint {

	@EJB
	private SetorService setorService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Setor entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Setor> responseEntity = null;
		try {
			responseEntity = setorService.create(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<Setor> responseEntity = null;
		try {
			responseEntity = setorService.deleteById(id);
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
		ResponseEntity<Setor> responseEntity = null;
		try {
			responseEntity = setorService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@RolesAllowed("CLIENTE")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Setor> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String nomeSetor) {
		return setorService.listAll(startPosition, maxResult , nomeSetor);
	}
	
	
	@RolesAllowed("CLIENTE")
	@GET
	@Path("/recuperarPorNome")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Setor> recuperarPorNome(@QueryParam("nome") String nome, @QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		List<Setor> setores = setorService.recuperarPorNome(nome, startPosition, maxResult);
		return setores;
	}
	

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, Setor entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Setor> responseEntity = null;
		try {
			responseEntity = setorService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}
}
