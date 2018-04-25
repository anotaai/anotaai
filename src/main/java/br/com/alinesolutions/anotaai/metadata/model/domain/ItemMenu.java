package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ItemMenu {

	CADASTRO_CONSUMIDOR("menu.principal.consumidor", Menu.PRINCIPAL, Icon.USER, "/consumidor", Perfil.CLIENTE),
	GRUPO_PRODUTO("menu.principal.grupoproduto", Menu.PRINCIPAL, Icon.TH, "/grupoproduto", Perfil.CLIENTE),
	SETOR("menu.principal.setor", Menu.PRINCIPAL, Icon.TH_LIST, "/setor", Perfil.CLIENTE),
	PRODUTO("menu.principal.produto", Menu.PRINCIPAL, Icon.BARCODE, "/produto", Perfil.CLIENTE),
	CONFIGURACAO_CADERNETA("menu.principal.configuracaocaderneta", Menu.PRINCIPAL, Icon.SETTINGS_APPLICATIONS, "/configuracaocaderneta", Perfil.CLIENTE),
	CADERNETA("menu.principal.caderneta", Menu.PRINCIPAL, Icon.BOOK, "/caderneta", Perfil.CLIENTE),
	//FOLHA("Folha", "folha", Menu.PRINCIPAL, Perfil.CONSUMIDOR, Icon.GLYPHICON_SEARCH),
	VENDA("menu.principal.venda", Menu.PRINCIPAL, Icon.PENCIL, "/venda", Perfil.CLIENTE),
	//Avaliar fase 2 / adequar o modelo
	//PEDIDO("Pedido", "app.pedido", Menu.PRINCIPAL, Icon.GLYPHICON_FILE, Perfil.CONSUMIDOR, Icon.FILE, "/pedido"),
	ANOTA_AI_HOME("menu.principal.home", Menu.SUPERIOR, null, "/home", Perfil.CLIENTE),
	PERFIL("menu.principal.perfil", Menu.SUPERIOR, null, "/perfil", Perfil.CLIENTE),
	ENTRADA_MERCADORIA("menu.principal.entradamercadoria", Menu.PRINCIPAL, Icon.ARROW_LEFT, "/entradamercadoria", Perfil.CLIENTE);
	
	private ItemMenu(String key, Menu menu, Icon icon, String url, Perfil... perfis) {
		this.key = key;
		this.menu = menu;
		this.icon = icon;
		this.perfis = perfis;
		this.url = url;
	}

	private String key;
	private Menu menu;
	@JsonIgnore
	private Perfil[] perfis;
	private String action;
	private Icon icon;
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public Menu getMenu() {
		return menu;
	}
	
	public Perfil[] getPerfis() {
		return perfis;
	}

	public String getAction() {
		return action;
	}
	
	public Icon getIcone() {
		return icon;
	}

	// TODO - Adicionar metodos dinamicamente
	public String getType() {
		return this.toString();
	}

	public String getPropertieKey() {
		StringBuilder sb = new StringBuilder("enum.");
		sb.append(this.getClass().getName()).append(".");
		sb.append(toString());
		return sb.toString().toLowerCase();
	}

	@JsonCreator
	public static ItemMenu fromObject(JsonNode node) {
		String type = null;
		if (node.getNodeType().equals(JsonNodeType.STRING)) {
			type = node.asText();
		} else {
			if (!node.has("type")) {
				throw new IllegalArgumentException();
			}
			type = node.get("type").asText();
		}
		return valueOf(type);
	}
}
