package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.MANDATARIA;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.QuadroResumoDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/quadroresumo")
public class QuadroResumoRest {

	@Inject
	private QuadroResumoBC quadroResumoBC;

	/**
	 * Consulta o historico de uma licitacao
	 *
	 * @param identificadorDaLicitacao - identificador da Licitacao
	 * @param versaoDaProposta         Versão da Proposta do versionamento
	 *
	 * @return Lista de registros do historico
	 */
	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<QuadroResumoDTO> recuperarQuadroResumo(
			@QueryParam("idLicitacao") Long identificadorDaLicitacao,
			@QueryParam("versaoDaProposta") Long versaoDaProposta) {
		QuadroResumoDTO quadroResumo = quadroResumoBC.recuperarQuadroResumo(identificadorDaLicitacao, versaoDaProposta);

		return success(quadroResumo);
	}

	@POST
	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Mudança de estado da documentação")
	public DefaultResponse<String> alterarEstadoDaDocumentacao(HistoricoLicitacaoDTO novoHistorico) {
		quadroResumoBC.processarNovoHistorico(novoHistorico);
		return success("OK");
	}
}
