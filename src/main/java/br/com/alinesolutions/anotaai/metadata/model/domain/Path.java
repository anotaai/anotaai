package br.com.alinesolutions.anotaai.metadata.model.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alinesolutions.anotaai.model.usuario.Usuario;

public enum Path {
	
	ANOTAAI("anotaai", null),
	FILES("files", ANOTAAI),
	USUARIO("usuario", FILES),
	CLIENTE("cliente", FILES),
	PRODUTO("produto", CLIENTE),
	THUMBNAIL_PRODUTO("thumbnail", PRODUTO),
	TEMP("temp", FILES),
	PROFILE("profile", USUARIO),
	THUMBNAIL_PRFILE("thumbnail", PROFILE);
	
	private Path root;
	private String path;
	
	private Path(String path, Path root) {
		this.path = path;
		this.root = root;
	}
	
	public String getPath() {
		StringBuilder caminho = new StringBuilder();
		List<String> paths = new ArrayList<>();
		Path current = this;
		paths.add(current.path);
		while (current.root != null) {
			current = current.root;
			if (current != null) {
				paths.add(current.path);
			}
		}
		Collections.reverse(paths);
		paths.stream().forEach(p -> caminho.append(p).append(File.separator));
		
		return caminho.toString();
	}
	
	public String getPath(Usuario usuario) {
		StringBuilder path = new StringBuilder(getPath());
		return path.append(usuario.getId()).append(File.separator).toString();
	}
	
}
