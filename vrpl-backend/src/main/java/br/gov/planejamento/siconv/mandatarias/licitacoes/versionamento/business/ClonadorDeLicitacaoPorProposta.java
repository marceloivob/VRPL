package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeLicitacaoPorProposta {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeLicitacaoPorProposta(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<LicitacaoBD>> clone(Clone<PropostaBD> cloneDeProposta) {
		List<LicitacaoBD> licitacoes = dao.selectLicitacaoParaClonarPorProposta(cloneDeProposta.getObjetoOriginal());

		List<Clone<LicitacaoBD>> clonesDeLicitacao = new ArrayList<>();

		for (LicitacaoBD licitacaoOriginal : licitacoes) {

			LicitacaoBD clone = dao.cloneLicitacaoPorProposta(licitacaoOriginal, cloneDeProposta.getObjetoClonado(),
					siglaEvento);

			clonesDeLicitacao.add(new Clone<LicitacaoBD>(licitacaoOriginal, clone));
		}

		return clonesDeLicitacao;
	}

}
