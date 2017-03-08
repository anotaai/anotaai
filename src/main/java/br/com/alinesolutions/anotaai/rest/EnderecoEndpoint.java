package br.com.alinesolutions.anotaai.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.com.alinesolutions.anotaai.metadata.model.AnotaaiViewException;
import br.com.alinesolutions.anotaai.metadata.model.Cep;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
@Path("/enderecos")
public class EnderecoEndpoint {

	@Inject
	private AppService service;

	@GET
	@Path("/findcep/{nrCep:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperarCep(@PathParam("nrCep") Integer nrCep) {
		Cep cep = null;
		ResponseBuilder builder = null;
		try {
			cep = service.findCep(nrCep);
			builder = Response.ok(cep);
		} catch (IllegalArgumentException e) {
			AnotaaiViewException exception = new AnotaaiViewException("cep.nao.cadastrado", TipoMensagem.WARNING,
					Constant.Message.DEFAULT_TIME_VIEW, cep.toString());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(exception);
		}

		return builder.build();
	}

}
