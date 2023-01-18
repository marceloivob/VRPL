package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeProposta {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeProposta(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public Clone<PropostaBD> clone(PropostaBD propostaOriginal) {
		PropostaBD propostaAtualizada = dao.updateProposta(propostaOriginal);

		PropostaBD propostaClonada = dao.cloneProposta(propostaAtualizada, siglaEvento);

		return new Clone<>(propostaAtualizada, propostaClonada);
	}
}
