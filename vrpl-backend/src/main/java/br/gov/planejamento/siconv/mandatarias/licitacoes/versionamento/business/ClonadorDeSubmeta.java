package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeSubmeta {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeSubmeta(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<SubmetaBD>> clone(Clone<PropostaBD> cloneDeProposta, List<Clone<MetaBD>> cloneDeMeta,
			List<Clone<LoteBD>> cloneDeLote) {
		List<Clone<SubmetaBD>> clonesDeSubmeta = new ArrayList<>();

		List<SubmetaBD> submetasOriginais = dao.selectSubmetaParaClonar(cloneDeProposta.getObjetoOriginal());

		for (SubmetaBD submetaOriginal : submetasOriginais) {
			Optional<Clone<MetaBD>> metasClonadas = cloneDeMeta.stream()
					.filter(metaClonada -> metaClonada.getObjetoOriginal().getId().equals(submetaOriginal.getIdMeta()))
					.findFirst();

			Optional<Clone<LoteBD>> lotesClonados = cloneDeLote.stream().filter(loteClonado -> loteClonado
					.getObjetoOriginal().getId().equals(submetaOriginal.getVrplLoteLicitacaoFk())).findFirst();

			if (metasClonadas.isPresent() && lotesClonados.isPresent()) {
				MetaBD metaClonada = metasClonadas.get().getObjetoClonado();
				LoteBD loteClonado = lotesClonados.get().getObjetoClonado();

				SubmetaBD clone = dao.cloneSubmeta(submetaOriginal, metaClonada, cloneDeProposta.getObjetoClonado(),
						loteClonado, siglaEvento);

				clonesDeSubmeta.add(new Clone<SubmetaBD>(submetaOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma MetaBD clonada ou um LoteBD clonado para o SubmetaBD "
								+ submetaOriginal);
			}
		}

		return clonesDeSubmeta;
	}
}
