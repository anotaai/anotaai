package br.com.alinesolutions.anotaai.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.model.produto.ProdutoGrupoProduto;
import br.com.alinesolutions.anotaai.service.app.ProdutoGrupoProdutoService;

@Stateless
@Path("/produtogrupoproduto")
public class ProdutoGrupoProdutoEndpoint {
	
	@EJB
	private ProdutoGrupoProdutoService service;
	
	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<ProdutoGrupoProduto> responseEntity = null;
		try {
			responseEntity = service.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(ProdutoGrupoProduto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<ProdutoGrupoProduto> responseEntity = null;
		try {
			responseEntity = service.create(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
	
}
