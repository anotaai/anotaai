package br.com.alinesolutions.anotaai.service.app;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta.FolhaCadernetaConstant;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.service.ResponseUtil;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
public class FolhaCadernetaService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@EJB
	private ResponseUtil responseUtil;

	@Resource
	private SessionContext sessionContext;
	

	public ResponseEntity<Caderneta> listAll(Integer startPosition, Integer maxResult, String descricao) throws AppException {

		ResponseEntity<Caderneta> responseEntity = new ResponseEntity<>();
		//Cliente cliente = appService.getCliente();
		return responseEntity;
	}


	public FolhaCaderneta recuperarFolhaCaderneta(Caderneta caderneta, Consumidor consumidor) {
		FolhaCaderneta folha = null;
		try {
			TypedQuery<FolhaCaderneta> query = em.createNamedQuery(FolhaCadernetaConstant.FIND_FOLHA_VIGENTE, FolhaCaderneta.class);
			query.setParameter("hoje", new Date());
			folha = query.getSingleResult();
		} catch (NoResultException e) {
			folha = new FolhaCaderneta();
			folha.setConsumidor(em.getReference(Consumidor.class, consumidor.getId()));
			caderneta = em.getReference(Caderneta.class, caderneta.getId());
			folha.setCaderneta(caderneta);
			folha.setDataCriacao(new Date());
			Integer diaBase = caderneta.getConfiguracao().getDiaBase();
			Integer qtdDiasDuracaoFolha = caderneta.getConfiguracao().getQtdDiasDuracaoFolha();
			LocalDate dataInicial = calcularDataInicioFolhaCaderneta(diaBase, qtdDiasDuracaoFolha);
			LocalDate dataFinal = dataInicial.plusDays(qtdDiasDuracaoFolha);
			folha.setDataInicio(Date.from(dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			folha.setDataTermino(Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			em.persist(folha);
		}
		return folha;
	}
	
	private LocalDate calcularDataInicioFolhaCaderneta(Integer diaBase, Integer qtdDiasDuracaoFolha) {
		LocalDate now = LocalDate.now();
		Integer countDay = 0;
		//quantidade de dias entre o dia base e a data atual
		while (!diaBase.equals(now.getDayOfMonth())) {
			countDay++;
			now = now.plusDays(-1);
		}
		LocalDate dataBase = LocalDate.now().plusDays(countDay * -1);
		LocalDate diaInicioFolhaCaderneta = dataBase;
		now = LocalDate.now();
		while (dataBase.isBefore(now)) {
			dataBase = dataBase.plusDays(qtdDiasDuracaoFolha);
			if (dataBase.isBefore(now)) {
				diaInicioFolhaCaderneta = dataBase;	
			}
		}
		return diaInicioFolhaCaderneta;
	}
}
