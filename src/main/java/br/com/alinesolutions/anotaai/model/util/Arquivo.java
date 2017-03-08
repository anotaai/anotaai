package br.com.alinesolutions.anotaai.model.util;

import java.io.InputStream;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.ws.rs.FormParam;

import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoArquivo;
import br.com.alinesolutions.anotaai.model.util.Arquivo.ArquivoContant;

@NamedQueries(
	@NamedQuery(name=ArquivoContant.LOAD_FILE_KEY, query=ArquivoContant.LOAD_FILE_QUERY)
)
@Entity
public class Arquivo extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@FormParam("name")
	private String name;

	@FormParam("size")
	private Integer size;

	private String path;

	@FormParam("type")
	private TipoArquivo tipoArquivo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@Transient
	@FormParam("file")
	private transient InputStream fileInput;
	
	@Transient
	private byte[] file;

	public Arquivo() {
		super();
	}
	
	public Arquivo(String path, String name, TipoArquivo tipoArquivo) {
		this(path, name);
		this.tipoArquivo = tipoArquivo;
	}
	
	public Arquivo(String path, String name) {
		super();
		this.name = name;
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public TipoArquivo getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(TipoArquivo tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public InputStream getFileInput() {
		return fileInput;
	}

	public void setFileInput(InputStream fileInput) {
		this.fileInput = fileInput;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public interface ArquivoContant {
		String FIELD_PATH = "path";
		String LOAD_FILE_KEY= "Arquivo.loadFile";
		String LOAD_FILE_QUERY = "select new br.com.alinesolutions.anotaai.model.util.Arquivo(a.path, a.name, a.tipoArquivo) from Usuario u join u.fotoPerfil a where u.id = :id";
	}

}