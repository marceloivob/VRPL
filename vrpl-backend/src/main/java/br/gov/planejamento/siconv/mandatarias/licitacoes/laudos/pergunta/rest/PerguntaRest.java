package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.rest;

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

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.business.PerguntaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/pergunta")
public class PerguntaRest {

    @Inject
    private PerguntaBC perguntaBC;

    @GET
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera um pergunta")
    public DefaultResponse<PerguntaDTO> consultarPerguntaPorId(@QueryParam ("id") Long id) {

    	PerguntaDTO pergunta = perguntaBC.recuperarPerguntaPorId(id);
        return success(pergunta);
    }

}
