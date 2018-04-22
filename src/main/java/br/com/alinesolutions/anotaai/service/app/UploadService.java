package br.com.alinesolutions.anotaai.service.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.common.io.ByteStreams;

import br.com.alinesolutions.anotaai.infra.Constant;
import br.com.alinesolutions.anotaai.infra.FileUtils;
import br.com.alinesolutions.anotaai.infra.ShardingResourceFactory;
import br.com.alinesolutions.anotaai.infra.UsuarioUtils;
import br.com.alinesolutions.anotaai.metadata.io.ResponseEntity;
import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.domain.Path;
import br.com.alinesolutions.anotaai.model.BaseEntity;
import br.com.alinesolutions.anotaai.model.domain.TipoArquivo;
import br.com.alinesolutions.anotaai.model.usuario.Usuario;
import br.com.alinesolutions.anotaai.model.util.Arquivo;
import br.com.alinesolutions.anotaai.model.util.Arquivo.ArquivoContant;

@Stateless
public class UploadService {
	
	@Inject
	private ShardingResourceFactory appManager;


	public ResponseEntity<Arquivo> saveProfileFile(Arquivo arquivo) throws AppException {
		
		ResponseEntity<Arquivo> entity = new ResponseEntity<>();
		try {
			File file = getProfileFile(arquivo);
			buildTypeFile(arquivo);
			String path = createFile(arquivo, file);
			arquivo.setPath(path);
			arquivo.setDataCriacao(new Date());
			Long idUsuario = appManager.getAppService().getUsuario().getId();
			Usuario usuario = appManager.getEntityManager().find(Usuario.class, idUsuario);
			createByteArray(arquivo);
			usuario.setFotoPerfil(arquivo);
			arquivo.setFileInput(null);
			entity.setEntity(arquivo);
			appManager.getEntityManager().merge(usuario);
		} catch (IllegalArgumentException e) {
			appManager.getResponseUtil().buildIllegalArgumentException(entity);
		} catch (NoResultException e) {
			appManager.getResponseUtil().buildIllegalArgumentException(entity);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}

	private void createByteArray(Arquivo arquivo) throws IOException, FileNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(arquivo.getPath());
		byte[] byteArray = ByteStreams.toByteArray(fileInputStream);
		arquivo.setFile(byteArray);
		fileInputStream.close();
	}

	private String createFile(Arquivo arquivo, File file) throws FileNotFoundException, IOException {
		int read = 0;
		byte[] bytes = new byte[1024];
		OutputStream out = new FileOutputStream(file);
		while ((read = arquivo.getFileInput().read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
		return file.getAbsolutePath();
	}

	private File getProfileFile(Arquivo arquivo) {
		StringBuilder fileName = new StringBuilder(FileUtils.getInstance().getRootDir());
		fileName.append(Path.PROFILE.getPath(UsuarioUtils.getUsuario()));
		File pathUsuario = new File(fileName.toString());
		if (!pathUsuario.exists()) {
			pathUsuario.mkdirs();
		}
		fileName.append(Constant.App.NAME_FOTO_PERFIL).append(getExtension(arquivo));
		arquivo.setPath(fileName.toString());
		return new File(fileName.toString());
	}
	
	private String getExtension(Arquivo arquivo) {
		return arquivo.getName().substring(arquivo.getName().lastIndexOf("."));
	}
	
	
	private void buildTypeFile(Arquivo arquivo) {
		String extension = getExtension(arquivo);
		arquivo.setTipoArquivo(TipoArquivo.buildFromExtension(extension));
	}

	public Arquivo getProfilePhoto() {
		Arquivo arquivo = null;
		try {
			TypedQuery<Arquivo> tp = appManager.getEntityManager().createNamedQuery(ArquivoContant.LOAD_FILE_KEY, Arquivo.class);
			tp.setParameter(BaseEntity.BaseEntityConstant.FIELD_ID, appManager.getAppService().getUsuario().getId());
			arquivo = tp.getSingleResult();
			createByteArray(arquivo);
		} catch (IOException e) {
			//TODO erro ao ler o arquivo
		} catch (NoResultException e) {
			//TODO nao possui foto de perfil
		}
		return arquivo;
	}
	
}
