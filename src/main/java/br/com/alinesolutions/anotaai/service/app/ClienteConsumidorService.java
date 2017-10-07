package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.SituacaoConsumidor;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor.ClienteConsumidorConstant;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Preferencia;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Telefone.TelefoneConstant;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.Usuario.UsuarioConstant;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.ShardingResourceFactory;

@Stateless
public class ClienteConsumidorService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@Inject
	ShardingResourceFactory appManager;

	@Resource
	private SessionContext sessionContext;

	@EJB
	private ResponseUtil responseUtil;
	
	public ResponseEntity<ClienteConsumidor> create(ClienteConsumidor clienteConsumidor) throws AppException {
		TypedQuery<Usuario> q = null;
		Usuario usuarioDatabase = null;
		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		Cliente cliente = appManager.getAppService().getCliente();
		clienteConsumidor.setCliente(cliente);
		clienteConsumidor.setDataAssociacao(new Date());
		clienteConsumidor.getConsumidor().setDataCadastro(new Date());
		clienteConsumidor.setSituacao(SituacaoConsumidor.ATIVO);
		Usuario usuario = clienteConsumidor.getConsumidor().getUsuario();
		usuario.setId(null);
		usuario.setDataCadastro(new Date());
		usuario.setPerfis(new ArrayList<>());
		usuario.getPerfis().add(new UsuarioPerfil(usuario, Perfil.CONSUMIDOR));
		usuario.setPreferencia(new Preferencia());
		usuario.setSituacao(SituacaoUsuario.NAO_REGISTRADO);
		Boolean isNewUser = Boolean.TRUE;
		try {
			// verifica se o telefone ainda nao foi utilizado
			q = em.createNamedQuery(UsuarioConstant.FIND_BY_TELEFONE_PERSIST_KEY, Usuario.class);
			q.setParameter(TelefoneConstant.FIELD_DDI, usuario.getTelefone().getDdi());
			q.setParameter(TelefoneConstant.FIELD_DDD, usuario.getTelefone().getDdd());
			q.setParameter(TelefoneConstant.FIELD_NUMERO, usuario.getTelefone().getNumero());
			usuarioDatabase = q.getSingleResult();
			clienteConsumidor.getConsumidor().setUsuario(usuarioDatabase);
			isNewUser = Boolean.FALSE;
		} catch (NoResultException e) {
			// verifica se o email ja esta cadastrado
			String email = usuario.getEmail();
			if (email != null && !email.equals("")) {
				email = appManager.getAppService().atulaizarEmail(usuario.getEmail());
				TypedQuery<Long> queryCount = em.createNamedQuery(Usuario.UsuarioConstant.COUNT_USURIO_BY_EMAIL_KEY,
						Long.class);
				queryCount.setParameter(Usuario.UsuarioConstant.FIELD_EMAIL, email);
				Long cont = queryCount.getSingleResult();
				if (cont > 0) {
					responseEntity = new ResponseEntity<>();
					responseEntity.setIsValid(Boolean.FALSE);
					AnotaaiMessage message = new AnotaaiMessage(IMessage.EMAIL_JA_CADASTRADO, TipoMensagem.ERROR,
							IMessage.DEFAULT_TIME_VIEW, usuario.getEmail());
					responseEntity.addMessage(message);
					throw new AppException(responseEntity);
				}
				usuario.setCodigoAtivacao(UUID.randomUUID().toString());
				if (usuario.getEmail() != null) {
					// remove o ponto quando for gmail
					usuario.setEmail(appManager.getAppService().atulaizarEmail(usuario.getEmail()));
				}
				clienteConsumidor.getConsumidor().setUsuario(usuario);
			}
		}
		
		em.persist(clienteConsumidor);
		notify(clienteConsumidor, isNewUser);
		ClienteConsumidor newClienteConsumidor = new ClienteConsumidor(clienteConsumidor.getId());
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setEntity(newClienteConsumidor);
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setMessages(new ArrayList<>());
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO,TipoMensagem.SUCCESS, IMessage.DEFAULT_TIME_VIEW, usuario.getNome()));
		return responseEntity;
	}

	private void notify(ClienteConsumidor clienteConsumidor, Boolean isNewUser) {
		String email = clienteConsumidor.getConsumidor().getUsuario().getEmail();
		Boolean hasEmail = email != null && !email.equals("");
		if (isNewUser) {
			notificacaoRegistroConsumidor(clienteConsumidor, hasEmail);
		} else {
			switch (clienteConsumidor.getConsumidor().getUsuario().getSituacao()) {
			case ATIVO:
				notificacaoAssociacaoConsumidorCliente(clienteConsumidor, hasEmail);
				break;
			case NAO_REGISTRADO:
				notificacaoRegistroConsumidor(clienteConsumidor, hasEmail);
				break;
			case BLOQUEADO:
				// TODO enviar mensagem para o consumidor e para o cliente
				// informando que o cadastro foi efetuado para um consumidor
				// bloqueado e assim que o bloqueio for suspenso ambos terao
				// acesso as informacoes do cliente ateh entao bloqueado
				break;
			case INATIVO:
				// TODO enviar mensagem para o consumidor e para o cliente
				// informando que o cadastro foi efetuado para um consumidor
				// bloqueado e assim que o bloqueio for suspenso ambos terao
				// acesso as informacoes do cliente ateh entao bloqueado
				break;
			case PENDENTE_VALIDACAO:
				// TODO criar uma mensagem de ativacao customizada para esta
				// situacao, o usuario cadastrou mas nao ativou o cadastro, e um
				// anotador cadastrou o usuario como consumidor, atualmente
				// estah enviando a mensagem como se o usuario tiversse se
				// cadastrado pelo caso de uso de criacao de conta
				notificacaoAtivacaoUsuario(clienteConsumidor, hasEmail);
				break;
			default:
				break;
			}
		}
	}

	private void notificacaoAtivacaoUsuario(ClienteConsumidor clienteConsumidor, Boolean hasEmail) {
		appManager.getSenderSMS().notificacaoRegistroUsuario(clienteConsumidor.getConsumidor().getUsuario());
		if (hasEmail) {
			appManager.getSenderMail().notificacaoRegistroUsuario(clienteConsumidor.getConsumidor().getUsuario());
		}
	}

	private void notificacaoAssociacaoConsumidorCliente(ClienteConsumidor clienteConsumidor, Boolean hasEmail) {
		appManager.getSenderSMS().notificacaoAssociacaoConsumidorCliente(clienteConsumidor);
		if (hasEmail) {
			appManager.getSenderMail().notificacaoAssociacaoConsumidorCliente(clienteConsumidor);
		}
	}

	private void notificacaoRegistroConsumidor(ClienteConsumidor clienteConsumidor, Boolean hasEmail) {
		appManager.getSenderSMS().notificacaoRegistroConsumidor(clienteConsumidor);
		if (hasEmail) {
			appManager.getSenderMail().notificacaoRegistroConsumidor(clienteConsumidor);
		}
	}

	public ResponseEntity<ClienteConsumidor> deleteById(Long id) throws AppException {
		ResponseEntity<ClienteConsumidor> entity = new ResponseEntity<>();
		 
		try {
			ClienteConsumidor clienteConsumidor = em.find(ClienteConsumidor.class, id);
			clienteConsumidor.getConsumidor().setAtivo(Boolean.FALSE);
			clienteConsumidor.setSituacao(SituacaoConsumidor.INATIVO);
			em.merge(clienteConsumidor);
			entity.setIsValid(Boolean.TRUE);
			entity.setMessages(new ArrayList<>());
			entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO,TipoMensagem.SUCCESS, IMessage.DEFAULT_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
		 
	}

	public ResponseEntity<Consumidor> listAll(Integer startPosition, Integer maxResult, String nome) throws AppException {
		TypedQuery<Consumidor> consumidorQuery = null;
		Cliente cliente = appManager.getAppService().getCliente();
		
		if(!"".equals(nome)) {
			consumidorQuery =  em.createNamedQuery(Consumidor.ConsumidorConstant.FIND_BY_NOME_KEY, Consumidor.class);
			consumidorQuery.setParameter("nome", nome);
		} else  {
			consumidorQuery =  em.createNamedQuery(Consumidor.ConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_KEY,Consumidor.class);
		}
		
		consumidorQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, cliente);
		
		if (startPosition != null) {
			consumidorQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			consumidorQuery.setMaxResults(maxResult);
		}
 
		ResponseEntity<Consumidor> responseEntity = new ResponseEntity<>();
		ResponseList<Consumidor> responseList = new ResponseList<Consumidor>();
		responseEntity.setList(responseList);
		
		final List<Consumidor> results = consumidorQuery.getResultList();
		responseList.setItens(results);
		
		TypedQuery<Long> countAll = null;
		
		if(!"".equals(nome)) {
			countAll  = em.createNamedQuery(Consumidor.ConsumidorConstant.FIND_BY_NOME_COUNT, Long.class);
			countAll.setParameter("nome", nome);
		} else {
			countAll  = em.createNamedQuery(Consumidor.ConsumidorConstant.LIST_ALL_COUNT, Long.class);
		}
		 
		countAll.setParameter(Constant.Entity.CLIENTE, cliente);
		responseList.setQtdTotalItens(countAll.getSingleResult());
		
		return responseEntity;
	}

	@POST
	@Path("/findby/telefone")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ClienteConsumidor> findByTelefone(Telefone telefone) throws AppException {
		Usuario usuario = null;
		ResponseEntity<ClienteConsumidor> responseEntity = null;
		ClienteConsumidor clienteConsumidor = null;
		AnotaaiMessage message = null;
		try {
			usuario = findUsuarioByTelefone(telefone);
			usuario.setTelefone(telefone);
			clienteConsumidor = new ClienteConsumidor();
			clienteConsumidor.setConsumidor(new Consumidor());
			clienteConsumidor.getConsumidor().setUsuario(usuario);
			String nome = clienteConsumidor.getConsumidor().getUsuario().getNome();
			responseEntity = new ResponseEntity<>(clienteConsumidor);
			try {
				clienteConsumidor = validarConsumidorJaCadastrado(telefone);
				responseEntity.addMessage(new AnotaaiMessage(IMessage.CONSUMIDOR_JA_CADASTRADO, TipoMensagem.ERROR,
						IMessage.KEEP_ALIVE_TIME_VIEW, nome));
				// o consumidor ja esta cadastrado para este cliente
			} catch (NoResultException e) {
				// o consumidor ja esta cadastrado para outro cliente
				message = new AnotaaiMessage(IMessage.CONSUMIDOR_JA_REGISTRADO, TipoMensagem.WARNING,
						IMessage.KEEP_ALIVE_TIME_VIEW, nome);
				responseEntity.setMessages(new ArrayList<>());
				responseEntity.getMessages().add(message);
			}
			responseEntity.setIsValid(Boolean.FALSE);
		} catch (NoResultException nre) {
			responseEntity = new ResponseEntity<>();
			responseEntity.setIsValid(Boolean.TRUE);
		}
		return responseEntity;
	}

	public ClienteConsumidor validarConsumidorJaCadastrado(Telefone telefone) {
		Cliente cliente = appManager.getAppService().getCliente();
		TypedQuery<ClienteConsumidor> q = em.createNamedQuery(ClienteConsumidorConstant.FIND_BY_TELEFONE_KEY,
				ClienteConsumidor.class);
		q.setParameter(Telefone.TelefoneConstant.FIELD_DDI, telefone.getDdi());
		q.setParameter(Telefone.TelefoneConstant.FIELD_DDD, telefone.getDdd());
		q.setParameter(Telefone.TelefoneConstant.FIELD_NUMERO, telefone.getNumero());
		q.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, cliente);
		return q.getSingleResult();
	}

	public Usuario findUsuarioByTelefone(Telefone telefone) {
		TypedQuery<Usuario> q = null;
		Usuario usuario = null;
		q = em.createNamedQuery(UsuarioConstant.FIND_BY_TELEFONE_KEY, Usuario.class);
		q.setParameter(TelefoneConstant.FIELD_DDI, telefone.getDdi());
		q.setParameter(TelefoneConstant.FIELD_DDD, telefone.getDdd());
		q.setParameter(TelefoneConstant.FIELD_NUMERO, telefone.getNumero());
		usuario = q.getSingleResult();
		return usuario;
	}

	public ResponseEntity<Usuario> isEditable(Usuario usuario) {
		ResponseEntity<Usuario> responseEntity = new ResponseEntity<>();
		Long qtdClientes = null;
		TypedQuery<SituacaoUsuario> querySituacao = em.createNamedQuery(Usuario.UsuarioConstant.SITUACAO_USUARIO_KEY,
				SituacaoUsuario.class);
		querySituacao.setParameter(Consumidor.ConsumidorConstant.FIELD_USUARIO, usuario);
		SituacaoUsuario situacao = querySituacao.getSingleResult();

		if (!situacao.equals(SituacaoUsuario.NAO_REGISTRADO)) {
			responseEntity.addMessage(new AnotaaiMessage(IMessage.EDICAO_EXCLUSIVA_USUARIO_JA_CADASTRADO,
							TipoMensagem.WARNING, IMessage.KEEP_ALIVE_TIME_VIEW, usuario.getNome()));
			responseEntity.setIsValid(Boolean.FALSE);
		} else {
			TypedQuery<Long> queryCont = em
					.createNamedQuery(ClienteConsumidor.ClienteConsumidorConstant.COUNT_USUARIO_KEY, Long.class);
			queryCont.setParameter(Consumidor.ConsumidorConstant.FIELD_USUARIO, usuario);
			qtdClientes = queryCont.getSingleResult();
			// exites apenas um clietne associado ao usuario
			responseEntity.setIsValid(qtdClientes == 1);
			if (!responseEntity.getIsValid()) {
				responseEntity.addMessage(new AnotaaiMessage(IMessage.EDICAO_EXCLUSIVA_USUARIO_MULTIPLOS_CLIENTES,
								TipoMensagem.WARNING, IMessage.KEEP_ALIVE_TIME_VIEW, usuario.getNome()));
			}
		}
		return responseEntity;
	}
	
	

	public ResponseEntity<ClienteConsumidor> recomendarEdicao(ClienteConsumidor clienteConsumidor) throws AppException {

		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		clienteConsumidor.setCliente(appManager.getAppService().getCliente());
		AnotaaiMessage message = new AnotaaiMessage(IMessage.RECOMENDACAO_EDICAO_ENVIADA, TipoMensagem.INFO, IMessage.LONG_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome());
		responseEntity.addMessage(message);
		appManager.getSenderMail().recomendarEdicaoDeCadastro(clienteConsumidor);
		responseEntity.setIsValid(Boolean.TRUE);
		return responseEntity;
	}
	
	public ResponseEntity<ClienteConsumidor> findById(Long id) throws AppException {
		ResponseEntity<ClienteConsumidor> entity = new ResponseEntity<>();
		Cliente cliente = appManager.getAppService().getCliente();
		try {
			TypedQuery<ClienteConsumidor> query = em.createNamedQuery(ClienteConsumidorConstant.FIND_BY_ID_KEY, ClienteConsumidor.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			query.setParameter(Constant.Entity.CLIENTE, cliente);
			ClienteConsumidor clienteConsumidor = query.getSingleResult();
			entity.setEntity(clienteConsumidor);
			
			Long qtdClientes = null;
			
			
			if (!clienteConsumidor.getConsumidor().getUsuario().getSituacao().equals(SituacaoUsuario.NAO_REGISTRADO)) {
				entity.addMessage(new AnotaaiMessage(IMessage.EDICAO_EXCLUSIVA_USUARIO_JA_CADASTRADO, TipoMensagem.WARNING, IMessage.KEEP_ALIVE_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
				entity.setIsValid(Boolean.FALSE);
				return entity;
			} else {
				TypedQuery<Long> queryCont = em.createNamedQuery(ClienteConsumidor.ClienteConsumidorConstant.COUNT_USUARIO_KEY, Long.class);
				queryCont.setParameter(Consumidor.ConsumidorConstant.FIELD_USUARIO, clienteConsumidor.getConsumidor().getUsuario());
				qtdClientes = queryCont.getSingleResult();
				// exites apenas um clietne associado ao usuario
				entity.setIsValid(qtdClientes == 1);
				if (!entity.getIsValid()) {
					entity.addMessage(new AnotaaiMessage(IMessage.EDICAO_EXCLUSIVA_USUARIO_MULTIPLOS_CLIENTES,TipoMensagem.WARNING, IMessage.KEEP_ALIVE_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
				}
			}
			
			
			
			entity.setIsValid(Boolean.TRUE);
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}
	
	public ResponseEntity<ClienteConsumidor> update(Long id, ClienteConsumidor entity) throws AppException {
		ClienteConsumidor clienteConsumidor = null;
		AnotaaiMessage message = null;
		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		AppException appException = null;
		if (entity != null && id != null && id.equals(entity.getId())) {
			clienteConsumidor = em.find(ClienteConsumidor.class, id);
			//Usuario oldUsuario =  em.find(Usuario.class, clienteConsumidor.getConsumidor().getUsuario().getId());
			mergeUsuario(entity.getConsumidor().getUsuario(), clienteConsumidor.getConsumidor().getUsuario());
			em.merge(clienteConsumidor.getConsumidor().getUsuario());
			message = new AnotaaiMessage(IMessage.ENTIDADE_EDICAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome());
			responseEntity.setMessages(new ArrayList<>());
			responseEntity.getMessages().add(message);
		} else {
			responseEntity.addMessage(IMessage.ILLEGAL_ARGUMENT, TipoMensagem.ERROR,IMessage.KEEP_ALIVE_TIME_VIEW);
			appException = new AppException(responseEntity);
			throw appException;
		}
		return responseEntity;
	}
	
	private void mergeUsuario(Usuario newUsuario, Usuario oldUsuario) {
	
		oldUsuario.setNome(newUsuario.getNome());
		oldUsuario.setEmail(newUsuario.getEmail());
		oldUsuario.getTelefone().setNumero(newUsuario.getTelefone().getNumero());
		oldUsuario.getTelefone().setDdd(newUsuario.getTelefone().getDdd());
		oldUsuario.getTelefone().setDdi(newUsuario.getTelefone().getDdi());
	
	}
	
	public  List<Consumidor> getConsumersByName(String nome) {
		TypedQuery<Consumidor> consumidorQuery = null;
		Cliente cliente = appManager.getAppService().getCliente();
		consumidorQuery =  em.createNamedQuery(Consumidor.ConsumidorConstant.FIND_BY_NOME_KEY, Consumidor.class);
		consumidorQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, cliente);
		consumidorQuery.setParameter("nome", nome);
		final List<Consumidor> results = consumidorQuery.getResultList();
		return results;
	}

}
