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
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.ConfiguracaoCaderneta;
import br.com.alinesolutions.anotaai.service.app.CadernetaService;
import br.com.alinesolutions.anotaai.util.Constant;

@RolesAllowed(Constant.Role.CLIENTE)
@Path("/caderneta")
public class CadernetaEndpoint {
	
	@EJB
	private CadernetaService cadernetaService;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Caderneta> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String descricao) {
		return cadernetaService.listAll(startPosition, maxResult , descricao);
	}
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {

		ResponseBuilder builder = null;
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getappointmentbooks")
	public List<Caderneta> getAppointmentBooks() {
		return cadernetaService.getAppointmentBooks();
	}
	
	
	@POST
	@Path("/checkSameConfiguration")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkSameConfiguration(ConfiguracaoCaderneta entity) {

		ResponseBuilder builder = null;
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.checkSameConfiguration(entity);
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
	public Response create(ConfiguracaoCaderneta entity) {
		ResponseBuilder builder = null;
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.create(entity);
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
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
	
	@DELETE
	@Path("/removeByBookId/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeByBookId(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.removeByBookId(id);
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, ConfiguracaoCaderneta entity) {
		ResponseBuilder builder = null;
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = null;
		try {
			responseEntity = cadernetaService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}

}
