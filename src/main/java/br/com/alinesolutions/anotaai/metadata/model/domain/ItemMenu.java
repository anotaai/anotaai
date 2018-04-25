package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ItemMenu {

	CADASTRO_CONSUMIDOR("Consumidor", Menu.PRINCIPAL, Icon.GLYPHICON_USER, Icon.USER, "/consumidor", Perfil.CLIENTE),
	GRUPO_PRODUTO("Grupo Produto", Menu.PRINCIPAL, Icon.GLYPHICON_TH, Icon.TH, "/grupoproduto", Perfil.CLIENTE),
	SETOR("Setor", Menu.PRINCIPAL, Icon.GLYPHICON_LIST, Icon.TH_LIST, "/setor", Perfil.CLIENTE),
	PRODUTO("Produto", Menu.PRINCIPAL, Icon.GLYPHICON_BARCODE, Icon.BARCODE, "/produto", Perfil.CLIENTE),
	CONFIGURACAO_CADERNETA("Configuracao Caderneta", Menu.PRINCIPAL, Icon.GLYPHICON_EDIT, Icon.SETTINGS_APPLICATIONS, "/configuracaocaderneta", Perfil.CLIENTE),
	CADERNETA("Caderneta", Menu.PRINCIPAL, Icon.GLYPHICON_BOOK, Icon.BOOK, "/caderneta", Perfil.CLIENTE),
	//FOLHA("Folha", "folha", Menu.PRINCIPAL, Perfil.CONSUMIDOR, Icon.GLYPHICON_SEARCH),
	VENDA("Venda", Menu.PRINCIPAL, Icon.GLYPHICON_PENCIL, Icon.PENCIL, "/venda", Perfil.CLIENTE),
	//Avaliar fase 2 / adequar o modelo
	//PEDIDO("Pedido", "app.pedido", Menu.PRINCIPAL, Icon.GLYPHICON_FILE, Perfil.CONSUMIDOR, Icon.FILE, "/pedido"),
	ANOTA_AI_HOME("Anota ai", Menu.SUPERIOR, Icon.GLYPHICON_FIRE, null, null, Perfil.CLIENTE),
	PERFIL("Perfil", Menu.SUPERIOR, Icon.GLYPHICON_USER, null, null, Perfil.CLIENTE),
	ENTRADA_MERCADORIA("Entrada de Mercadoria", Menu.PRINCIPAL, Icon.GLYPHICON_ARROW_LEFT, Icon.ARROW_LEFT, "/entradamercadoria", Perfil.CLIENTE);
	
	private ItemMenu(String descricao, Menu menu, Icon icon, Icon iconeMaterial, String url, Perfil... perfis) {
		this.descricao = descricao;
		this.menu = menu;
		this.icon = icon;
		this.perfis = perfis;
		this.url = url;
		this.iconeMaterial = iconeMaterial;
	}

	private String descricao;
	private Menu menu;
	@JsonIgnore
	private Perfil[] perfis;
	private String action;
	private Icon icon;
	private Icon iconeMaterial;
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Icon getIconeMaterial() {
		return iconeMaterial;
	}
	
	public void setIconeMaterial(Icon iconeMaterial) {
		this.iconeMaterial = iconeMaterial;
	}

	public String getDescricao() {
		return descricao;
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
