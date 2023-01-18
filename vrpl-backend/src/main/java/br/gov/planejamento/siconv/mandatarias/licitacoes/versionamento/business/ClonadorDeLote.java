package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeLote {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeLote(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<LoteBD>> clone(Clone<PropostaBD> cloneDeProposta, List<Clone<LicitacaoBD>> cloneDeLicitacao,
			List<Clone<FornecedorBD>> cloneDeFornecedor) {
		List<Clone<LoteBD>> clonesDeLote = new ArrayList<>();

		List<LoteBD> lotesOriginais = dao.selectLoteParaClonar(cloneDeProposta.getObjetoOriginal());

		for (LoteBD loteOriginal : lotesOriginais) {
			Optional<Clone<LicitacaoBD>> licitacoesClonadas = cloneDeLicitacao.stream()
					.filter(licitacaoClonada -> licitacaoClonada.getObjetoOriginal().getId()
							.equals(loteOriginal.getIdentificadorDaLicitacao()))
					.findFirst();

			Optional<Clone<FornecedorBD>> fornecedoresClonados = cloneDeFornecedor.stream()
					.filter(fornecedorClonado -> fornecedorClonado.getObjetoOriginal().getId()
							.equals(loteOriginal.getIdFornecedor()))
					.findFirst();

			// TODO Corrigir NPE quando não existir Fornecedor e Licitacao
			// Esse new em Fornecedor e Licitação é uma GAMBI! O correto é passar null, se
			// não existir Fornecedor ou Licitacao
			FornecedorBD fornecedorClonado = new FornecedorBD();
			LicitacaoBD licitacaoClonada = new LicitacaoBD();

			if (fornecedoresClonados.isPresent()) {
				fornecedorClonado = fornecedoresClonados.get().getObjetoClonado();
			}

			if (licitacoesClonadas.isPresent()) {
				licitacaoClonada = licitacoesClonadas.get().getObjetoClonado();
			}

			LoteBD clone = dao.cloneLicitacaoLote(loteOriginal, fornecedorClonado, licitacaoClonada, siglaEvento);

			clonesDeLote.add(new Clone<LoteBD>(loteOriginal, clone));
		}

		return clonesDeLote;
	}
}
