package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeLaudo {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeLaudo(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<LaudoBD>> clone(List<Clone<LicitacaoBD>> cloneDeLicitacao) {
		List<Long> idsLicitacoesOriginais = cloneDeLicitacao.stream().map(Clone::getObjetoOriginal)
				.map(LicitacaoBD::getId).collect(Collectors.toList());

		List<Clone<LaudoBD>> clonesDeHistorico = new ArrayList<>();

		List<LaudoBD> laudosOriginais = dao.selectLaudoParaClonar(idsLicitacoesOriginais);

		for (LaudoBD laudoOriginal : laudosOriginais) {
			Optional<Clone<LicitacaoBD>> licitacoesClonadas = cloneDeLicitacao.stream()
					.filter(licitacaoClonada -> licitacaoClonada.getObjetoOriginal().getId()
							.equals(laudoOriginal.getLicitacaoFk()))
					.findFirst();

			if (licitacoesClonadas.isPresent()) {
				LicitacaoBD licitacaoClonada = licitacoesClonadas.get().getObjetoClonado();

				LaudoBD clone = dao.cloneLaudo(laudoOriginal, licitacaoClonada, siglaEvento);

				clonesDeHistorico.add(new Clone<LaudoBD>(laudoOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma LicitacaoBD clonada para o LaudoBD " + laudoOriginal);
			}
		}

		return clonesDeHistorico;
	}
}
