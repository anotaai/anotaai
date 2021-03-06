package br.com.alinesolutions.anotaai.service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Hibernate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alinesolutions.anotaai.infra.AnotaaiUtil;
import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.LoadResource;
import br.com.alinesolutions.anotaai.infra.UsuarioUtils;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AnotaaiMessage;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.Cep;
import br.com.alinesolutions.anotaai.metadata.model.domain.ItemMenu;
import br.com.alinesolutions.anotaai.metadata.model.domain.Menu;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoMensagem;
import br.com.alinesolutions.anotaai.model.SessaoUsuario;
import br.com.alinesolutions.anotaai.model.usuario.Cliente;
import br.com.alinesolutions.anotaai.model.usuario.Telefone;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;

@Singleton
@Startup
public class AppService {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;
	
	@EJB
	private LoadResource loader;
	
	private GoogleCredential scoped;
	
	private static Logger log;
	
	static {
		log = Logger.getLogger(AppService.class.getSimpleName());
	}
	
	@PostConstruct
	private void initGoogleCredential() {
		try {
			GoogleCredential googleCred = GoogleCredential.fromStream(loader.getInputStream(Constant.FileNane.GOOGLE_KEY));
			scoped = googleCred.createScoped(Arrays.asList(Constant.App.GOOGLE_CREDENTIAL_SCOPE));
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	@Schedule(second="*/3000", minute = "*", hour = "*", persistent = false)
	public void limparSessao() {
		String findAllKey = SessaoUsuario.SessaoUsuarioConstant.FIND_ALL_KEY;
		TypedQuery<SessaoUsuario> query = em.createNamedQuery(findAllKey, SessaoUsuario.class);
		List<SessaoUsuario> sessoes = query.getResultList();
		log.log(Level.INFO, new StringBuilder("Sessoes ativar: ").append(sessoes.size()).toString());
		for (SessaoUsuario sessaoUsuario : sessoes) {
			try {
				validarTempoSessao(sessaoUsuario.getUltimoAcesso());
			} catch (IllegalStateException e) {
				sessaoUsuario = em.find(SessaoUsuario.class, sessaoUsuario.getId());
				em.remove(sessaoUsuario);
			}
		}
	}

	private void validarTempoSessao(ZonedDateTime inicio) throws IllegalStateException {
		Long timeLogged = ChronoUnit.MINUTES.between(inicio, AnotaaiUtil.getInstance().now());
		log.log(Level.INFO, timeLogged.toString());
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
				Hibernate.initialize(cliente.getUsuario());
				UsuarioUtils.putCliente(cliente);
			} catch (NoResultException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return cliente;
	}

	public Cep findCep(Integer nrCep) {
		Cep cep = null;
		StringBuilder url = new StringBuilder();
		url.append("http://viacep.com.br/ws/");
		url.append(nrCep);
		url.append("/json");
		HttpClient client = HttpClients.createDefault();
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
			ResponseEntity<?> responseEntity = new ResponseEntity<>();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.addMessage(new AnotaaiMessage("cep.nao.cadastrado", TipoMensagem.WARNING, Constant.App.DEFAULT_TIME_VIEW, cep.toString()));
			throw new AppException(responseEntity);
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
		String key = null;
		if (email != null) {
			if (email.endsWith("@gmail.com")) {
				StringBuilder novoEmail = new StringBuilder();
				indexInicioProvedor = email.indexOf("@gmail.com");
				nickName = email.substring(0, indexInicioProvedor).replace(".", "");
				novoEmail.append(nickName).append("@gmail.com");
				key = novoEmail.toString();
			} else {
				key = email;
			}
		}
		return key;
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

	public String getGooleServiceAccountKey() {
		try {
			scoped.refreshToken();
			return scoped.getAccessToken();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ItemMenu> getItensMenu(Menu menu) {
		final List<Perfil> perfis = getUsuario().getPerfis().stream().map(UsuarioPerfil::getPerfil).collect(Collectors.toList());
		return Arrays.asList(ItemMenu.values()).stream().filter(itemMenu -> Arrays.asList(itemMenu.getPerfis()).stream().
				filter(perfil -> perfis.contains(perfil)).count() > 0 && itemMenu.getMenu().equals(menu)).collect(Collectors.toList());
	}
	
}
