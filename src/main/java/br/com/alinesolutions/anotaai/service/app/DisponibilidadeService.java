package br.com.alinesolutions.anotaai.service.app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.produto.Disponibilidade;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Cliente.ClienteConstant;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;

@Stateless
public class DisponibilidadeService {
	
	@Inject
	private EntityManager em;
	
	@EJB
	private AppService appService;
	
	@EJB
	private ResponseUtil responseUtil;
	
	public ResponseEntity<Disponibilidade> create(Disponibilidade disponibilidade) throws AppException {
		TypedQuery<Cliente> q = null;
		Cliente clienteLogado = appService.getCliente();
		Cliente cliente = null;
		ResponseEntity<Disponibilidade> responseEntity = new ResponseEntity<>();
		try {
			q = em.createNamedQuery(ClienteConstant.FIND_BY_PRODUTO_KEY, Cliente.class);
			q.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, disponibilidade.getProduto().getId());
			cliente = q.getSingleResult();
			if (cliente.equals(clienteLogado)) {
				em.persist(disponibilidade);
				responseEntity.setIsValid(Boolean.TRUE);
				responseEntity.setEntity(disponibilidade);
				responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO,
						TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, disponibilidade.getDia().getDescricao()));
			} else {
				responseUtil.buildIllegalArgumentException(responseEntity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(responseEntity);
		}
		return responseEntity;
	}
	
	public ResponseEntity<Disponibilidade> deleteById(Long id) throws AppException {
		ResponseEntity<Disponibilidade> entity = new ResponseEntity<>();
		Cliente clienteLogado = appService.getCliente();
		Cliente clienteDisponibilidade = null;
		Disponibilidade disponibilidade = null;
		try {
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_DISPONIBILIDADE_KEY, Cliente.class);
			query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
			clienteDisponibilidade = query.getSingleResult();
			entity.setIsValid(clienteDisponibilidade.equals(clienteLogado));
			if (entity.getIsValid()) {
				disponibilidade = em.find(Disponibilidade.class, id);
				em.remove(disponibilidade);
				entity.addMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO, TipoMensagem.SUCCESS, Constant.App.DEFAULT_TIME_VIEW, disponibilidade.getDia().getDescricao());
			} else {
				responseUtil.buildIllegalArgumentException(entity);
			}
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

}
