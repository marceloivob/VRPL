package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business.MacroServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/macroservico")
public class MacroServicoRest {

	@Inject
	private MacroServicoBC macroServicoBC;

	@GET
	@Path("/po/{poId}")
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera macroServicos com seus respectivos servicos")
	public DefaultResponse<PoMacroServicoServicosDTO> consultarMacroServicosPorPo(@PathParam("poId") Long poId) {

		Map<PoBD, PoMacroServicoServicosDTO> macroServicos = macroServicoBC.recuperarMacroServicoServicosPorPo(Arrays.asList(poId));

		if (macroServicos.keySet().iterator().hasNext()) {
			PoBD key = macroServicos.keySet().iterator().next();

			return success(macroServicos.get(key));

		} else {
			PoMacroServicoServicosDTO macroServicoVazio = new PoMacroServicoServicosDTO();
			List<MacroServicoServicosDTO> listaVaziaMacroservicos = new ArrayList<>();
			macroServicoVazio.setPoId(poId);
			macroServicoVazio.setMacroservicos(listaVaziaMacroservicos);

			return success(macroServicoVazio);
		}
	}

}
