package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.LicitacaoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class RegimeDeContratacaoDaLicitacaoEhRDCOuLei13303 {

	@Inject
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	private ClientLicitacoesInterface clientLicitacoes;

	@Inject
	private DAOFactory dao;

	public boolean regimeDeContratacaoEhRDCOuLei13303(LicitacaoBD licitacao) {
		ProcessoExecucaoResponse processoDeExecucao = clientLicitacoes
				.detalharProcessosExecucao(licitacao.getIdLicitacaoFk());

		if (processoDeExecucaoNaoFoiEncontrado(processoDeExecucao)) {
			throw new LicitacaoNaoEncontradaException(licitacao);
		}

		List<String> regimeDeContratacaoEhRDCOuLei13303 = Arrays.asList("Lei 12.462/2011 - RDC", "Lei 13.303/2016");

		return regimeDeContratacaoEhRDCOuLei13303.contains(processoDeExecucao.getRegimeContratacao());
	}

	private boolean processoDeExecucaoNaoFoiEncontrado(ProcessoExecucaoResponse processoDeExecucao) {
		return processoDeExecucao == null || processoDeExecucao.getNumeroAno() == null
				|| processoDeExecucao.getNumeroAno().equals("");
	}

}
