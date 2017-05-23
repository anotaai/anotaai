package br.com.alinesolutions.anotaai.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.i18n.ITranslate;
import br.com.alinesolutions.anotaai.i18n.TranslateEN;
import br.com.alinesolutions.anotaai.i18n.TranslatePT;
import br.com.alinesolutions.anotaai.i18n.Locale;;

@Path("/i18n")
public class MessageEndpoint {

	@GET
	@Path("/locales")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Locale> getLocales() throws IOException {
		List<Locale> locales = new ArrayList<>();
		locales.add(new Locale("en", "English", "en-US"));
		locales.add(new Locale("pt", "Português", "pt-BR"));
		return locales;
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
