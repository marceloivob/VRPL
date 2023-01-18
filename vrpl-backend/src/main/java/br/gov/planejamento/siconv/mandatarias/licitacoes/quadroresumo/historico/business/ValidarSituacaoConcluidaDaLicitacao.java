package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.LicitacaoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.LicitacaoNaoConcluidaException;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;

public class ValidarSituacaoConcluidaDaLicitacao {

	private ClientLicitacoesInterface clientLicitacoes;

	public static final String SITUACAO_PROCESSO_LICITACAO_CONCLUIDO = "Concluído";

	@Inject
	private BusinessExceptionContext businessExceptionContext;  
	
	
	@Inject
	public ValidarSituacaoConcluidaDaLicitacao(ClientLicitacoesInterface clientLicitacoes) {
		this.clientLicitacoes = clientLicitacoes;
	}

	/**
	 * A licitação tem que estar Concluída
	 */
	public void validarSeProcessoLicitatorioEstaConcluido(LicitacaoBD licitacao) {
		ProcessoExecucaoResponse processoDeExecucao = clientLicitacoes
				.detalharProcessosExecucao(licitacao.getIdLicitacaoFk());

		if (processoDeExecucaoNaoFoiEncontrado(processoDeExecucao)) {
			businessExceptionContext.add(new LicitacaoNaoEncontradaException(licitacao));
			businessExceptionContext.throwException();
		}

		//Validação 01
		if (!SITUACAO_PROCESSO_LICITACAO_CONCLUIDO.equalsIgnoreCase(processoDeExecucao.getStatus())) {
			businessExceptionContext.add(new LicitacaoNaoConcluidaException(licitacao.getNumeroAno()));
		}
	}

	protected boolean processoDeExecucaoNaoFoiEncontrado(ProcessoExecucaoResponse processoDeExecucao) {
		return processoDeExecucao == null || processoDeExecucao.getNumeroAno() == null
				|| processoDeExecucao.getNumeroAno().equals("");
	}

}
