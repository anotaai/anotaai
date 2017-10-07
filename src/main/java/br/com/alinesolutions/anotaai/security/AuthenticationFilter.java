package br.com.alinesolutions.anotaai.security;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiSession;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.Login;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.app.SessaoUsuarioService;
import br.com.alinesolutions.anotaai.service.app.UsuarioService;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.RequestUtils;
import br.com.alinesolutions.anotaai.util.UsuarioUtils;

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
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
						.entity(buildResponseEntity(IMessage.SECURITY_ACCESS_FORBIDDEN)).build());
			}
		}

		// se for proibido para todos bloqueia o acesso
		if (method.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
					.entity(buildResponseEntity(IMessage.SECURITY_ACCESS_FORBIDDEN)).build());
		} else if (method.isAnnotationPresent(RolesAllowed.class)) {// se for um metodo com permissao
			if (login != null) {
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				try {
					Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
					// o usuario nao tem acesso a funcionalidade ou a sessao expirou
					if (!isSessionActive(sessaoUsuario)) {
						requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
								.entity(buildResponseEntity(IMessage.SECURITY_ACCESS_DENIED)).build());
					} else if (!isUserAllowed(usuarioDatabase, rolesSet)) {
						requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
								.entity(buildResponseEntity(IMessage.SECURITY_ACCESS_FORBIDDEN)).build());
					} else {
						//acesso a funcionalidade liberado reinicia o tempo de sessao do usuario
						sessaoUsuario.setUltimoAcesso(new Date());
						sessaoUsuarioervice.resetSession(sessaoUsuario);
					}
				} catch (AppException e) {// se o usuario ou a sessao nao existirem
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
							.entity(buildResponseEntity(IMessage.SECURITY_SECURITY_SESSION_TIMEOUT)).build());
				}
			} else {
				// sessao expirou, solicitar um novo login
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.entity(buildResponseEntity(IMessage.SECURITY_ACCESS_DENIED)).build());
			}
		} else {
			AnotaaiSession anotaaiSession = new AnotaaiSession(request, sessaoUsuario);
			RequestUtils.putRequest(anotaaiSession);
		}
	}

	private ResponseEntity<?> buildResponseEntity(String key) {
		ResponseEntity<?> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.FALSE);
		responseEntity.addMessage(key, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW);
		return responseEntity;
	}

	private Boolean isSessionActive(SessaoUsuario sessaoUsuario) {
		Boolean isSessionActive = Boolean.TRUE;
		Calendar sessionTime = Calendar.getInstance();
		sessionTime.setTime(sessaoUsuario.getUltimoAcesso());
		Long timeLogged = ChronoUnit.MINUTES.between(sessionTime.toInstant(), Calendar.getInstance().toInstant());
		if (timeLogged > Constant.App.SESSION_TIME) {
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

	private boolean isUserAllowed(final Usuario usuario, final Set<String> rolesSet) {
		Boolean isAllowed = Boolean.FALSE;

		for (UsuarioPerfil perfilUsuario : usuario.getPerfis()) {
			isAllowed = rolesSet.contains(perfilUsuario.getPerfil().toString());
			if (isAllowed) {
				isAllowed = Boolean.TRUE;
				break;
			}
		}
		return isAllowed;
	}
}