package br.com.alinesolutions.anotaai.model.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoArquivo {

	PNG("PNG", "image/png", ".png"),
	JPG("JPG", "image/jpeg", ".jpg"),
	JPEG("JPEG", "image/jpeg", ".jpeg"),
	DOC("DOC", "application/msword", ".doc"),
	PDF("PDF", "application/pdf", ".pdf");
	
	private TipoArquivo(String descricao, String mediaType, String suffixe) {
		this.descricao = descricao;
		this.mediaType = mediaType;
		this.suffixe = suffixe;
	}
	
	private String descricao;
	private String mediaType;
	private String suffixe	;
	private static Map<String, TipoArquivo> tiposArquivo;
	
	static {
		tiposArquivo = new HashMap<>();
		tiposArquivo.put(TipoArquivo.PNG.suffixe, TipoArquivo.PNG);
		tiposArquivo.put(TipoArquivo.JPG.suffixe, TipoArquivo.JPG);
		tiposArquivo.put(TipoArquivo.JPEG.suffixe, TipoArquivo.JPEG);
		tiposArquivo.put(TipoArquivo.DOC.suffixe, TipoArquivo.DOC);
		tiposArquivo.put(TipoArquivo.PDF.suffixe, TipoArquivo.PDF);
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getMediaType() {
		return mediaType;
	}
	
	public String getSuffixe() {
		return suffixe;
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
	public static TipoArquivo fromObject(JsonNode node) {
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

	public static TipoArquivo buildFromExtension(String extension) {
		if (tiposArquivo.containsKey(extension)) {
			return tiposArquivo.get(extension);
		}
		throw new IllegalArgumentException();
	} 
	
}
