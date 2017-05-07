package br.com.alinesolutions.anotaai.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.service.app.ClienteConsumidorService;
import br.com.alinesolutions.anotaai.util.Constant;

@Path("/clienteconsumidor")
public class ClienteConsumidorEndpoint {

	@Inject
	private ClienteConsumidorService service;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(ClienteConsumidor clienteConsumidor) {
		ResponseBuilder builder = null;
		try {
			service.create(clienteConsumidor);
			builder = Response.noContent();
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		try {
			service.deleteById(id);
			builder = Response.noContent();
		} catch (AppException e) {
			builder = Response.ok().entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("CLIENTE")
	public List<Consumidor> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		List<Consumidor> results = service.listAll(startPosition, maxResult);
		return results;
	}

	@POST
	@Path("/findby/telefone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByTelefone(Telefone telefone) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
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
		ResponseEntity responseEntity = null;
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
		AnotaaiMessage message = null;
		ResponseEntity responseEntity = null;
		try {
			service.recomendarEdicao(clienteConsumidor);
			message = new AnotaaiMessage(Constant.Message.RECOMENDACAO_EDICAO_ENVIADA, TipoMensagem.INFO, Constant.Message.LONG_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome());
			responseEntity = new ResponseEntity(message);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok().entity(e.getResponseEntity());
		}
		return builder.build();
	}

}
