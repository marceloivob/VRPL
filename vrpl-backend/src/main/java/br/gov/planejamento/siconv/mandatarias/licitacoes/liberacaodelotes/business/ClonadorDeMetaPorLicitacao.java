package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public class ClonadorDeMetaPorLicitacao {

	private LiberarLoteDAO dao;
	private String siglaEvento;

	public ClonadorDeMetaPorLicitacao(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeMetaPorLicitacao(LiberarLoteDAO dao) {
		this.dao = dao;
	}

	public List<Clone<MetaBD>> clone(LicitacaoBD licitacaoOriginal,
			List<Clone<SubitemInvestimentoBD>> cloneDeSubItemDeInvestimento) {

		List<MetaBD> metas = dao.selectMetaParaClonarPorLicitacao(licitacaoOriginal);

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
	

	public List<MetaBD> consultarClonesGerados(List<SubmetaBD> submetasClonadasGeradasPelaRejeicaoApagar, PropostaBD propostaClonada) {

		// Metas foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone 
		List<MetaBD> listaApagar = dao.selectMetasApagar(submetasClonadasGeradasPelaRejeicaoApagar.stream().
				map(submetaBD -> submetaBD.getId()).collect(Collectors.toList()));
		
		// metas que tem submetas que nao serao apagadas associadas a ele; ou seja, nao podem ser apagados
		List<MetaBD> listaNAOApagar = dao.selectMetaNAOApagar(propostaClonada, EventoQuadroResumoEnum.CANCELAR_REJEITE);
		
		listaApagar.removeAll(listaNAOApagar);
		
		return listaApagar; 
		
	}	


	public void apagarClone(List<MetaBD> metasClonadasGeradasPelaRejeicaoApagar) {

		if (!metasClonadasGeradasPelaRejeicaoApagar.isEmpty()) {
			dao.apagarMetas(metasClonadasGeradasPelaRejeicaoApagar.stream().
								map(meta -> meta.getId()).collect(Collectors.toList()));
		}
	}	

}
