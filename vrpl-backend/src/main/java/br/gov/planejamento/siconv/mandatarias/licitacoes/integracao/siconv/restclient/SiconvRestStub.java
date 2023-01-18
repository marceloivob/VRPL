package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.util.Date;

import javax.enterprise.inject.Alternative;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Alternative
public class SiconvRestStub implements SiconvRest {

	@Override
	public ContratoDocLiquidacaoResponse consultarContratoDocLiquidacaoVinculadoProcessoExecucao(Long licitacao,
			SiconvPrincipal usuarioLogado) {
		log.info(
				"Integração com o serviço consultarContratoDocLiquidacaoVinculadoProcessoExecucao, com os parâmetros: licitacao: {}, token: {}",
				licitacao, usuarioLogado.getToken());

		return new ContratoDocLiquidacaoResponse(null, null);
	}

	@Override
	public void enviarProcessoExecucaoParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		log.info(
				"Integração com o serviço enviarProcessoExecucaoParaAceite, com os parâmetros: licitacao: {},  justificativa: {}, token: {}",
				licitacao, justificativa, usuarioLogado.getToken());
	}

	@Override
	public void aceitarRejeitarProcessoExecucao(Long licitacao, EventoAceiteRejeicao evento,
			String atribuicaoResponsavel, Date dataAnalise, String justificativa, SiconvPrincipal usuarioLogado) {
		log.info(
				"Integração com o serviço aceitarRejeitarProcessoExecucao, com os parâmetros: licitacao: {}, evento: {}, atribuicaoResponsavel: {}, dataAnalise: {}, justificativa: {}, token: {}",
				licitacao, evento, atribuicaoResponsavel, dataAnalise, justificativa, usuarioLogado.getToken());
	}

	@Override
	public void estornarProcessoExecucao(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		log.info(
				"Integração com o serviço estornarProcessoExecucao, com os parâmetros: licitacao: {}, justificativa: {}, token: {}",
				licitacao, justificativa, usuarioLogado.getToken());
	}
	
	@Override
	public void estornarEnvioParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		log.info(
				"Integração com o serviço estornarEnvioParaAceite, com os parâmetros: licitacao: {}, justificativa: {}, token: {}",
				licitacao, justificativa, usuarioLogado.getToken());
	}

}
