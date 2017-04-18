package br.com.alinesolutions.anotaai.rest;

import javax.ejb.EJB;
import javax.ws.rs.Path;

import br.com.alinesolutions.anotaai.service.AppService;

@Path("/config")
public class AppConfigEndpoint {

	@EJB
	private AppService service;
	
	
}
