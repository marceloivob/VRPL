package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.business.RespostaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/resposta")
public class RespostaRest {

    @Inject
    private RespostaBC respostaBC;

    @POST
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Inclusão/alteração de Resposta")
    public DefaultResponse<String> salvarResposta(RespostaDTO resposta) {

		if (resposta.getRespostaId() == null) {
    		respostaBC.inserir(resposta);
        } else {
        	respostaBC.alterar(resposta);
        }

        return success("OK");
    }

    @POST
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Inclusão/Alteração de Respostas")
    public DefaultResponse<String> salvarRespostas(List<RespostaDTO> respostas) {
    	respostaBC.inserirRespostas(respostas);
        return success("OK");
    }

    /**
     * remove um resposta
     *
     */
    @DELETE
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<String> deleteResposta(@QueryParam ("id") Long id) {
    	respostaBC.excluir(id);
        return success("OK");
    }

    @GET
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera um resposta")
    public DefaultResponse<RespostaDTO> consultarRespostaPorId(@QueryParam ("id") Long id) {

    	RespostaDTO resposta = respostaBC.recuperarRespostaPorId(id);
        return success(resposta);
    }

}
