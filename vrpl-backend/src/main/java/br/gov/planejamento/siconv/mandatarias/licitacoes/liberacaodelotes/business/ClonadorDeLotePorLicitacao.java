package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeLotePorLicitacao {

	private LiberarLoteDAO dao;
	private VersionamentoDAO versionamentoDAO;
	private String siglaEvento;

	public ClonadorDeLotePorLicitacao(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	public ClonadorDeLotePorLicitacao(LiberarLoteDAO dao, VersionamentoDAO versionamentoDAO) {
		this.dao = dao;
		this.versionamentoDAO = versionamentoDAO;
	}	

	public List<Clone<LoteBD>> clone(LicitacaoBD licitacaoOriginal) {
		List<Clone<LoteBD>> clonesDeLote = new ArrayList<>();

		List<LoteBD> lotesOriginais = dao.selectLoteParaClonarPorLicitacao(licitacaoOriginal);

		for (LoteBD loteOriginal : lotesOriginais) {

			LoteBD clone = dao.cloneLicitacaoLote(loteOriginal, siglaEvento);

			clonesDeLote.add(new Clone<LoteBD>(loteOriginal, clone));
		}

		return clonesDeLote;
	}
	
	
	@Deprecated
	public List<LoteBD> consultarClonesGerados(PropostaBD propostaClonada) {
		// Lotes que foram gerados pela Rejeição no método clone e serão apagados pelo método apagarClone 
		return dao.selectLoteParaApagarPorProposta(propostaClonada, EventoQuadroResumoEnum.CANCELAR_REJEITE);
		
	}

	public List<LoteBD> consultarLotesApagar(PropostaBD propostaClonada, LicitacaoBD licitacao) {
		// Lotes que foram gerados pela Rejeição no método clone e serão apagados pelo método apagarClone 
		return dao.selectLoteLiberadosParaApagarDaLicitacaoRetornandoDaRejeicao(propostaClonada, licitacao);
	}
	
	public void apagarClone(List<LoteBD> lotesClonadosGeradosPelaRejeicaoApagar) {
		if (!lotesClonadosGeradosPelaRejeicaoApagar.isEmpty()) {
			dao.apagarLicitacaoLotes(lotesClonadosGeradosPelaRejeicaoApagar.stream().
									map((LoteBD lote) -> lote.getId()).collect(Collectors.toList()));
		}
	}
	
}

