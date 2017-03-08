package br.com.alinesolutions.anotaai.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import br.com.alinesolutions.anotaai.metadata.model.FirebaseConfig;
import br.com.alinesolutions.anotaai.service.AppService;

@Path("/config")
public class AppConfigEndpoint {

	@EJB
	private AppService service;
	
	@GET
	@Path("/firebase")
	public FirebaseConfig loadFirebaseConfig() {
		return service.getFirebaseConfig();
	}
	
}
