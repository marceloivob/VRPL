package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeMeta {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeMeta(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<MetaBD>> clone(PropostaBD propostaOriginal,
			List<Clone<SubitemInvestimentoBD>> cloneDeSubItemDeInvestimento) {

		List<MetaBD> metas = dao.selectMetaParaClonar(propostaOriginal);

		List<Clone<MetaBD>> clonesDeMeta = new ArrayList<>();

		for (MetaBD metaOriginal : metas) {
			Optional<Clone<SubitemInvestimentoBD>> subItemDeInvestimentoClonado = cloneDeSubItemDeInvestimento.stream()
					.filter(subitem -> subitem.getObjetoOriginal().getId().equals(metaOriginal.getSubItemFk()))
					.findFirst();

			if (subItemDeInvestimentoClonado.isPresent()) {
				SubitemInvestimentoBD subitemInvestimentoClonado = subItemDeInvestimentoClonado.get()
						.getObjetoClonado();

				MetaBD clone = dao.cloneMeta(metaOriginal, subitemInvestimentoClonado, siglaEvento);

				clonesDeMeta.add(new Clone<MetaBD>(metaOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista um SubitemInvestimentoBD clonado para a MetaBD " + metaOriginal);
			}
		}

		return clonesDeMeta;
	}

}
