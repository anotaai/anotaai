package br.com.alinesolutions.anotaai.service.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.i18n.IMessage;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.io.ResponseList;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.Caderneta.CadernetaConstant;
import br.com.alinesolutions.anotaai.model.venda.ConfiguracaoCaderneta;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class CadernetaService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Resource
	private SessionContext sessionContext;
	

	public ResponseEntity<Caderneta> listAll(Integer startPosition, Integer maxResult, String descricao)
			throws AppException {

		Cliente cliente = appService.getCliente();
		TypedQuery<Caderneta> cadernetaQuery = null;

		if (!"".equals(descricao)) {
			cadernetaQuery = em.createNamedQuery(CadernetaConstant.FIND_BY_DESCRICAO_LIKE_KEY, Caderneta.class);
			cadernetaQuery.setParameter(CadernetaConstant.FIELD_DESCRICAO , descricao.toUpperCase());
		} else {
			cadernetaQuery = em.createNamedQuery(CadernetaConstant.LIST_ALL_KEY, Caderneta.class);
		}

		cadernetaQuery.setParameter(Constant.Entity.CLIENTE, cliente);
		ResponseEntity<Caderneta> responseEntity = new ResponseEntity<>();
		ResponseList<Caderneta> responseList = new ResponseList<Caderneta>();
		responseEntity.setList(responseList);

		if (startPosition != null) {
			cadernetaQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			cadernetaQuery.setMaxResults(maxResult);
		}
		final List<Caderneta> results = cadernetaQuery.getResultList();
		responseList.setItens(results);

		TypedQuery<Long> countAll = null;

		if (!"".equals(descricao)) {
			countAll = em.createNamedQuery(CadernetaConstant.FIND_BY_DESCRICAO_COUNT, Long.class);
			countAll.setParameter(CadernetaConstant.FIELD_DESCRICAO , descricao.toUpperCase());
		} else {
			countAll = em.createNamedQuery(CadernetaConstant.LIST_ALL_COUNT, Long.class);
		}

		countAll.setParameter(Constant.Entity.CLIENTE, cliente);
		responseList.setQtdTotalItens(countAll.getSingleResult());
		return responseEntity;
	}

	public ResponseEntity<ConfiguracaoCaderneta> findById(Long id) throws AppException {
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = new ResponseEntity<>();
		TypedQuery<ConfiguracaoCaderneta> query = em.createNamedQuery(CadernetaConstant.EDIT_KEY,
				ConfiguracaoCaderneta.class);
		query.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, id);
		ConfiguracaoCaderneta configuracaoCaderneta = query.getSingleResult();
		responseEntity.setIsValid(true);
		if (responseEntity.getIsValid()) {
			configuracaoCaderneta.setCadernetas(loadCadernetas(configuracaoCaderneta));
			responseEntity.setEntity(configuracaoCaderneta);
		} else {
			responseUtil.buildIllegalArgumentException(responseEntity);
		}
		return responseEntity;
	}

	private List<Caderneta> loadCadernetas(ConfiguracaoCaderneta configuracaoCaderneta) throws AppException {
		TypedQuery<Caderneta> query = em.createNamedQuery(CadernetaConstant.CADERNETA_BY_CONFIGURACAO_KEY,
				Caderneta.class);
		query.setParameter(Constant.Entity.CONFIGURACAO_CADERNETA, configuracaoCaderneta);
		List<Caderneta> cadernetas = query.getResultList();
		return cadernetas;
	}

	public ResponseEntity<ConfiguracaoCaderneta> create(ConfiguracaoCaderneta configuracaoCaderneta)
			throws AppException {

		Cliente cliente = appService.getCliente();
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = new ResponseEntity<>();

		for (Caderneta caderneta : configuracaoCaderneta.getCadernetas()) {
			caderneta.setDataAbertura(new Date());
			caderneta.setDataFechamento(new Date());
			caderneta.setCliente(cliente);
			caderneta.setConfiguracao(configuracaoCaderneta);
		}

		em.persist(configuracaoCaderneta);
		ConfiguracaoCaderneta caderdetaNova = new ConfiguracaoCaderneta(configuracaoCaderneta.getId());
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.setEntity(caderdetaNova);
		responseEntity.setMessages(new ArrayList<>());
		responseEntity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_GRAVACAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, CadernetaConstant.CADERNETA));

		return responseEntity;
	}

	public ResponseEntity<ConfiguracaoCaderneta> removeByBookId(Long id) throws AppException {
		ResponseEntity<ConfiguracaoCaderneta> entity = new ResponseEntity<>();

		try {
			Caderneta caderneta = em.find(Caderneta.class, id);
			ConfiguracaoCaderneta configuracaoCaderneta = em.find(ConfiguracaoCaderneta.class, caderneta.getConfiguracao().getId());
		 
			if(configuracaoCaderneta.getCadernetas().size() == 1) {
				em.remove(configuracaoCaderneta);
			} else {
				 for (Iterator<Caderneta> iterator = configuracaoCaderneta.getCadernetas().iterator(); iterator.hasNext();) {
					Caderneta c = (Caderneta) iterator.next();
					if(c.getId().equals(id)) {
						em.remove(c);
						iterator.remove();
					}
					
				}
			}
			
			entity.setIsValid(Boolean.TRUE);
			entity.setMessages(new ArrayList<>());
			entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, CadernetaConstant.CADERNETA));
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}
	
	
	public ResponseEntity<ConfiguracaoCaderneta> deleteById(Long id) throws AppException {
		ResponseEntity<ConfiguracaoCaderneta> entity = new ResponseEntity<>();

		try {
			ConfiguracaoCaderneta configuracaoCaderneta = em.find(ConfiguracaoCaderneta.class, id);
			em.remove(configuracaoCaderneta);
			entity.setIsValid(Boolean.TRUE);
			entity.setMessages(new ArrayList<>());
			entity.getMessages().add(new AnotaaiMessage(IMessage.ENTIDADE_EXCLUSAO_SUCESSO, TipoMensagem.SUCCESS,IMessage.DEFAULT_TIME_VIEW, CadernetaConstant.CADERNETA));
		} catch (NoResultException e) {
			responseUtil.buildIllegalArgumentException(entity);
		}
		return entity;
	}

	public ResponseEntity<ConfiguracaoCaderneta> update(Long id, ConfiguracaoCaderneta entity) throws AppException {
		ConfiguracaoCaderneta configuracaoUdate = em.find(ConfiguracaoCaderneta.class, entity.getId());
		updateCadernetas(configuracaoUdate,entity);
		em.merge(configuracaoUdate);
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = new ResponseEntity<>(entity);
		responseEntity.setIsValid(Boolean.TRUE);
		responseEntity.addMessage(new AnotaaiMessage(IMessage.ENTIDADE_EDICAO_SUCESSO, TipoMensagem.SUCCESS, IMessage.DEFAULT_TIME_VIEW, CadernetaConstant.CADERNETA));
		return responseEntity;
	}
	
	private void updateCadernetas(ConfiguracaoCaderneta configuracaoUdate, ConfiguracaoCaderneta entity) {

		Iterator<Caderneta> iteratorCadernetas = configuracaoUdate.getCadernetas().iterator();
		Caderneta c = null;
		Cliente cliente = appService.getCliente();

		while (iteratorCadernetas.hasNext()) {
			c = iteratorCadernetas.next();
			if (!entity.getCadernetas().contains(c)) {
				em.remove(c);
				iteratorCadernetas.remove();
			}
		}

		for (Caderneta caderneta : entity.getCadernetas()) {
			caderneta.setDataAbertura(new Date());
			caderneta.setDataFechamento(new Date());
			Cliente currentClient = new Cliente();
			currentClient.setId(cliente.getId());
			caderneta.setCliente(currentClient);
			if (caderneta.getId() == null) {
				caderneta.setConfiguracao(new ConfiguracaoCaderneta(configuracaoUdate.getId()));
				configuracaoUdate.getCadernetas().add(caderneta);
			} else {
				for (Caderneta cadernetaUpdate : configuracaoUdate.getCadernetas()) {
					if (cadernetaUpdate.getId().equals(caderneta.getId())) {
						cadernetaUpdate.setDescricao(caderneta.getDescricao());
						break;
					}
				}
			}
		}
	}
	
	
	public ResponseEntity<ConfiguracaoCaderneta> checkSameConfiguration(ConfiguracaoCaderneta entity) throws AppException {

		Cliente cliente = appService.getCliente();
		ResponseEntity<ConfiguracaoCaderneta> responseEntity = new ResponseEntity<>();
		responseEntity.setIsValid(Boolean.TRUE);
		
		try {
			TypedQuery<ConfiguracaoCaderneta> configuracaoCadernetaQuery = em.createNamedQuery(CadernetaConstant.CADERNETA_BY_KEYS, ConfiguracaoCaderneta.class);
			configuracaoCadernetaQuery.setParameter("qtdDiasDuracaoFolha", entity.getQtdDiasDuracaoFolha());
			configuracaoCadernetaQuery.setParameter("diaBase", entity.getDiaBase());
			configuracaoCadernetaQuery.setParameter("cliente", cliente);
			configuracaoCadernetaQuery.setParameter("id", entity.getId() == null ? 0 : entity.getId());
			ConfiguracaoCaderneta configuracaoCaderneta = configuracaoCadernetaQuery.getSingleResult();
		    responseEntity.setEntity(configuracaoCaderneta);
			responseEntity.addMessage(IMessage.ENTIDADE_JA_CADASTRADA, TipoMensagem.WARNING,IMessage.KEEP_ALIVE_TIME_VIEW, CadernetaConstant.CADERNETA);
		} catch (NoResultException e) {
            return responseEntity;
		}

		return responseEntity;

	}

	public List<Caderneta> getAppointmentBooks() {
		Cliente cliente = appService.getCliente();
		TypedQuery<Caderneta> cadernetaQuery = em.createNamedQuery(CadernetaConstant.LIST_ALL_KEY, Caderneta.class);
		cadernetaQuery.setParameter(Constant.Entity.CLIENTE, cliente);
	    return cadernetaQuery.getResultList();
	}

}
