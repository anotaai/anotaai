package br.com.alinesolutions.anotaai.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.metadata.model.domain.ItemMenu;
import br.com.alinesolutions.anotaai.metadata.model.domain.Menu;
import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.model.usuario.UsuarioPerfil;
import br.com.alinesolutions.anotaai.service.AppService;
import br.com.alinesolutions.anotaai.util.Constant;

@Stateless
@Path("/menu")
public class MenuEndpoint {

	@PersistenceContext(unitName = Constant.App.UNIT_NAME)
	private EntityManager em;

	@EJB
	private AppService appService;

	@GET()
	@Path("/principal")
	@Produces(MediaType.APPLICATION_JSON)
	public Response principal() throws Exception {

		List<UsuarioPerfil> perfisUsuario = appService.getUsuario().getPerfis();
		List<Perfil> perfis = new ArrayList<>();
		for (UsuarioPerfil usuarioPerfil : perfisUsuario) {
			perfis.add(usuarioPerfil.getPerfil());
		}
		Map<Perfil, List<ItemMenu>> menuMap = buildMenuMap(Menu.PRINCIPAL, perfis);

		Set<Entry<Perfil, List<ItemMenu>>> entrySet = menuMap.entrySet();

		List<ItemMenu> itensMenu = new ArrayList<>();
		for (Entry<Perfil, List<ItemMenu>> entry : entrySet) {
			for (ItemMenu itemMenu : entry.getValue()) {
				itensMenu.add(itemMenu);
			}
		}
		return Response.ok(itensMenu).build();
	}

	private Map<Perfil, List<ItemMenu>> buildMenuMap(Menu menu, List<Perfil> perfis) throws Exception {
		Map<Perfil, List<ItemMenu>> mapMenus = new HashMap<>();

		List<ItemMenu> itensMenu = null;
		for (Perfil perfil : perfis) {
			itensMenu = new ArrayList<ItemMenu>();
			for (ItemMenu itemMenu : ItemMenu.values()) {
				if (itemMenu.getMenu().equals(menu) && itemMenu.getPerfil().equals(perfil)) {
					itensMenu.add(itemMenu);
				}
			}
			mapMenus.put(perfil, itensMenu);
		}
		return mapMenus;
	}

}
