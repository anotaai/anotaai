package br.com.alinesolutions.anotaai.service.app;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
			LocalDate dataAbertura = caderneta.getDataAbertura().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Long diasCaderneta = ChronoUnit.DAYS.between(dataAbertura, LocalDate.now());
			Integer qtdDiasDuracaoFolha = caderneta.getConfiguracao().getQtdDiasDuracaoFolha();
			LocalDate now = LocalDate.now();
			
			Integer diaHoje = now.getDayOfMonth();
			Integer ultimoDiaDoMes = now.withDayOfMonth(now.lengthOfMonth()).getDayOfMonth();
			
			Calendar dataInicial = Calendar.getInstance();
			Calendar dataFinal = Calendar.getInstance();
			
			if (diasCaderneta / qtdDiasDuracaoFolha == 0) {
				dataInicial.set(Calendar.DAY_OF_MONTH, (int) (diasCaderneta % qtdDiasDuracaoFolha * -1));
				dataFinal.set(Calendar.DAY_OF_MONTH, qtdDiasDuracaoFolha);
			}

			Integer diaAbertura = dataAbertura.getDayOfMonth();
			Long diasPrimeiroPeriodo = ChronoUnit.DAYS.between(dataAbertura, dataAbertura.plusDays(diaBase >= diaAbertura ? diaBase % qtdDiasDuracaoFolha : (dataAbertura.withDayOfMonth(dataAbertura.lengthOfMonth()).getDayOfMonth() - diaAbertura) + diaBase));
			Integer diasJaPassadosPeriodoAtual = (int) ((diasCaderneta - diasPrimeiroPeriodo) % qtdDiasDuracaoFolha);
			dataInicial.add(Calendar.DAY_OF_MONTH, diasJaPassadosPeriodoAtual * -1);
			dataFinal.add(Calendar.DAY_OF_MONTH, qtdDiasDuracaoFolha - diasJaPassadosPeriodoAtual);
			
			System.out.println(dataInicial.getTime());
			System.out.println(dataFinal.getTime());
		}
		return folha;
	}
	

}
