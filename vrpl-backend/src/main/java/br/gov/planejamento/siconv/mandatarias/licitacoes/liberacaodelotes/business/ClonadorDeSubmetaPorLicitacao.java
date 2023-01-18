package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public class ClonadorDeSubmetaPorLicitacao {

	private LiberarLoteDAO dao;
	private String siglaEvento;
	private String siglaSituacaoLicitacao;

	public ClonadorDeSubmetaPorLicitacao(LiberarLoteDAO dao, EventoQuadroResumoEnum evento, SituacaoLicitacaoEnum situacaoLicitacao ) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
		this.siglaSituacaoLicitacao = situacaoLicitacao.getSigla();
	}

	public ClonadorDeSubmetaPorLicitacao(LiberarLoteDAO dao) {
		this.dao = dao;
	}

	public List<Clone<SubmetaBD>> clone(LicitacaoBD licitacaoOriginal, Clone<PropostaBD> cloneDeProposta,
			List<Clone<MetaBD>> cloneDeMeta, List<Clone<LoteBD>> cloneDeLote) {
		List<Clone<SubmetaBD>> clonesDeSubmeta = new ArrayList<>();

		List<SubmetaBD> submetasOriginais = dao.selectSubmetaParaClonarPorLicitacao(licitacaoOriginal);

		for (SubmetaBD submetaOriginal : submetasOriginais) {
			Optional<Clone<MetaBD>> metasClonadas = cloneDeMeta.stream()
					.filter(metaClonada -> metaClonada.getObjetoOriginal().getId().equals(submetaOriginal.getIdMeta()))
					.findFirst();

			Optional<Clone<LoteBD>> lotesClonados = cloneDeLote.stream().filter(loteClonado -> loteClonado
					.getObjetoOriginal().getId().equals(submetaOriginal.getVrplLoteLicitacaoFk())).findFirst();

			if (metasClonadas.isPresent() && lotesClonados.isPresent()) {
				MetaBD metaClonada = metasClonadas.get().getObjetoClonado();
				LoteBD loteClonado = lotesClonados.get().getObjetoClonado();
				PropostaBD propostaClonada = cloneDeProposta.getObjetoClonado();

				SubmetaBD clone = dao.cloneSubmetaPorLicitacao(submetaOriginal, propostaClonada, metaClonada,
						loteClonado, siglaEvento, siglaSituacaoLicitacao);

				clonesDeSubmeta.add(new Clone<SubmetaBD>(submetaOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma MetaBD clonada ou um LoteBD clonado para o SubmetaBD "
								+ submetaOriginal);
			}
		}

		return clonesDeSubmeta;
	}
	
	public List<SubmetaBD> consultarClonesGerados(List<LoteBD> lotesClonadosGeradosPelaRejeicaoApagar) {

		// Submetas foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone 
		return dao.selectSubmetasApagarPorLicitacao(lotesClonadosGeradosPelaRejeicaoApagar.stream().
				map((LoteBD lote) -> lote.getId()).collect(Collectors.toList()));
	}	


	public void apagarClone(List<SubmetaBD> submetasClonadasGeradasPelaRejeicaoApagar) {

		if (!submetasClonadasGeradasPelaRejeicaoApagar.isEmpty()) {
			dao.apagarSubmetas(submetasClonadasGeradasPelaRejeicaoApagar.stream().
								map((SubmetaBD submeta) -> submeta.getId()).collect(Collectors.toList()));
		}
	}	

	
}
