package br.com.alinesolutions.anotaai.security;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.infra.AnotaaiUtil;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.RequestUtils;
import br.com.alinesolutions.anotaai.infra.UsuarioUtils;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSession;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.Login;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.app.SessaoUsuarioService;
import br.com.alinesolutions.anotaai.service.app.UsuarioService;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	@Context
	private HttpServletRequest request;
	
	@EJB
	private SessaoUsuarioService sessaoUsuarioervice;
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private AppService service;

	private static final String AUTHORIZATION_PROPERTY;
	private static final String AUTHENTICATION_SCHEME;

	static {
		AUTHORIZATION_PROPERTY = "Authorization";
		AUTHENTICATION_SCHEME = "Basic";
	}

	@Override
	public void filter(ContainerRequestContext requestContext) {

		String authorization = requestContext.getHeaderString(AUTHORIZATION_PROPERTY);
		Method method = resourceInfo.getResourceMethod();
		//passa o request para as classes de negocio para recuperar: <code>RequestUtils.getRequest()</code>
		
		Login login = null;
		Usuario usuarioDatabase = null;
		SessaoUsuario sessaoUsuario = null;
		if (authorization != null && !authorization.isEmpty() && !authorization.equals(AUTHENTICATION_SCHEME)) {
			login = buildLogin(authorization);
			try {
				usuarioDatabase = usuarioService.loadByTelefone(login.getUsuario().getTelefone());
				UsuarioUtils.putUsuario(usuarioDatabase);
				sessaoUsuario = sessaoUsuarioervice.getSessionActive(login.getSessionID());
				AnotaaiSession anotaaiSession = new AnotaaiSession(request, sessaoUsuario);
				RequestUtils.putRequest(anotaaiSession);
			} catch (AppException e) {
				abortWith(requestContext, Response.Status.FORBIDDEN, IMessage.SECURITY_ACCESS_FORBIDDEN);
			}
		}

		// se for proibido para todos bloqueia o acesso na classe ou no metodo
		if (resourceInfo.getResourceClass().isAnnotationPresent(DenyAll.class) || method.isAnnotationPresent(DenyAll.class)) {
			abortWith(requestContext, Response.Status.FORBIDDEN, IMessage.SECURITY_ACCESS_FORBIDDEN);
		} else {
			Boolean securityFromMethod = method.isAnnotationPresent(RolesAllowed.class);
			Boolean securityFromClass = resourceInfo.getResourceClass().isAnnotationPresent(RolesAllowed.class);
			if (securityFromMethod || securityFromClass) {// se for um metodo com permissao
				if (login != null) {
					RolesAllowed rolesAnnotation = securityFromClass ? resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class) : method.getAnnotation(RolesAllowed.class);
					try {
						List<Perfil> listaPerfis = Arrays.stream(rolesAnnotation.value()).map(perfil -> Perfil.valueOf(perfil)).collect(Collectors.toList());
						// o usuario nao tem acesso a funcionalidade ou a sessao expirou
						if (!isSessionActive(sessaoUsuario)) {
							abortWith(requestContext, Response.Status.FORBIDDEN, IMessage.SECURITY_ACCESS_DENIED);
						} else if (!isUserAllowed(usuarioDatabase, listaPerfis)) {
							abortWith(requestContext, Response.Status.UNAUTHORIZED, IMessage.SECURITY_ACCESS_FORBIDDEN);
						} else {
							//acesso a funcionalidade liberado reinicia o tempo de sessao do usuario
							sessaoUsuario.setUltimoAcesso(AnotaaiUtil.getInstance().now());
							sessaoUsuarioervice.resetSession(sessaoUsuario);
						}
					} catch (AppException e) {// se o usuario ou a sessao nao existirem
						abortWith(requestContext, Response.Status.FORBIDDEN, IMessage.SECURITY_SESSION_TIMEOUT);
					}
				} else {
					// sessao expirou, solicitar um novo login
					abortWith(requestContext, Response.Status.FORBIDDEN, IMessage.SECURITY_ACCESS_DENIED);
				}
			} else if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
				//Acesso para todos os usuarios, com ou sem sessao ativa
				AnotaaiSession anotaaiSession = new AnotaaiSession(request, sessaoUsuario);
				RequestUtils.putRequest(anotaaiSession);
			} else {
				abortWith(requestContext, Response.Status.UNAUTHORIZED, IMessage.SECURITY_ACCESS_DENIED);
			}
		}
	}

	private void abortWith(ContainerRequestContext requestContext, Status status, String messageKey) {
		requestContext.abortWith(Response.status(status).entity(buildResponseEntity(messageKey)).build());
	}

	private ResponseEntity<?> buildResponseEntity(String key) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.FALSE);
		responseEntity.addMessage(key, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW);
		return responseEntity;
	}

	private Boolean isSessionActive(SessaoUsuario sessaoUsuario) {
		Boolean isSessionActive = Boolean.TRUE;
		if (sessaoUsuario != null && sessaoUsuario.getUltimoAcesso() != null) {
			Long timeLogged = ChronoUnit.MINUTES.between(sessaoUsuario.getUltimoAcesso(), AnotaaiUtil.getInstance().now());
			if (timeLogged > Constant.App.SESSION_TIME) {
				isSessionActive = Boolean.FALSE;
			}
		} else {
			isSessionActive = Boolean.FALSE;
		}
		return isSessionActive;
	}
	
	private Login buildLogin(String authorization) {
		String encodedUserPassword = authorization.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));
		StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		String email = tokenizer.nextToken();
		String sessionID = tokenizer.nextToken();
		String telefoneStr = tokenizer.nextToken();
		Telefone telefone = service.buildTelefone(telefoneStr);
		Login login = new Login(email, telefone, sessionID);
		return login;
	}

	private Boolean isUserAllowed(final Usuario usuario, final List<Perfil> listaPerfis) {
		Stream<UsuarioPerfil> filter = usuario.getPerfis().stream().filter(usuarioPerfil -> listaPerfis.contains(usuarioPerfil.getPerfil()));
		return !filter.collect(Collectors.toList()).isEmpty();
	}
}