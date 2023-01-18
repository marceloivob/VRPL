package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.TipoDeParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/parecer")
public class ParecerRest {

	@Inject
	private LaudoBC laudoBC;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera um laudos")
	public DefaultResponse<LaudoDTO> consultarLaudoPorId(@QueryParam("id") Long id) {

		LaudoDTO laudo = laudoBC.recuperarLaudoPorId(id);
		return success(laudo);
	}

	@GET
	@Path("/{idLicitacao}")
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera o Laudo por Licitacao")
	public DefaultResponse<LaudoDTO> consultarLaudoPorLicitacao(
			@PathParam("idLicitacao") Long idLicitacao,
			@QueryParam("tipoLaudo") String tipoLaudoAsString) {

		TipoDeParecerEnum tipoDeLaudo = TipoDeParecerEnum.valueOf(tipoLaudoAsString);

		LaudoDTO laudo = laudoBC.recuperarLaudoPorLicitacao(idLicitacao, tipoDeLaudo);

		return success(laudo);
	}

	@POST
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera o Laudo por Licitacao")
	public DefaultResponse<String> salvarLaudo(LaudoDTO laudo) {
		laudoBC.salvarLaudo(laudo);
		return success("OK");
	}

	@GET
	@Path("/quememitiu")
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera quem emitiu o Laudo por Licitacao")
	public DefaultResponse<String> quemEmitiuLaudo(
			@QueryParam("idLicitacao") Long idLicitacao,
			@QueryParam("tipoLaudo") String tipoLaudoAsString) {

		String quemEmitiu = laudoBC.quemEmitiuLaudo(idLicitacao, tipoLaudoAsString);

		return success(quemEmitiu);
	}

}
