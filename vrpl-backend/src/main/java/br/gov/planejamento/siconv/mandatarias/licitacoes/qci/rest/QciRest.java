package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business.QciBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.QciDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaQciVRPLDTO;

@SimplyTimed
@Path("/qci")
public class QciRest {

    @Inject
    private QciBC qciBC;

    @GET
    @Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<QciDTO> find(@QueryParam("idLicitacao") Long idLicitacao) {
		QciDTO qci = qciBC.recuperarQCIInternoByIdLicitacao(idLicitacao);

		return success(qci);
    }

    @POST
    @Produces(APPLICATION_JSON_UTF8)
    @Consumes(MediaType.APPLICATION_JSON)
    @AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> alterar(SubmetaQciVRPLDTO submeta) {

		qciBC.alterar(submeta);

		return success("OK");
    }
}
