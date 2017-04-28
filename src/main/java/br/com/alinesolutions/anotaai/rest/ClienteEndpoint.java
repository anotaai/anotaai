package br.com.alinesolutions.anotaai.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSequencial;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiViewException;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoCodigoInterno;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.domain.SituacaoCliente;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.util.AnotaaiUtil;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.Criptografia;

@Stateless
@Path("/clientes")
public class ClienteEndpoint {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@Inject
	@Email
	private AnotaaiSendMessage sender;

	@Context
	private HttpServletRequest request;

	@EJB
	private AppService appService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)  
	public Response create(Cliente entity) {

		ResponseBuilder builder = null;

		entity.setDataCadastro(new Date());
		entity.getUsuario().setDataCadastro(new Date());
		entity.getUsuario().setCodigoAtivacao(UUID.randomUUID().toString());
		entity.getUsuario().setPerfis(new ArrayList<UsuarioPerfil>());
		entity.getUsuario().getPerfis().add(new UsuarioPerfil(entity.getUsuario(), Perfil.CLIENTE));
		entity.getUsuario().getPerfis().add(new UsuarioPerfil(entity.getUsuario(), Perfil.CONSUMIDOR));
		entity.getUsuario().setSenha(Criptografia.criptografar(entity.getUsuario().getSenha()));
		entity.setSituacaoCliente(SituacaoCliente.BLOQUEADO);
		entity.getUsuario().setSituacao(SituacaoUsuario.PENDENTE_VALIDACAO);
		ResponseEntity response = new ResponseEntity();
		try {
			validarCliente(entity);
			entity.setSequences(new ArrayList<>());
			entity.getSequences().add(new AnotaaiSequencial(TipoCodigoInterno.PRODUTO, entity));
			entity.getSequences().add(new AnotaaiSequencial(TipoCodigoInterno.CUPOM, entity));
			em.persist(entity);
			sender.notificacaoRegistroUsuario(entity.getUsuario());
			response.setIsValid(Boolean.TRUE);
			builder = Response.ok(response);
		} catch (AppException e) {
			response.setException(e.getViewException());
			response.setIsValid(Boolean.FALSE);
			builder = Response.ok(response);
		} catch (Exception e) {
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Cliente entity = em.find(Cliente.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	//f
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Cliente> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.usuario LEFT JOIN FETCH c.endereco WHERE c.id = :entityId ORDER BY c.id", Cliente.class);

		findByIdQuery.setParameter("entityId", id);
		Cliente entity;
		try {
			entity = findByIdQuery.getSingleResult();
			org.hibernate.Hibernate.initialize(entity.getUsuario().getSessoesAtivas());
			org.hibernate.Hibernate.initialize(entity.getProdutos());
			org.hibernate.Hibernate.initialize(entity.getConsumidores());
			org.hibernate.Hibernate.initialize(entity.getCuponsFiscais());

		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Cliente> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
		TypedQuery<Cliente> findAllQuery = em
				.createQuery("SELECT DISTINCT new Cliente(c.id, c.nomeComercial, c.cpf, c.dataCadastro, c.usuario.id, c.usuario.nome) FROM Cliente c LEFT JOIN c.usuario ORDER BY c.id", Cliente.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Cliente> results = findAllQuery.getResultList();
		return results;
	}

	private void validarCliente(Cliente cliente) throws AppException {
		Long defaultTimeView = Constant.Message.DEFAULT_TIME_VIEW;
		String telefoneJaCadastrado = Constant.Message.TELEFONE_JA_CADASTRADO;
		Usuario usuario = cliente.getUsuario();
		AnotaaiUtil util = AnotaaiUtil.getInstance();
		AnotaaiViewException ex = new AnotaaiViewException();
		Boolean hasException = Boolean.FALSE;
		TypedQuery<Long> queryCount = em.createNamedQuery(Usuario.UsuarioConstant.COUNT_USURIO_BY_EMAIL_KEY, Long.class);
		if (usuario.getEmail() != null) {
			// remove o ponto quando for gmail
			usuario.setEmail(appService.atulaizarEmail(usuario.getEmail()));
		}
		queryCount.setParameter(Usuario.UsuarioConstant.FIELD_EMAIL, usuario.getEmail());
		Long cont = queryCount.getSingleResult();
		if (cont > 0) {
			ex.setMessage(Constant.Message.EMAIL_JA_CADASTRADO, TipoMensagem.ERROR, defaultTimeView, usuario.getEmail());
			hasException = Boolean.TRUE;
		}
		queryCount = em.createNamedQuery(Usuario.UsuarioConstant.COUNT_USURIO_BY_TELEFONE_KEY, Long.class);
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_DDI, usuario.getTelefone().getDdi());
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_DDD, usuario.getTelefone().getDdd());
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_NUMERO, usuario.getTelefone().getNumero());
		cont = queryCount.getSingleResult();
		if (cont > 0) {
			ex.setMessage(telefoneJaCadastrado, TipoMensagem.ERROR, defaultTimeView, util.formatarTelefoneStr(usuario.getTelefone()));
			hasException = Boolean.TRUE;
		}

		if (hasException) {
			throw new AppException(ex);
		}
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, Cliente entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(Cliente.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
