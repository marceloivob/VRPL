package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import java.util.Date;

import javax.enterprise.inject.Any;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.ContratoDocLiquidacaoResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;

@Any
@SiconvRestQualifier
public class SiconvRestMock implements SiconvRest {

	@Override
	public ContratoDocLiquidacaoResponse consultarContratoDocLiquidacaoVinculadoProcessoExecucao(Long licitacao,
			SiconvPrincipal usuarioLogado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enviarProcessoExecucaoParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aceitarRejeitarProcessoExecucao(Long licitacao, EventoAceiteRejeicao evento,
			String atribuicaoResponsavel, Date dataAnalise, String justificativa, SiconvPrincipal usuarioLogado) {
		// TODO Auto-generated method stub

	}

	@Override
	public void estornarProcessoExecucao(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		// TODO Auto-generated method stub

	}

	@Override
	public void estornarEnvioParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		// TODO Auto-generated method stub

	}

}
