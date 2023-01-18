package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeSubItemDeInvestimento {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeSubItemDeInvestimento(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<SubitemInvestimentoBD>> clone(PropostaBD propostaOriginal) {

		List<SubitemInvestimentoBD> subitemsDeInvestimento = dao
				.selectSubItemDeInvestimentoParaClonar(propostaOriginal);

		List<Clone<SubitemInvestimentoBD>> clonesDeSubItemDeInvestimento = new ArrayList<>();

		for (SubitemInvestimentoBD original : subitemsDeInvestimento) {

			SubitemInvestimentoBD clone = dao.cloneSubItemDeInvestimento(original, siglaEvento);

			clonesDeSubItemDeInvestimento.add(new Clone<SubitemInvestimentoBD>(original, clone));
		}

		return clonesDeSubItemDeInvestimento;
	}
}
