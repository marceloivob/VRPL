package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.rest;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubItemInvestimento;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.UF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import java.util.List;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

@SimplyTimed
@Path("/dominio-siconv")
public class DominioSiconvRest {

    @Inject
    private SiconvBC siconvBC;

    @GET
    @Path("/ufs/")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<List<UF>> findUfs(){
       List<UF> ufs = siconvBC.findUFS();
       return success(ufs);
    }

    @GET
    @Path("/iteminvestimento/")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<SubItemInvestimento> loadSubItemInvestimento(@QueryParam("idSubItemInvestimentoFK") Long idSubItemInvestimento) {
        SubItemInvestimento subItemInvestimento = siconvBC.loadSubItemInvestimento(idSubItemInvestimento);
        return success(subItemInvestimento);
    }

}
