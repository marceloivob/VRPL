package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.business.PendenciaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.dto.PendenciaDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/pendencia")
public class PendenciaRest {

    @Inject
    private PendenciaBC pendenciaBC;

    @PUT
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Inclusão/alteração de Pendencia")
    public DefaultResponse<String> salvarPendencia(PendenciaDTO pendencia) {

    	if (pendencia.getId() == null) {
    		pendenciaBC.inserir(pendencia);
        } else {
        	pendenciaBC.alterar(pendencia);
        }

        return success("OK");
    }

    /**
     * remove um pendencia
     *
     */
    @DELETE
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<String> deletePendencia(@QueryParam ("id") Long id) {
    	pendenciaBC.excluir(id);
        return success("OK");
    }

    @GET
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera um pendencia")
    public DefaultResponse<PendenciaDTO> consultarPendenciaPorId(@QueryParam ("id") Long id) {

    	PendenciaDTO pendencia = pendenciaBC.recuperarPendenciaPorId(id);
        return success(pendencia);
    }

    @GET
    @Path("/{idLaudo}")
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera as pendencias de um Laudo")
    public DefaultResponse<List<PendenciaDTO>> consultarPendenciaPorLaudo(@NotNull @PathParam("idLaudo") Long idLaudo) {

    	List<PendenciaDTO> pendencias = pendenciaBC.recuperarPendenciaPorLaudo(idLaudo);
        return success(pendencias);
    }

}
