package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.business.GrupoPerguntaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/grupopergunta")
public class GrupoPerguntaRest {

	@Inject
	private GrupoPerguntaBC grupoPerguntaBC;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera um grupoPergunta")
	public DefaultResponse<GrupoPerguntaDTO> consultarGrupoPerguntaPorId(@QueryParam("id") Long id) {

		GrupoPerguntaDTO grupoPergunta = grupoPerguntaBC.recuperarGrupoPerguntaPorId(id);
		return success(grupoPergunta);
	}

}
