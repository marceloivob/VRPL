package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public class ClonadorDeSubItemDeInvestimentoPorLicitacao {

	private LiberarLoteDAO dao;
	private String siglaEvento;

	public ClonadorDeSubItemDeInvestimentoPorLicitacao(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeSubItemDeInvestimentoPorLicitacao(LiberarLoteDAO dao) {
		this.dao = dao;
	}

	public List<Clone<SubitemInvestimentoBD>> clone(LicitacaoBD licitacaoOriginal) {

		List<SubitemInvestimentoBD> subitemsDeInvestimento = dao
				.selectSubItemDeInvestimentoParaClonarPorLicitacao(licitacaoOriginal);

		List<Clone<SubitemInvestimentoBD>> clonesDeSubItemDeInvestimento = new ArrayList<>();

		for (SubitemInvestimentoBD original : subitemsDeInvestimento) {

			SubitemInvestimentoBD clone = dao.cloneSubItemDeInvestimento(original, siglaEvento);

			clonesDeSubItemDeInvestimento.add(new Clone<SubitemInvestimentoBD>(original, clone));
		}

		return clonesDeSubItemDeInvestimento;
	}

	
	public List<SubitemInvestimentoBD> consultarClonesGerados(List<MetaBD> metasClonadasGeradasPelaRejeicaoApagar, PropostaBD propostaClonada) {

		// Subitem Investimentos que foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone
		List<SubitemInvestimentoBD> listaApagar = dao.selectSubItemDeInvestimentoApagar(
				metasClonadasGeradasPelaRejeicaoApagar.stream().map(meta -> meta.getSubItemFk()).collect(Collectors.toList())
			); 
		
		// subitens que tem metas que nao serao apagadas associadas a ele; ou seja, nao podem ser apagados
		List<SubitemInvestimentoBD> listaNAOApagar = dao.selectSubItemDeInvestimentoNAOApagar(propostaClonada, EventoQuadroResumoEnum.CANCELAR_REJEITE);
		
		listaApagar.removeAll(listaNAOApagar);
		
		return listaApagar; 
		
	}	
	
	
	public void apagarClone(List<SubitemInvestimentoBD> subitemInvestimentoClonadosGeradosPelaRejeicaoApagar) {

		if (!subitemInvestimentoClonadosGeradosPelaRejeicaoApagar.isEmpty()) {
	
			dao.apagarSubitemInvestimentos(subitemInvestimentoClonadosGeradosPelaRejeicaoApagar.stream().
								map(subIteminvestimento -> subIteminvestimento.getId()).collect(Collectors.toList()));
		}
	}	
	
}
