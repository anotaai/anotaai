package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.message.AnotaaiSendMessage;
import br.com.alinesolutions.anotaai.message.qualifier.Email;
import br.com.alinesolutions.anotaai.message.qualifier.SMS;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.Login;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoAcesso;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.SessaoUsuario.SessaoUsuarioConstant;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Telefone.TelefoneConstant;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.Usuario.UsuarioConstant;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.util.AnotaaiUtil;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.Criptografia;

@Stateless
public class UsuarioService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@Inject
	@Email
	private AnotaaiSendMessage senderEmail;
	
	@Inject
	@SMS
	private AnotaaiSendMessage senderSMS;
	
	@EJB
	private UploadService uploadService;
	
	@EJB
	private AppService appService;

	@Resource
	private SessionContext sessionContext;

	public Usuario create(Usuario usuario) throws AppException {
		usuario.setDataCadastro(new Date());
		if (usuario.getEmail() != null) {
			//remove o ponto quando for gmail
			usuario.setEmail(appService.atulaizarEmail(usuario.getEmail()));
		}
		usuario.setCodigoAtivacao(UUID.randomUUID().toString());
		usuario.setPerfis(new ArrayList<UsuarioPerfil>());
		usuario.getPerfis().add(new UsuarioPerfil(usuario, Perfil.CONSUMIDOR));
		usuario.setSituacao(SituacaoUsuario.PENDENTE_VALIDACAO);
		usuario.setSenha(Criptografia.criptografar(usuario.getSenha()));
		validarUsuario(usuario);
		Consumidor consumidor = new Consumidor();
		consumidor.setUsuario(usuario);
		consumidor.setDataCadastro(new Date());
		em.persist(consumidor);
		senderEmail.notificacaoRegistroUsuario(usuario);
		return usuario;
	}

	private void validarUsuario(Usuario usuario) throws AppException {
		AnotaaiUtil util = AnotaaiUtil.getInstance();
		ResponseEntity responseEntity = new ResponseEntity();
		TypedQuery<Long> queryCount = em.createNamedQuery(Usuario.UsuarioConstant.COUNT_USURIO_BY_EMAIL_KEY, Long.class);
		String email = appService.atulaizarEmail(usuario.getEmail());
		queryCount.setParameter(Usuario.UsuarioConstant.FIELD_EMAIL, email);
		Long cont = queryCount.getSingleResult();
		if (cont > 0) {
			responseEntity.addMessage(Constant.Message.EMAIL_JA_CADASTRADO, TipoMensagem.ERROR, Constant.Message.DEFAULT_TIME_VIEW, usuario.getEmail());
			responseEntity.setIsValid(Boolean.FALSE);
		}
		queryCount = em.createNamedQuery(Usuario.UsuarioConstant.COUNT_USURIO_BY_TELEFONE_KEY, Long.class);
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_DDI, usuario.getTelefone().getDdi());
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_DDD, usuario.getTelefone().getDdd());
		queryCount.setParameter(Telefone.TelefoneConstant.FIELD_NUMERO, usuario.getTelefone().getNumero());
		cont = queryCount.getSingleResult();
		if (cont > 0) {
			responseEntity.addMessage(Constant.Message.TELEFONE_JA_CADASTRADO, TipoMensagem.ERROR, Constant.Message.DEFAULT_TIME_VIEW, util.formatarTelefoneStr(usuario.getTelefone()));
			responseEntity.setIsValid(Boolean.FALSE);
		}

		if (responseEntity.getIsValid() != null) {
			throw new AppException(responseEntity);
		}
	}

	public void ativarUsuario(Usuario usuario) throws AppException {
		Usuario usuarioDatabase = em.find(Usuario.class, usuario.getId());
		usuarioDatabase.setSenha(Criptografia.criptografar(usuario.getSenha()));
		usuarioDatabase.setSituacao(SituacaoUsuario.PENDENTE_VALIDACAO);
		usuario.setCodigoAtivacao(usuarioDatabase.getCodigoAtivacao());
		em.merge(usuarioDatabase);
		senderEmail.notificacaoRegistroUsuario(usuario);
	}

	public ResponseEntity findUserByActivationCode(String codigoAtivacao) throws AppException {
		TypedQuery<Usuario> q = em.createNamedQuery(Usuario.UsuarioConstant.FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_KEY, Usuario.class);
		q.setParameter(Usuario.UsuarioConstant.FIELD_CODIGO_ATIVACAO, codigoAtivacao);
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			Usuario usuario = q.getSingleResult();
			if (usuario.getSituacao().equals(SituacaoUsuario.NAO_REGISTRADO)) {
				responseEntity.setEntity(usuario);
				responseEntity.setIsValid(Boolean.TRUE);
			} else {
				responseEntity.addMessage(Constant.Message.USUARIO_JA_CADASTRADO, TipoMensagem.WARNING, Constant.Message.DEFAULT_TIME_VIEW);
				responseEntity.setIsValid(Boolean.FALSE);
			}
		} catch (NoResultException e) {
			responseEntity.addMessage(Constant.Message.CODIGO_ATIVACAO_INVALIDO, TipoMensagem.ERROR, Constant.Message.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
		}
		return responseEntity;
	}

	public ResponseEntity activateAccount(String codigoAtivacao) throws AppException {
		TypedQuery<Usuario> q = em.createNamedQuery(Usuario.UsuarioConstant.FIND_FOR_ACTIVATE_KEY, Usuario.class);
		q.setParameter(Usuario.UsuarioConstant.FIELD_CODIGO_ATIVACAO, codigoAtivacao);
		ResponseEntity responseEntity = new ResponseEntity();
		AppException appException = null;
		Usuario usuario = null;
		try {
			usuario = q.getSingleResult();
			if (usuario.getSituacao().equals(SituacaoUsuario.PENDENTE_VALIDACAO)) {
				usuario.setSituacao(SituacaoUsuario.ATIVO);
				em.merge(usuario);
				responseEntity.setIsValid(Boolean.TRUE);
			} else {
				responseEntity.setIsValid(Boolean.FALSE);
			}
		} catch (NoResultException e) {
			responseEntity.addMessage(Constant.Message.CODIGO_ATIVACAO_INVALIDO, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW);
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}

	public ResponseEntity findByTelefone(Telefone telefone) {
		Usuario usuario = null;
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			usuario = findUsuarioByTelefone(telefone);
			if (usuario.getSituacao().equals(SituacaoUsuario.NAO_REGISTRADO)) {
				usuario.setTelefone(telefone);
				responseEntity.setEntity(usuario);
				responseEntity.setIsValid(Boolean.TRUE);
			} else {
				responseEntity.addMessage(Constant.Message.USUARIO_JA_CADASTRADO, TipoMensagem.ERROR, Constant.Message.DEFAULT_TIME_VIEW);
				responseEntity.setIsValid(Boolean.FALSE);
			}
		} catch (NoResultException nre) {
			responseEntity.setIsValid(Boolean.TRUE);
		}
		return responseEntity;
	}

	/**
	 * Metodo para validar a senha do usuario e criar a sessao caso as senhas
	 * sejam coincidentes
	 * 
	 * @param login
	 * @param usuarioLogin
	 * @param senha
	 * @return
	 * @throws AppException
	 */
	private void ativarLogin(Login login, Usuario usuarioLogin, String senha) throws AppException {
		SessaoUsuario sessaoUsuario;
 
		String senhaUsuario = new String(Base64.getDecoder().decode(login.getUsuario().getSenha().getBytes()));
		String senhaCriptografada = Criptografia.criptografar(senhaUsuario);
		if (senha.equals(senhaCriptografada)) {
			sessaoUsuario = criarSessao(login, usuarioLogin);
			login.setUsuario(usuarioLogin);
			login.setSessionID(sessaoUsuario.getSessionID());
		} else {
			// senha nao confere
			ResponseEntity responseEntity = new ResponseEntity();
			responseEntity.addMessage(Constant.Message.USUARIO_SENHA_INVALIDO, TipoMensagem.ERROR, Constant.Message.DEFAULT_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
			throw new AppException(responseEntity);
		}
	}

	private SessaoUsuario criarSessao(Login login, Usuario usuarioLogin) {
		SessaoUsuario sessaoUsuario;
		sessaoUsuario = new SessaoUsuario();
		sessaoUsuario.setUsuario(em.find(Usuario.class, usuarioLogin.getId()));
		sessaoUsuario.setUltimoAcesso(new Date());
		sessaoUsuario.setSessionID(UUID.randomUUID().toString());
		sessaoUsuario.setKeepAlive(login.getKeepAlive());
		em.persist(sessaoUsuario);
		return sessaoUsuario;
	}

	public ResponseEntity login(Login login) throws AppException {

		AppException appException = null;
		Usuario usuarioLogin = null;
		TipoMensagem tipoMensagemErro = TipoMensagem.ERROR;
		Long time = Constant.Message.KEEP_ALIVE_TIME_VIEW;
		String key = null;
		String senha = null;
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setIsValid(Boolean.FALSE);
		try {
			usuarioLogin = loadUsuario(login);
			switch (usuarioLogin.getSituacao()) {
				case ATIVO:
					senha = usuarioLogin.getSenha();
					usuarioLogin.setSenha(null);
					responseEntity.setIsValid(Boolean.TRUE);
					ativarLogin(login, usuarioLogin, senha);
					break;
				case BLOQUEADO:
					key = Constant.Message.USUARIO_BLOQUEADO;
					break;
				case INATIVO:
					key = Constant.Message.USUARIO_INATIVO;
					break;
				case NAO_REGISTRADO:
					key = Constant.Message.USUARIO_NAO_REGISTRADO;
					time = Constant.Message.KEEP_ALIVE_TIME_VIEW;
					break;
				case PENDENTE_VALIDACAO:
					key = Constant.Message.USUARIO_PENDENTE_VALIDACAO;
					senderEmail.notificacaoRegistroUsuario(usuarioLogin);
					break;
			}
			if (!usuarioLogin.getSituacao().equals(SituacaoUsuario.ATIVO)) {
				// usuario com acesso bloqueado ou conta nao ativada
				responseEntity.addMessage(key, tipoMensagemErro, time);
				responseEntity.setIsValid(Boolean.FALSE);
				appException = new AppException(responseEntity);
				throw appException;
			}
			responseEntity.setLogin(login);
			return responseEntity;
		} catch (NoResultException e) {
			// usuario nao registrado
			responseEntity.addMessage(Constant.Message.USUARIO_SENHA_INVALIDO, tipoMensagemErro, time);
			responseEntity.setIsValid(Boolean.FALSE);
			appException = new AppException(responseEntity);
			throw appException;
		}
	}

	private Usuario loadUsuario(Login login) {
		TypedQuery<Usuario> q = null;
		if (login.getTipoAcesso().equals(TipoAcesso.EMAIL)) {
			q = em.createNamedQuery(UsuarioConstant.FIND_BY_EMAIL_KEY, Usuario.class);
			String email = appService.atulaizarEmail(login.getUsuario().getEmail());
			q.setParameter(UsuarioConstant.FIELD_EMAIL, email);
		} else if (login.getTipoAcesso().equals(TipoAcesso.TELEFONE)) {
			q = em.createNamedQuery(UsuarioConstant.FIND_BY_TELEFONE_LOGIN_KEY, Usuario.class);
			q.setParameter(Telefone.TelefoneConstant.FIELD_DDI, login.getUsuario().getTelefone().getDdi());
			q.setParameter(Telefone.TelefoneConstant.FIELD_DDD, login.getUsuario().getTelefone().getDdd());
			q.setParameter(Telefone.TelefoneConstant.FIELD_NUMERO, login.getUsuario().getTelefone().getNumero());
		}
		Usuario usuarioLogin = q.getSingleResult();
		//manter o email informado no login quando for gmail recolocar os pontos separadores
		if (login.getTipoAcesso().equals(TipoAcesso.EMAIL)) {
			usuarioLogin.setEmail(login.getUsuario().getEmail());			
		}
		return usuarioLogin;
	}

	public ResponseEntity update(Long id, Usuario entity) throws AppException {
		Usuario usuario = null;
		AnotaaiMessage message = null;
		ResponseEntity responseEntity = new ResponseEntity();
		AppException appException = null;
		if (entity != null && id != null && id.equals(entity.getId())) {
			usuario = em.find(Usuario.class, id);
			usuario.clone(entity);
			entity = em.merge(usuario);
			message = new AnotaaiMessage(Constant.Message.USUARIO_EDITADO_SUCESSO, TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, usuario.getNome());
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(message);
		} else {
			responseEntity.addMessage(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}

	private Usuario findUsuarioByTelefone(Telefone telefone) {
		TypedQuery<Usuario> q = null;
		Usuario usuario = null;
		q = em.createNamedQuery(UsuarioConstant.FIND_BY_TELEFONE_KEY, Usuario.class);
		q.setParameter(TelefoneConstant.FIELD_DDI, telefone.getDdi());
		q.setParameter(TelefoneConstant.FIELD_DDD, telefone.getDdd());
		q.setParameter(TelefoneConstant.FIELD_NUMERO, telefone.getNumero());
		usuario = q.getSingleResult();
		return usuario;
	}

	public ResponseEntity logout(Login login) throws AppException {
		ResponseEntity responseEntity = new ResponseEntity();
		Query query = em.createNamedQuery(SessaoUsuarioConstant.REMOVE_SESSION_KEY);
		query.setParameter(SessaoUsuarioConstant.FIELD_SESSION_ID, login.getSessionID());
		query.executeUpdate();
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}

	public Usuario loadByEmail(String email) {
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setIsValid(Boolean.FALSE);
		try {
			
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.UsuarioConstant.ACCESS_KEY, Usuario.class);
			query.setParameter(UsuarioConstant.FIELD_EMAIL, appService.atulaizarEmail(email));
			Usuario usuario = query.getSingleResult();
			return usuario;
			
		} catch (NoResultException e) {
			responseEntity.addMessage(Constant.Message.USUARIO_NAO_ENCONTRADO, TipoMensagem.WARNING, Constant.Message.DEFAULT_TIME_VIEW,"email");
			throw new AppException(responseEntity);
		}
	}

	public Usuario loadByTelefone(Telefone telefone) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.UsuarioConstant.FIND_BY_TELEFONE_PERSIST_KEY, Usuario.class);
			query.setParameter(Telefone.TelefoneConstant.FIELD_DDI, telefone.getDdi());
			query.setParameter(Telefone.TelefoneConstant.FIELD_DDD, telefone.getDdd());
			query.setParameter(Telefone.TelefoneConstant.FIELD_NUMERO, telefone.getNumero());
			return query.getSingleResult();
		} catch (NoResultException e) {
			responseEntity.addMessage(Constant.Message.USUARIO_NAO_ENCONTRADO, TipoMensagem.WARNING, Constant.Message.DEFAULT_TIME_VIEW,"telefone");
			throw new AppException(responseEntity);
		}
		
	}
	
	/**
	 * Confere se existe um usuário com o email ou telefone informado para atualizar a senha e enviar a notificação de confirmação de cadastro
	 * @param usuarioRequest
	 * @throws AppException
	 */
	public ResponseEntity solicitarMensagemAlteracaoSenha(Usuario usuario) throws AppException {
		
		Usuario usuarioDataBase = null;
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			StringBuilder mensagem = new StringBuilder();
			if(usuario.getTelefone() != null && usuario.getTelefone().getNumero() != null) {
				mensagem.append(TipoAcesso.TELEFONE.getDescricao()).append(" ");
				usuarioDataBase = loadByTelefone(usuario.getTelefone());
			} else {
				mensagem.append(" e ").append(TipoAcesso.EMAIL.getDescricao());
				usuarioDataBase = loadByEmail(usuario.getEmail());
			}
			usuarioDataBase.setCodigoAtivacao(UUID.randomUUID().toString());
			em.merge(usuarioDataBase);
			if (usuarioDataBase.getTelefone() != null && usuarioDataBase.getTelefone().getNumero() != null) {
				senderSMS.notificacaoRenewPassword(usuarioDataBase);
			}
			if (usuarioDataBase.getEmail() != null && !usuarioDataBase.getEmail().isEmpty()) {
				senderEmail.notificacaoRenewPassword(usuarioDataBase);
			}
			responseEntity.setIsValid(Boolean.TRUE);
			responseEntity.addMessage(new AnotaaiMessage(Constant.Message.SOLICITACAO_ALTERACAO_SENHA, TipoMensagem.SUCCESS, Constant.Message.LONG_TIME_VIEW, mensagem.toString()));
		} catch (AppException e) {
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(Constant.Message.USUARIO_NAO_ENCONTRADO, TipoMensagem.ERROR, Constant.Message.LONG_TIME_VIEW, "Parâetro");
		}
		return responseEntity;
	}
	
	public ResponseEntity findUserByActivationCodeRecuperarSenha(String codigoAtivacao) throws AppException {
		TypedQuery<Usuario> q = em.createNamedQuery(Usuario.UsuarioConstant.FIND_FOR_REGISTER_BY_CODIGO_ATIVACAO_KEY, Usuario.class);
		q.setParameter(Usuario.UsuarioConstant.FIELD_CODIGO_ATIVACAO, codigoAtivacao);
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			Usuario usuario = q.getSingleResult();
			if (usuario.getSituacao().equals(SituacaoUsuario.ATIVO) || usuario.getSituacao().equals(SituacaoUsuario.PENDENTE_VALIDACAO)) {
				responseEntity.setEntity(usuario);
				responseEntity.setIsValid(Boolean.TRUE);
			} else {
				responseEntity.addMessage(Constant.Message.USUARIO_BLOQUEADO, TipoMensagem.ERROR, Constant.Message.LONG_TIME_VIEW);
				responseEntity.setIsValid(Boolean.FALSE);
			}
		} catch (NoResultException e) {
			responseEntity.addMessage(Constant.Message.CODIGO_ATIVACAO_INVALIDO, TipoMensagem.ERROR, Constant.Message.LONG_TIME_VIEW);
			responseEntity.setIsValid(Boolean.FALSE);
			throw new AppException(responseEntity);
		}
		return responseEntity;
	}
	
	public ResponseEntity alterarSenha(Usuario entity) throws AppException {
		Usuario usuario = null;
		AnotaaiMessage message = null;
		ResponseEntity responseEntity = new ResponseEntity();
		AppException appException = null;
		if (entity != null && entity.getEmail() != null) {
			usuario = loadByEmail(entity.getEmail());
			if (entity.getCodigoAtivacao().equals(usuario.getCodigoAtivacao())) {
				entity.setSituacao(SituacaoUsuario.ATIVO);
				entity.setCodigoAtivacao(UUID.randomUUID().toString());
				entity.setSenha(Criptografia.criptografar(entity.getSenha()));
				usuario.clone(entity);
				em.merge(usuario);
				entity.setCodigoAtivacao(null);
				message = new AnotaaiMessage(Constant.Message.USUARIO_EDITADO_SUCESSO, TipoMensagem.SUCCESS, Constant.Message.DEFAULT_TIME_VIEW, usuario.getNome());
				responseEntity.setIsValid(Boolean.TRUE);
				responseEntity.setEntity(entity);
				responseEntity.setMessages(new ArrayList<>());
				responseEntity.getMessages().add(message);
			} else {
				responseEntity.addMessage(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW);
				appException = new AppException(responseEntity);
				throw appException;
			}
		} else {
			responseEntity.addMessage(Constant.Message.ILLEGAL_ARGUMENT, TipoMensagem.ERROR, Constant.Message.KEEP_ALIVE_TIME_VIEW);
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}
	
}
