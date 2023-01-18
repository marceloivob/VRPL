package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeFornecedor {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeFornecedor(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<FornecedorBD>> clone(List<Clone<LicitacaoBD>> cloneDeLicitacao) {
		List<Long> idsLicitacoesOriginais = cloneDeLicitacao.stream().map(Clone::getObjetoOriginal)
				.map(LicitacaoBD::getId).collect(Collectors.toList());

		List<Clone<FornecedorBD>> clonesDeFornecedor = new ArrayList<>();

		List<FornecedorBD> fornecedoresOriginais = dao.selectFornecedorParaClonar(idsLicitacoesOriginais);

		for (FornecedorBD fornecedorOriginal : fornecedoresOriginais) {
			Optional<Clone<LicitacaoBD>> licitacoesClonadas = cloneDeLicitacao.stream()
					.filter(licitacaoClonada -> licitacaoClonada.getObjetoOriginal().getId()
							.equals(fornecedorOriginal.getLicitacaoFk()))
					.findFirst();

			if (licitacoesClonadas.isPresent()) {
				LicitacaoBD licitacaoClonada = licitacoesClonadas.get().getObjetoClonado();

				FornecedorBD clone = dao.cloneFornecedor(fornecedorOriginal, licitacaoClonada, siglaEvento);

				clonesDeFornecedor.add(new Clone<FornecedorBD>(fornecedorOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma LicitacaoBD clonada para o FornecedorBD " + fornecedorOriginal);
			}
		}

		return clonesDeFornecedor;
	}

}
