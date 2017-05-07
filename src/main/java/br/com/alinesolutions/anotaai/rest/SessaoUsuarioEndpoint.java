package br.com.alinesolutions.anotaai.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.service.app.SessaoUsuarioService;

@Stateless
@Path("/sessaousuario")
public class SessaoUsuarioEndpoint {

	@EJB
	private SessaoUsuarioService service;
	
	@POST
	@Path("/isactive")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response isActive(String sessionID) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = service.isActive(sessionID);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return builder.build();
		
	}
	
}
