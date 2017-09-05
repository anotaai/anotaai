package br.com.alinesolutions.anotaai.service.app;

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
			//TODO recuperar a folha de caderneta atual
		} catch (NoResultException e) {
			//TODO criar a folha de caderneta
		}
		return folhaCaderneta;
	}
	

}
