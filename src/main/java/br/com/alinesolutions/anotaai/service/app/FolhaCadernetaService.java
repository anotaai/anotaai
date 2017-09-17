package br.com.alinesolutions.anotaai.service.app;

import java.time.LocalDate;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.model.usuario.Consumidor;
import br.com.alinesolutions.anotaai.model.venda.Caderneta;
import br.com.alinesolutions.anotaai.model.venda.FolhaCaderneta;
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
		FolhaCaderneta folhaCaderneta = null;
		try {
			throw new NoResultException();
		} catch (NoResultException e) {
			FolhaCaderneta folha = new FolhaCaderneta();
			folha.setConsumidor(em.getReference(Consumidor.class, consumidor.getId()));
			caderneta = em.getReference(Caderneta.class, caderneta.getId());
			folha.setCaderneta(caderneta);
			folha.setDataCriacao(new Date());
			
			Integer diaBase = caderneta.getConfiguracao().getDiaBase();
			Integer qtdDiasDuracaoFolha = caderneta.getConfiguracao().getQtdDiasDuracaoFolha();
			Integer diaHoje = LocalDate.now().getDayOfMonth();
			
			Integer inicio = null;
			Integer termino = null;

			//se o dia base estiver entre hoje e a soma dos dias de duracao da folha
			if (diaHoje - diaBase <= qtdDiasDuracaoFolha && qtdDiasDuracaoFolha - diaHoje >= 0) {
				inicio = diaBase;
				termino = qtdDiasDuracaoFolha;
			} else {
				//cria o periodo de acordo com o dia 
				inicio = (diaHoje - (diaHoje % qtdDiasDuracaoFolha));
				termino = inicio + qtdDiasDuracaoFolha;
			}
			System.out.println(termino);
			
			
		}
		return folhaCaderneta;
	}
	

}
