package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeAnexo {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeAnexo(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<AnexoBD>> clone(List<Clone<LicitacaoBD>> cloneDeLicitacao) {
		List<Long> idsLicitacoesOriginais = cloneDeLicitacao.stream().map(Clone::getObjetoOriginal)
				.map(LicitacaoBD::getId).collect(Collectors.toList());

		List<Clone<AnexoBD>> clonesDeAnexos = new ArrayList<>();

		List<AnexoBD> anexosOriginais = dao.selectAnexoParaClonar(idsLicitacoesOriginais);

		for (AnexoBD anexoOriginal : anexosOriginais) {
			Optional<Clone<LicitacaoBD>> licitacoesClonadas = cloneDeLicitacao.stream()
					.filter(licitacaoClonada -> licitacaoClonada.getObjetoOriginal().getId()
							.equals(anexoOriginal.getIdentificadorDaLicitacao()))
					.findFirst();

			if (licitacoesClonadas.isPresent()) {
				LicitacaoBD licitacaoClonada = licitacoesClonadas.get().getObjetoClonado();

				AnexoBD clone = dao.cloneAnexo(anexoOriginal, licitacaoClonada, siglaEvento);

				clonesDeAnexos.add(new Clone<AnexoBD>(anexoOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma LicitacaoBD clonada para o AnexoBD " + anexoOriginal);
			}
		}

		return clonesDeAnexos;
	}

}
