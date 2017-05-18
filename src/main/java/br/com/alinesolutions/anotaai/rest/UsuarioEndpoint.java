package br.com.alinesolutions.anotaai.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.Login;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.service.app.UploadService;
import br.com.alinesolutions.anotaai.service.app.UsuarioService;

@Path("/usuarios")
public class UsuarioEndpoint {

	@Inject
	private UsuarioService usuarioService;

	@Context
	private SecurityContext context;

	@Context
	private HttpServletRequest request;
	
	
	@Inject
	private UploadService uploadService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Usuario usuario) {
		ResponseBuilder builder = null;
		ResponseEntity entity = new ResponseEntity();
		try {
			usuarioService.create(usuario);
			entity.setIsValid(Boolean.TRUE);
			builder = Response.ok(entity);
		} catch (AppException e) {
			builder = Response.ok(e.getResponseEntity());
		}
		return builder.build();
	}

	@PermitAll
	@POST
	@Path("/activationuser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ativarUsuario(Usuario usuario) {
		ResponseBuilder builder = null;
		try {
			usuarioService.ativarUsuario(usuario);
			builder = Response.ok();
		} catch (AppException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@POST
	@Path("/activation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activateAccount(String codigoAtivacao) {
		ResponseEntity entity = null;
		ResponseBuilder builder = null;
		try {
			entity = usuarioService.activateAccount(codigoAtivacao);
			builder = Response.ok(entity);
		} catch (AppException e) {
			builder = Response.ok().entity(e.getResponseEntity());
		}
		return builder.build();
	}

	@POST
	@Path("/byactivationcode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUserByActivationCode(String codigoAtivacao) {
		ResponseBuilder builder = null;
		ResponseEntity entity = null;
		try {
			entity = usuarioService.findUserByActivationCode(codigoAtivacao);
			builder = Response.ok(entity);
		} catch (AppException e) {
			builder = Response.status(Status.OK).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@PermitAll
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Login login) throws AppException {

		ResponseBuilder builder = null;
		try {
			builder = Response.ok(usuarioService.login(login));
		} catch (AppException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}

	@RolesAllowed("CONSUMIDOR")
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(Login login) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = usuarioService.logout(login);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@POST
	@Path("/findby/telefone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByTelefone(Telefone telefone) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = usuarioService.findByTelefone(telefone);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, Usuario entity) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = usuarioService.update(id, entity);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	@GET
	@Path("/profilePhoto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response profilePhoto() {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			responseEntity.setEntity(uploadService.getProfilePhoto());
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			builder = Response.status(Status.BAD_REQUEST).entity(e.getResponseEntity());
		}
		return builder.build();
	}
	
	@POST
	@Path("/solicitarMensagemAlteracaoSenha")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response solicitarMensagemAlteracaoSenha(Usuario usuario) {
		ResponseBuilder builder = null;
		ResponseEntity entity = null;
		try {
			entity = usuarioService.solicitarMensagemAlteracaoSenha(usuario);
			builder = Response.ok(entity);
		} catch (AppException e) {
			builder = Response.status(Status.OK).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	
	@POST
	@Path("/recuperarUsuarioAlteracaoSenha")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperarUsuarioAlteracaoSenha(String codigoAtivacao) {
		ResponseBuilder builder = null;
		ResponseEntity entity = null;
		try {
			entity = usuarioService.findUserByActivationCodeRecuperarSenha(codigoAtivacao);
			builder = Response.ok(entity);
		} catch (AppException e) {
			builder = Response.status(Status.OK).entity(e.getResponseEntity());
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
	/**
	 * Confere se existe um usuário com o email ou telefone informado para atualizar a senha e enviar a notificação de 
	 * confirmação de cadastro
	 * @param usuario
	 * @return
	 * @throws AppException
	 */
	
	
	@PermitAll
	@POST
	@Path("/alterarSenha")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response alterarSenha(Usuario usuario) throws AppException {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			responseEntity = usuarioService.alterarSenha(usuario);
		} catch (AppException e) {
			responseEntity = e.getResponseEntity();
		}
		builder = Response.ok(responseEntity);
		return builder.build();
	}

}
