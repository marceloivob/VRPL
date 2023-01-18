package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.business.TemplateLaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.dto.TemplateLaudoDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/templatelaudo")
public class TemplateLaudoRest {

	@Inject
	private TemplateLaudoBC templateLaudoBC;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera um templateLaudo")
	public DefaultResponse<TemplateLaudoDTO> consultarTemplateLaudoPorId(@QueryParam("idTemplate") Long idTemplate) {

		TemplateLaudoDTO templateLaudo = templateLaudoBC.recuperarTemplateLaudoPorId(idTemplate);
		return success(templateLaudo);
	}

	@GET
	@Path("/{tipoTemplateLaudo}/licitacao/{idLicitacao}/versao/{idVersaoDaLicitacao}/perguntas")
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera um Template Laudo com os grupos e perguntas")
	public DefaultResponse<TemplateLaudoDTO> consultarTemplateLaudoPorTipo(
			@PathParam("tipoTemplateLaudo") String tipoTemplateLaudo, @PathParam("idLicitacao") Long idLicitacao,
			@PathParam("idVersaoDaLicitacao") Long idVersaoDaLicitacao) {
		TemplateLaudoDTO templateLaudo = templateLaudoBC.recuperarTemplateLaudoPorTipo(tipoTemplateLaudo, idLicitacao,
				idVersaoDaLicitacao);
		return success(templateLaudo);
	}

}
