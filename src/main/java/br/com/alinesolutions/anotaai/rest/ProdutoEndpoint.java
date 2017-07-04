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

import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.produto.ItemReceita;
import br.com.alinesolutions.anotaai.model.produto.Produto;
import br.com.alinesolutions.anotaai.service.app.ProdutoService;

@Path("/produto")
public class ProdutoEndpoint {
	
	@EJB
	private ProdutoService produtoService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Produto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.create(entity);
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
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.deleteById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		ResponseBuilder builder = null;
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.findById(id);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	
	@RolesAllowed("CLIENTE")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Produto> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult, @QueryParam("nome") String descricao) {
		return produtoService.listAll(startPosition, maxResult , descricao);
	}

	@RolesAllowed("CLIENTE")
	@Path("/search")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public List<Produto> search(@QueryParam("query") String query, @QueryParam("start") Integer startPosition, 
			@QueryParam("max") Integer maxResult, @QueryParam("idGrupoProduto") Long idGrupoProduto) {
		List<Produto> produto = produtoService.searchByGrupoProduto(query, startPosition, maxResult, idGrupoProduto);
		return produto;
	}
	
	/**
	 * Recupera os produtos para criar itens de receita, nao deve retornar produtos que ja foram incluidos como item de receita e nem o 
	 * proprio produto que esta sendo cadastrado quando este ja estiver cadastrado
	 * 
	 * @param query
	 * @param startPosition
	 * @param maxResult
	 * @param produtosNaoInclusos ID dos produtos que ja estao no item de receita e o id do proprio produto para excluir da consulta
	 * @return
	 */
	@RolesAllowed("CLIENTE")
	@Path("/searchProdutosParaReceita")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public List<Produto> searchProdutoByDescricao(@QueryParam("query") String query, @QueryParam("start") Integer startPosition, 
			@QueryParam("max") Integer maxResult, @QueryParam("produtosDaReceita") List<Long> excludes) {
		List<Produto> produtos = produtoService.searchByDescricao(query, startPosition, maxResult, excludes);
		return produtos;
	}
	
	@RolesAllowed("CLIENTE")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addItemReceita")
	public Response addItemReceita(ItemReceita itemReceita) {
		ResponseEntity<ItemReceita> entity = produtoService.addItemReceita(itemReceita);
		return Response.ok(entity).build();
	}
	
	@RolesAllowed("CLIENTE")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/editItemReceita")
	public Response editItemReceita(ItemReceita itemReceita) {
		ResponseEntity<ItemReceita> entity = produtoService.editItemReceita(itemReceita);
		return Response.ok(entity).build();
	}
	
	@RolesAllowed("CLIENTE")
	@POST()
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deleteItemReceita")
	public Response deleteItemReceita(ItemReceita itemReceita) {
		ResponseEntity<ItemReceita> entity = produtoService.deleteItemReceita(itemReceita);
		return Response.ok(entity).build();
	}
	
	@PUT
	@Path("/{id:[0-9][0-9]*}")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, Produto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	@Path("/loadItensReceita")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadItensReceita(Produto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.loadItensReceita(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
	@Path("/loadDisponibilidades")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loadDisponibilidades(Produto entity) {
		ResponseBuilder builder = null;
		ResponseEntity<Produto> responseEntity = null;
		try {
			responseEntity = produtoService.loadDisponibilidades(entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.BAD_REQUEST);
		}
		return builder.build();
	}
	
}
