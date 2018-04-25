package br.com.alinesolutions.anotaai.rest;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.model.domain.Menu;
import br.com.alinesolutions.anotaai.service.AppService;

@PermitAll
@Stateless
@Path("/menu")
public class MenuEndpoint {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@GET()
	@Path("/principal")
	@Produces(MediaType.APPLICATION_JSON)
	public Response principal() throws Exception {
		return Response.ok(appService.getItensMenu(Menu.PRINCIPAL)).build();
	}
}	