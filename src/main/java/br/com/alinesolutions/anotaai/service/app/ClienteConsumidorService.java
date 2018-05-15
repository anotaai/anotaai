package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
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
import br.com.alinesolutions.anotaai.infra.AnotaaiUtil;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.ShardingResourceFactory;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.BaseEntity.BaseEntityConstant;
import br.com.alinesolutions.anotaai.model.domain.SituacaoConsumidor;
import br.com.alinesolutions.anotaai.model.domain.SituacaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor;
import br.com.alinesolutions.anotaai.model.usuario.ClienteConsumidor.ClienteConsumidorConstant;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor.ConsumidorConstant;
import br.com.alinesolutions.anotaai.model.usuario.Preferencia;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Telefone.TelefoneConstant;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.Usuario.UsuarioConstant;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.ResponseUtil;

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
		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		ClienteConsumidor retorno = clienteConsumidor.clone();
		Cliente cliente = appManager.getAppService().getCliente();
		Boolean isNewUser = null;
		try {
			//verifica se o consumidor ja esta cadastrado com o telefone informado
			TypedQuery<Consumidor> consumidorQuery = em.createNamedQuery(ConsumidorConstant.FIND_BY_TELEFONE_KEY, Consumidor.class);
			Telefone telefone = clienteConsumidor.getConsumidor().getUsuario().getTelefone();
			consumidorQuery.setParameter(TelefoneConstant.FIELD_DDI, telefone.getDdi());
			consumidorQuery.setParameter(TelefoneConstant.FIELD_DDD, telefone.getDdd());
			consumidorQuery.setParameter(TelefoneConstant.FIELD_NUMERO, telefone.getNumero());
			Consumidor consumidorDatabase = consumidorQuery.getSingleResult();			
			TypedQuery<ClienteConsumidor> clienteConsumidorQuery = em.createNamedQuery(ClienteConsumidorConstant.LOAD_BY_CONSUMIDOR_KEY, ClienteConsumidor.class);
			clienteConsumidorQuery.setParameter(BaseEntityConstant.FIELD_CONSUMIDOR, consumidorDatabase);
			clienteConsumidorQuery.setParameter(BaseEntityConstant.FIELD_CLIENTE, cliente);
			try {
				//verifica se ja criou clienteconsumidor para o telefone informado
				ClienteConsumidor clienteConsumidorDatabase = clienteConsumidorQuery.getSingleResult();
				if (clienteConsumidorDatabase.getSituacao().equals(SituacaoConsumidor.INATIVO)) {
					clienteConsumidorDatabase.setSituacao(SituacaoConsumidor.ATIVO);
					em.merge(clienteConsumidorDatabase);
					clienteConsumidor = clienteConsumidorDatabase;
				} else {
					responseEntity.setIsValid(Boolean.FALSE);
					responseEntity.setEntity(clienteConsumidor);
					responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
					throw new AppException(responseEntity);
				}
			} catch (NoResultException e) {
				clienteConsumidor.setConsumidor(consumidorDatabase);
				clienteConsumidor.setSituacao(SituacaoConsumidor.ATIVO);
				clienteConsumidor.setCliente(cliente);
				em.persist(clienteConsumidor);
			}
			isNewUser = Boolean.FALSE;
		} catch (NoResultException e) {
			isNewUser = Boolean.TRUE;
			clienteConsumidor.setCliente(cliente);
			clienteConsumidor.setDataAssociacao(AnotaaiUtil.getInstance().now());
			clienteConsumidor.getConsumidor().setDataCadastro(AnotaaiUtil.getInstance().now());
			clienteConsumidor.setSituacao(SituacaoConsumidor.ATIVO);
			Usuario usuario = clienteConsumidor.getConsumidor().getUsuario();
			usuario.setId(null);
			usuario.setEmail(null);
			usuario.setNome(null);
			usuario.setDataCadastro(AnotaaiUtil.getInstance().now());
			usuario.setPerfis(new ArrayList<>());
			usuario.getPerfis().add(new UsuarioPerfil(usuario, Perfil.CONSUMIDOR));
			usuario.setPreferencia(new Preferencia());
			usuario.setSituacao(SituacaoUsuario.NAO_REGISTRADO);
			usuario.setCodigoAtivacao(UUID.randomUUID().toString());
			usuario.setSituacao(SituacaoUsuario.NAO_REGISTRADO);
			clienteConsumidor.getConsumidor().setUsuario(usuario);
			em.persist(clienteConsumidor);
		}
		retorno.setId(clienteConsumidor.getId());
		notify(clienteConsumidor, isNewUser);
		retorno.setId(clienteConsumidor.getId());
		responseEntity.setEntity(retorno);
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, clienteConsumidor.getNomeConsumidor()));
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
		 
		ClienteConsumidor clienteConsumidor = em.find(ClienteConsumidor.class, id);
		if (clienteConsumidor != null) {
			clienteConsumidor.setSituacao(SituacaoConsumidor.INATIVO);
			em.merge(clienteConsumidor);
			entity.setIsValid(Boolean.TRUE);
			entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO,TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
		} else {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public ResponseEntity<ClienteConsumidor> listAll(Integer startPosition, Integer maxResult, String nome) throws AppException {
		TypedQuery<ClienteConsumidor> consumidorQuery = null;
		Cliente cliente = appManager.getAppService().getCliente();
		if(!"".equals(nome)) {
			consumidorQuery = em.createNamedQuery(ClienteConsumidorConstant.FIND_BY_NOME_KEY, ClienteConsumidor.class);
			consumidorQuery.setParameter("nome", nome);
		} else  {
			consumidorQuery = em.createNamedQuery(ClienteConsumidorConstant.LIST_CLIENTE_CONSUMIDOR_KEY, ClienteConsumidor.class);
		}
		consumidorQuery.setParameter(ClienteConsumidorConstant.FIELD_SITUACAO, SituacaoConsumidor.ATIVO);
		consumidorQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, cliente);
		if (startPosition != null) {
			consumidorQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			consumidorQuery.setMaxResults(maxResult);
		}
		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		ResponseList<ClienteConsumidor> responseList = new ResponseList<>();
		responseEntity.setList(responseList);
		
		responseList.setItens(consumidorQuery.getResultList());
		
		TypedQuery<Long> countAll = null;
		
		if(!"".equals(nome)) {
			countAll  = em.createNamedQuery(Consumidor.ConsumidorConstant.FIND_BY_NOME_COUNT, Long.class);
			countAll.setParameter("nome", nome);
		} else {
			countAll  = em.createNamedQuery(Consumidor.ConsumidorConstant.LIST_ALL_COUNT, Long.class);
		}
		 
		countAll.setParameter(Constant.Entity.CLIENTE, cliente);
		countAll.setParameter(ClienteConsumidorConstant.FIELD_SITUACAO, SituacaoConsumidor.ATIVO);
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
				responseEntity.addMessage(new AnotaaiMessage(IMessage.CONSUMIDOR_JACADASTRADO, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW, nome));
				// o consumidor ja esta cadastrado para este cliente
			} catch (NoResultException e) {
				// o consumidor ja esta cadastrado para outro cliente
				message = new AnotaaiMessage(IMessage.CONSUMIDOR_JAREGISTRADO, TipoMensagem.WARNING, Constant.App.KEEP_ALIVE_TIME_VIEW, nome);
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
			responseEntity.addMessage(new AnotaaiMessage(IMessage.USUARIO_EDICAOEXCLUSIVA, TipoMensagem.WARNING, Constant.App.KEEP_ALIVE_TIME_VIEW, usuario.getNome()));
			responseEntity.setIsValid(Boolean.FALSE);
		} else {
			TypedQuery<Long> queryCont = em
					.createNamedQuery(ClienteConsumidor.ClienteConsumidorConstant.COUNT_USUARIO_KEY, Long.class);
			queryCont.setParameter(Consumidor.ConsumidorConstant.FIELD_USUARIO, usuario);
			qtdClientes = queryCont.getSingleResult();
			// exites apenas um clietne associado ao usuario
			responseEntity.setIsValid(qtdClientes == 1);
			if (!responseEntity.getIsValid()) {
				responseEntity.addMessage(new AnotaaiMessage(IMessage.USUARIO_EDICAOEXCLUSIVAMULTIPLOSCLIENTES,
								TipoMensagem.WARNING, Constant.App.KEEP_ALIVE_TIME_VIEW, usuario.getNome()));
			}
		}
		return responseEntity;
	}
	
	

	public ResponseEntity<ClienteConsumidor> recomendarEdicao(ClienteConsumidor clienteConsumidor) throws AppException {

		ResponseEntity<ClienteConsumidor> responseEntity = new ResponseEntity<>();
		clienteConsumidor.setCliente(appManager.getAppService().getCliente());
		AnotaaiMessage message = new AnotaaiMessage(IMessage.USUARIO_RECOMENDACAOEDICAOENVIADA, TipoMensagem.INFO, Constant.App.LONG_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome());
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
				entity.addMessage(new AnotaaiMessage(IMessage.USUARIO_EDICAOEXCLUSIVA, TipoMensagem.WARNING, Constant.App.KEEP_ALIVE_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
				entity.setIsValid(Boolean.FALSE);
				return entity;
			} else {
				TypedQuery<Long> queryCont = em.createNamedQuery(ClienteConsumidor.ClienteConsumidorConstant.COUNT_USUARIO_KEY, Long.class);
				queryCont.setParameter(Consumidor.ConsumidorConstant.FIELD_USUARIO, clienteConsumidor.getConsumidor().getUsuario());
				qtdClientes = queryCont.getSingleResult();
				// exites apenas um clietne associado ao usuario
				entity.setIsValid(qtdClientes == 1);
				if (!entity.getIsValid()) {
					entity.addMessage(new AnotaaiMessage(IMessage.USUARIO_EDICAOEXCLUSIVAMULTIPLOSCLIENTES,TipoMensagem.WARNING, Constant.App.KEEP_ALIVE_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome()));
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
			message = new AnotaaiMessage(IMessage.ENTIDADE_EDICAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, clienteConsumidor.getConsumidor().getUsuario().getNome());
			responseEntity.getMessages().add(message);
		} else {
			responseEntity.addMessage(IMessage.ERRO_ILLEGALARGUMENT, TipoMensagem.ERROR, Constant.App.KEEP_ALIVE_TIME_VIEW);
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
	
	public ResponseEntity<ClienteConsumidor> getConsumersByName(String nomeConsumidor) {
		TypedQuery<ClienteConsumidor> consumidorQuery = null;
		Cliente cliente = appManager.getAppService().getCliente();
		consumidorQuery = em.createNamedQuery(ClienteConsumidorConstant.FIND_BY_NOME_CONSUMIDOR_KEY, ClienteConsumidor.class);
		consumidorQuery.setParameter(BaseEntity.BaseEntityConstant.FIELD_CLIENTE, cliente);
		consumidorQuery.setParameter(ClienteConsumidorConstant.FIELD_SITUACAO, SituacaoConsumidor.ATIVO);
		consumidorQuery.setParameter(ClienteConsumidorConstant.FIELD_NOME_CONSUMIDOR, nomeConsumidor);
		ResponseEntity<ClienteConsumidor> resp = new ResponseEntity<>();
		resp.setList(new ResponseList<>(consumidorQuery.getResultList()));
		return resp;
	}

}
