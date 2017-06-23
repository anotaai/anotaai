package br.com.alinesolutions.anotaai.rest;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.service.app.ClienteConsumidorService;

@Path("/clienteconsumidor")
public class ClienteConsumidorEndpoint {

	@Inject
	private ClienteConsumidorService service;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(ClienteConsumidor clienteConsumidor) {
		ResponseBuilder builder = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		
		try {
			responseEntity = service.create(clienteConsumidor);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		try {
			responseEntity = service.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		try {
			responseEntity = service.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("CLIENTE")
	public ResponseEntity<Consumidor> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String nome) {
	  return service.listAll(startPosition, maxResult, nome);
	}
	

	@POST
	@Path("/findby/telefone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByTelefone(Telefone telefone) {
		ResponseBuilder builder = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		try {
			responseEntity = service.findByTelefone(telefone);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.OK).entity(e.getResponseEntity());
		}
		return builder.build();
	}

	@POST
	@Path("/usuario/editable")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response isEditable(Usuario usuario) {
		ResponseBuilder builder = null;
		ResponseEntity<Usuario> responseEntity = null;
		try {
			responseEntity = service.isEditable(usuario);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok().entity(e.getResponseEntity());
		}
		return builder.build();
	}

	@POST
	@Path("/usuario/recomendaredicao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response recomendarEdicao(ClienteConsumidor clienteConsumidor) {
		ResponseBuilder builder = null;
		try {
			ResponseEntity<ClienteConsumidor> responseEntity = service.recomendarEdicao(clienteConsumidor);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok().entity(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, ClienteConsumidor clienteConsumidor) {
		ResponseBuilder builder = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		try {
			responseEntity = service.update(id, clienteConsumidor);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}

}
