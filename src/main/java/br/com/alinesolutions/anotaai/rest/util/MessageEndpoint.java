package br.com.alinesolutions.anotaai.rest.util;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.i18n.Locale;
import br.com.alinesolutions.anotaai.i18n.base.ITranslate;
import br.com.alinesolutions.anotaai.i18n.en.TranslateEN;
import br.com.alinesolutions.anotaai.i18n.pt.TranslatePT;;

@Path("/i18n")
public class MessageEndpoint {

	@GET
	@Path("/locales")
	@Produces(MediaType.APPLICATION_JSON)
	public Response perfil() throws Exception {
		return Response.ok(Locale.values()).build();
	}
	
	@GET
	@Path("/locales/{locale}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocale(@PathParam("locale") String locale) throws IOException {
		ITranslate translate = null;
		switch (locale) {
		case "pt":
			translate = TranslatePT.getInstance();
			break;
		case "en":
			translate = TranslateEN.getInstance();
			break;
		default:
			translate = TranslatePT.getInstance();
			break;
		}
		
		return Response.ok(translate).build();
	}

}
