package br.com.alinesolutions.anotaai.rest.util;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alinesolutions.anotaai.metadata.model.domain.Perfil;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoAcesso;
import br.com.alinesolutions.anotaai.metadata.model.domain.TipoVenda;
import br.com.alinesolutions.anotaai.model.domain.DiaSemana;
import br.com.alinesolutions.anotaai.model.domain.Estado;
import br.com.alinesolutions.anotaai.model.domain.Operadora;
import br.com.alinesolutions.anotaai.model.domain.SituacaoCliente;
import br.com.alinesolutions.anotaai.model.domain.TipoArmazenamento;
import br.com.alinesolutions.anotaai.model.domain.UnidadeMedida;

@Singleton
@Path("/enums")
public class EnumEndpoint {

	@GET
	@Path("/perfis")
	@Produces(MediaType.APPLICATION_JSON)
	public Response perfil() throws Exception {
		return Response.ok(Perfil.values()).build();
	}

	@GET
	@Path("/estados")
	@Produces(MediaType.APPLICATION_JSON)
	public Response estado() throws Exception {
		return Response.ok(Estado.values()).build();
	}

	@GET
	@Path("/operadoras")
	@Produces(MediaType.APPLICATION_JSON)
	public Response operadora() throws Exception {
		return Response.ok(Operadora.values()).build();
	}

	@GET
	@Path("/unidadesmedida")
	@Produces(MediaType.APPLICATION_JSON)
	public Response unidadeMedida() throws Exception {
		return Response.ok(UnidadeMedida.values()).build();
	}

	@GET
	@Path("/diasemana")
	@Produces(MediaType.APPLICATION_JSON)
	public Response diaSemana() throws Exception {
		return Response.ok(DiaSemana.values()).build();
	}

	@GET
	@Path("/situacoescliente")
	@Produces(MediaType.APPLICATION_JSON)
	public Response situacoesCliente() throws Exception {
		return Response.ok(SituacaoCliente.values()).build();
	}

	@GET
	@Path("/tiposAcesso")
	@Produces(MediaType.APPLICATION_JSON)
	public Response tiposAcesso() throws Exception {
		return Response.ok(TipoAcesso.values()).build();
	}
	
	@GET
	@Path("/tiposArmazenamento")
	@Produces(MediaType.APPLICATION_JSON)
	public Response tiposArmazenamento() throws Exception {
		return Response.ok(TipoArmazenamento.values()).build();
	}
	
	@GET
	@Path("/tipovenda")
	@Produces(MediaType.APPLICATION_JSON)
	public Response tipoVenda() throws Exception {
		return Response.ok(TipoVenda.values()).build();
	}
}
