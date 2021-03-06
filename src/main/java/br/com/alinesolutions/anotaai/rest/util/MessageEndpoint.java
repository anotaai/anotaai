package br.com.alinesolutions.anotaai.rest.util;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.i18n.Locale;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.LoadResource;;

@PermitAll
@Path("/i18n")
public class MessageEndpoint {
	
	@EJB
	private LoadResource loader;

	@GET
	@Path("/locales")
	@Produces(MediaType.APPLICATION_JSON)
	public Response perfil() throws Exception {
		return Response.ok(Locale.values()).build();
	}
	
	@GET
	@Path("/locales/{locale}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocale(@PathParam("locale") String key) throws IOException {
		Locale locale = null;
		try {
			locale = Locale.getByKey(key);
		} catch (IllegalArgumentException e) {
			locale = Locale.PT;//defaul locale
		}
		StringBuilder fileName = new StringBuilder(Constant.FileNane.I18N_PATH).append(locale.getKey()).append(".json");
		return Response.ok(loader.getFile(fileName.toString())).build();
	}

}
