package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.util.Date;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;

public interface SiconvRest {

	ContratoDocLiquidacaoResponse consultarContratoDocLiquidacaoVinculadoProcessoExecucao(Long licitacao,
			SiconvPrincipal usuarioLogado);

	void enviarProcessoExecucaoParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado);

	void aceitarRejeitarProcessoExecucao(Long licitacao, EventoAceiteRejeicao evento, String atribuicaoResponsavel,
			Date dataAnalise, String justificativa, SiconvPrincipal usuarioLogado);

	void estornarProcessoExecucao(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado);
	
	void estornarEnvioParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado);

}