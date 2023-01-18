package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.EmailProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.SenderEmail;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business.PropostaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.dto.PropostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business.QciBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.QciExternoDTO;

@SimplyTimed
@Path("/proposta")
public class PropostaRest {

	@Inject
	private QciBC qciBC;

	@Inject
	private PropostaBC propostaBC;

	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	@EmailProducer
	private SenderEmail senderEmail;

	@GET
	@Path("{versaoDaProposta}")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<PropostaDTO> dadosBasicos(@PathParam(value = "versaoDaProposta") Long versaoDaProposta,
			@Context Request request) {

		PropostaBD propostaBD = propostaBC.recuperarProposta(usuarioLogado, versaoDaProposta);

		List<Long> versoes = propostaBC.recuperaVersoesDaProposta(usuarioLogado);

		PropostaDTO propostaAsDTO = new PropostaDTO(propostaBD, versoes);

		return success(propostaAsDTO);
	}

	@GET
	@Path("/{versaoDaProposta}/qci")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<QciExternoDTO> qciDaProposta(@PathParam(value = "versaoDaProposta") Long versaoDaProposta,
			@Context Request request) {

		QciExternoDTO qci = qciBC.recuperarNovoQCIExternoByPropFk(usuarioLogado, versaoDaProposta);

		return success(qci);

	}

	@GET
	@Path("/verifica-inicio-vrpl/")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<Boolean> verificaInicioVrpl() {
		Boolean ehPossivelIniciarVRPL = propostaBC.verificaInicioVrpl();

		return success(ehPossivelIniciarVRPL);
	}


	@GET
	@Path("/{versaoDaProposta}/lotes-ativos")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<LoteDTO>> lotesAtivosDaProposta(
			@PathParam(value = "versaoDaProposta") Long versaoDaProposta, @Context Request request) {
		List<LoteDTO> lotesDaProposta = propostaBC.lotesAtivosDaProposta(usuarioLogado, versaoDaProposta);

		return success(lotesDaProposta);
	}

	@GET
	@Path("/{versaoDaProposta}/lotes-rejeitados")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<LoteDTO>> lotesRejeitadosDaProposta(
			@PathParam(value = "versaoDaProposta") Long versaoDaProposta, @Context Request request) {
		List<LoteDTO> lotesDaProposta = propostaBC.lotesRejeitadosDaProposta(usuarioLogado, versaoDaProposta);

		return success(lotesDaProposta);
	}

	@GET
	@Path("/verifica-fornecedor-obsoleto")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<String> verificaFornecedorObsoleto() {
		propostaBC.verificaFornecedorObsoleto();
		
		return success("OK");
	}

}
