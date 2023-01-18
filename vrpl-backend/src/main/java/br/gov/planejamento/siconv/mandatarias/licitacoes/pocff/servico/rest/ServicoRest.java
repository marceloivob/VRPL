package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business.ServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/servico")
public class ServicoRest {

    @Inject
    private ServicoBC servicoBC;

    @POST
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Inclusão/alteração de Servico")
    @AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
    public DefaultResponse<String> salvarServico(ServicoDTO servico) {
        servicoBC.alterar(servico);
        return success("OK");
    }

    /**
     * remove um servico
     *
     */
    @DELETE
    @Produces(APPLICATION_JSON_UTF8)
    @AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> deleteServico(@QueryParam("id") Long id, @QueryParam("versao") Long versao) {

		ServicoDTO servico = new ServicoDTO();
		servico.setId(id);
		servico.setVersao(versao);

		servicoBC.excluir(servico);

        return success("OK");
    }

    @GET
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera um servico")
	public DefaultResponse<ServicoDTO> consultarServicoPorId(@QueryParam("id") Long id) {

		ServicoBD servico = new ServicoBD();
		servico.setId(id);

		ServicoDTO servicoDTO = servicoBC.recuperarServicoPorId(servico);

		return success(servicoDTO);
    }

    @GET
    @Path("/{idServico}/frente-obra")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<List<ServicoFrenteObraDTO>> recuperarListaFrentesDeObra(@PathParam("idServico") Long idServico) {
		List<ServicoFrenteObraDTO> dto = this.servicoBC.recuperarListaFrentes(idServico);

		return success(dto);
    }

    @GET
    @Path("/{idServico}/frente-obra-analise")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<List<ServicoFrenteObraDTO>> recuperarListaFrentesDeObraAnalise(@PathParam("idServico") Long idServico) {
		List<ServicoFrenteObraDTO> dto = this.servicoBC.recuperarListaFrentesDeObraAnalise(idServico);

		return success(dto);
    }

}
