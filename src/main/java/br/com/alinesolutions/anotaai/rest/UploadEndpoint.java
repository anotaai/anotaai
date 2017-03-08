package br.com.alinesolutions.anotaai.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.com.alinesolutions.anotaai.metadata.model.AppException;
import br.com.alinesolutions.anotaai.metadata.model.ResponseEntity;
import br.com.alinesolutions.anotaai.model.util.Arquivo;
import br.com.alinesolutions.anotaai.service.app.UploadService;


@Path("fileUpload")
public class UploadEndpoint {

	@EJB
	private UploadService upload;

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@MultipartForm Arquivo arquivo) {
		ResponseBuilder builder = null;
		ResponseEntity responseEntity = null;
		try {
			responseEntity = upload.saveProfileFile(arquivo);
			builder = Response.ok(responseEntity);
		} catch (AppException e) {
			responseEntity = new ResponseEntity();
			responseEntity.setIsValid(Boolean.FALSE);
			responseEntity.setException(e.getViewException());
			builder = Response.ok(responseEntity);
		} catch (Exception e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}
	
}
