package br.com.alinesolutions.anotaai.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.produto.GrupoProduto;
import br.com.alinesolutions.anotaai.service.app.GrupoProdutoService;

@RolesAllowed(Constant.Role.CLIENTE)
@Path("/grupoproduto")
public class GrupoProdutoEndpoint {
	
	@EJB
	private GrupoProdutoService grupoProdutoService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(GrupoProduto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<GrupoProduto> responseEntity = null;
		try {
			responseEntity = grupoProdutoService.create(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<GrupoProduto> responseEntity = null;
		try {
			responseEntity = grupoProdutoService.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		
		ResponseBuilder builder = null;
		ResponseEntity<GrupoProduto> responseEntity = null;
		try {
			responseEntity = grupoProdutoService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<GrupoProduto> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String nome) {
		return grupoProdutoService.listAll(startPosition, maxResult,nome);
	}
	
	@RolesAllowed(Constant.Role.CLIENTE)
	@GET
	@Path("/recuperarPorDescricao")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GrupoProduto> recuperarPorDescricao(@QueryParam("query") String query, @QueryParam("start") Integer startPosition, 
			 										@QueryParam("max") Integer maxResult, @QueryParam("idSetor") Long idSetor) {
		List<GrupoProduto> gruposProduto = grupoProdutoService.recuperarPorDescricao(query, startPosition, maxResult, idSetor);
		return gruposProduto;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, GrupoProduto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<GrupoProduto> responseEntity = null;
		try {
			responseEntity = grupoProdutoService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}
	
	
	@RolesAllowed(Constant.Role.CLIENTE)
	@GET
	@Path("/recuperarPorNome")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GrupoProduto> recuperarPorNome(@QueryParam("nome") String nome, @QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		List<GrupoProduto> grupos = grupoProdutoService.recuperarPorNome(nome, startPosition, maxResult);
		return grupos;
	}
}
