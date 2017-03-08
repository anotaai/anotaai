package br.com.alinesolutions.anotaai.metadata.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ItemMenu {

	CADASTRO_CONSUMIDOR("Consumidor", "app.cliente-consumidor", Menu.PRINCIPAL, Icone.GLYPHICON_USER, Perfil.CLIENTE),
	GRUPO_PRODUTO("Grupo Produto", "app.grupo-produto", Menu.PRINCIPAL, Icone.GLYPHICON_TH, Perfil.CLIENTE),
	SETOR("Setor", "app.setor", Menu.PRINCIPAL, Icone.GLYPHICON_TH_LIST, Perfil.CLIENTE),
	PRODUTO("Produto", "app.produto", Menu.PRINCIPAL, Icone.GLYPHICON_BARCODE, Perfil.CLIENTE),
	CADERNETA("Caderneta", "app.caderneta", Menu.PRINCIPAL, Icone.GLYPHICON_BOOK, Perfil.CLIENTE),
	//FOLHA("Folha", "folha", Menu.PRINCIPAL, Perfil.CONSUMIDOR, Icone.GLYPHICON_SEARCH),
	VENDA("Venda", "app.venda", Menu.PRINCIPAL, Icone.GLYPHICON_PENCIL, Perfil.CLIENTE),
	PEDIDO("Pedido", "app.pedido", Menu.PRINCIPAL, Icone.GLYPHICON_FILE, Perfil.CONSUMIDOR),
	ANOTA_AI_HOME("Anota ai", "home", Menu.SUPERIOR, Icone.GLYPHICON_FIRE, Perfil.CLIENTE),
	PERFIL("Perfil", "app.cliente", Menu.SUPERIOR, Icone.GLYPHICON_USER, Perfil.CLIENTE),
	ENTRADA_MERCADORIA("Entrada de Mercadoria", "app.entrada-mercadoria", Menu.PRINCIPAL, Icone.GLYPHICON_ARROW_LEFT, Perfil.CLIENTE);
	
	private ItemMenu(String descricao, String action, Menu menu, Icone icone, Perfil perfil) {
		this.descricao = descricao;
		this.action = action;
		this.menu = menu;
		this.icone = icone;
		this.perfil = perfil;
	}

	private String descricao;
	private Menu menu;
	private Perfil perfil;
	private String action;
	private Icone icone;

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
	
	public Icone getIcone() {
		return icone;
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
