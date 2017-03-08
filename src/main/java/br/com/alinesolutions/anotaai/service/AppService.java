package br.com.alinesolutions.anotaai.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alinesolutions.anotaai.metadata.model.Cep;
import br.com.alinesolutions.anotaai.metadata.model.FirebaseConfig;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.util.Constant;
import br.com.alinesolutions.anotaai.util.LoadResource;
import br.com.alinesolutions.anotaai.util.UsuarioUtils;

@Singleton
@Startup
public class AppService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;
	
	@EJB
	private LoadResource loader;

	@Schedule(minute = "*/30", hour = "*")
	public void limparSessao() {
		String findAllKey = SessaoUsuario.SessaoUsuarioConstant.FIND_ALL_KEY;
		TypedQuery<SessaoUsuario> query = em.createNamedQuery(findAllKey, SessaoUsuario.class);
		List<SessaoUsuario> sessoes = query.getResultList();
		for (SessaoUsuario sessaoUsuario : sessoes) {
			try {
				validarTempoSessao(sessaoUsuario.getUltimoAcesso());
			} catch (IllegalStateException e) {
				sessaoUsuario = em.find(SessaoUsuario.class, sessaoUsuario.getId());
				em.remove(sessaoUsuario);
			}
		}
	}

	private void validarTempoSessao(Date inicio) throws IllegalStateException {
		Instant instantInicio = inicio.toInstant();
		Long timeLogged = ChronoUnit.MINUTES.between(instantInicio, Calendar.getInstance().toInstant());
		if (timeLogged > Constant.App.SESSION_TIME) {
			throw new IllegalStateException();
		}
	}

	public Usuario getUsuario() {
		Usuario usuario = UsuarioUtils.getUsuario();
		if (usuario == null) {
			throw new IllegalArgumentException();
		}
		return usuario;
	}

	public Cliente getCliente() {
		Cliente cliente = UsuarioUtils.getCliente();
		if (cliente == null) {
			Usuario usuario = getUsuario();
			TypedQuery<Cliente> query = em.createNamedQuery(Cliente.ClienteConstant.FIND_BY_USUARIO_KEY, Cliente.class);
			query.setParameter(Cliente.ClienteConstant.FIELD_USUARIO, usuario);
			try {
				cliente = query.getSingleResult();
				UsuarioUtils.putCliente(cliente);
			} catch (NoResultException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return cliente;
	}

	public Cep findCep(Integer nrCep) {
		Cep cep = null;
		StringBuffer url = new StringBuffer();
		url.append("http://viacep.com.br/ws/");
		url.append(nrCep);
		url.append("/json");
		HttpClient client = new DefaultHttpClient();
		HttpGet method = new HttpGet(url.toString());
		try {
			HttpResponse response = client.execute(method);
			HttpEntity entity = response.getEntity();
			String resultado = EntityUtils.toString(entity, "UTF-8");
			if (resultado != null) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				cep = gson.fromJson(resultado, Cep.class);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		return cep;
	}

	/**
	 * Quando o cadastro é feito com contas de email do google, o ponto deve ser
	 * desconsiderado do nickname, o email serah gravado sem o ponto, e antes de
	 * qualquer consuta o ponto deverah ser removido
	 * 
	 * @param email
	 * @return
	 */
	public String atulaizarEmail(String email) {
		Integer indexInicioProvedor = null;
		String nickName = null;
		String retorno = null;
		if (email != null) {
			if (email.indexOf("@gmail.com") > -1) {
				StringBuilder novoEmail = new StringBuilder();
				indexInicioProvedor = email.indexOf("@gmail.com");
				nickName = email.substring(0, indexInicioProvedor).replace(".", "");
				novoEmail.append(nickName).append("@gmail.com");
				retorno = novoEmail.toString();
			} else {
				retorno = email;
			}
		}
		return retorno;
	}

	/**
	 * Converte uma string contendo apenas numeros em um telefone
	 * 
	 * @param telefoneStr
	 * @return
	 */
	public Telefone buildTelefone(String telefoneStr) {
		Integer ddi = Integer.parseInt(telefoneStr.substring(0, 2));
		Integer ddd = Integer.parseInt(telefoneStr.substring(2, 4));
		Integer numero = Integer.parseInt(telefoneStr.substring(4, telefoneStr.length()));
		return new Telefone(ddi, ddd, numero);
	}

	public FirebaseConfig getFirebaseConfig() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		String config = loader.getFile(Constant.FileNane.FIREBASE_CONFIG);
		FirebaseConfig firebaseConfig = gson.fromJson(config, FirebaseConfig.class);
		return firebaseConfig;
	}

}
