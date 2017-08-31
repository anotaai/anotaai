package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ItemMenu {

	CADASTRO_CONSUMIDOR("Consumidor", "app.cliente-consumidor", Menu.PRINCIPAL, Icon.GLYPHICON_USER, Perfil.CLIENTE, Icon.USER, "/consumidor"),
	GRUPO_PRODUTO("Grupo Produto", "app.grupo-produto", Menu.PRINCIPAL, Icon.GLYPHICON_TH, Perfil.CLIENTE, Icon.TH, "/grupoproduto"),
	SETOR("Setor", "app.setor", Menu.PRINCIPAL, Icon.GLYPHICON_LIST, Perfil.CLIENTE, Icon.TH_LIST, "/setor"),
	PRODUTO("Produto", "app.produto", Menu.PRINCIPAL, Icon.GLYPHICON_BARCODE, Perfil.CLIENTE, Icon.BARCODE, "/produto"),
	CADERNETA("Caderneta", "app.caderneta", Menu.PRINCIPAL, Icon.GLYPHICON_BOOK, Perfil.CLIENTE, Icon.BOOK, "/caderneta"),
	//FOLHA("Folha", "folha", Menu.PRINCIPAL, Perfil.CONSUMIDOR, Icon.GLYPHICON_SEARCH),
	VENDA("Venda", "app.venda", Menu.PRINCIPAL, Icon.GLYPHICON_PENCIL, Perfil.CLIENTE, Icon.PENCIL, "/venda"),
	//Avaliar fase 2 / adequar o modelo
	//PEDIDO("Pedido", "app.pedido", Menu.PRINCIPAL, Icon.GLYPHICON_FILE, Perfil.CONSUMIDOR, Icon.FILE, "/pedido"),
	ANOTA_AI_HOME("Anota ai", "home", Menu.SUPERIOR, Icon.GLYPHICON_FIRE, Perfil.CLIENTE, null, null),
	PERFIL("Perfil", "app.cliente", Menu.SUPERIOR, Icon.GLYPHICON_USER, Perfil.CLIENTE, null, null),
	ENTRADA_MERCADORIA("Entrada de Mercadoria", "app.entrada-mercadoria", Menu.PRINCIPAL, Icon.GLYPHICON_ARROW_LEFT, Perfil.CLIENTE, Icon.ARROW_LEFT, "/entradamercadoria");
	
	private ItemMenu(String descricao, String action, Menu menu, Icon icon, Perfil perfil, Icon iconeMaterial, String url) {
		this.descricao = descricao;
		this.action = action;
		this.menu = menu;
		this.icon = icon;
		this.perfil = perfil;
		this.iconeMaterial = iconeMaterial;
		this.url = url;
		 
	}

	private String descricao;
	private Menu menu;
	private Perfil perfil;
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
	
	public Perfil getPerfil() {
		return perfil;
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
