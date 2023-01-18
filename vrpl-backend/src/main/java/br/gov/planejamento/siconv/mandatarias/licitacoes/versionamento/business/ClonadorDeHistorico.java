package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeHistorico {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * construtor
	 * @param dao
	 * @param evento
	 */
	public ClonadorDeHistorico(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * retornar lista de clones
	 * @param cloneDeLicitacao
	 * @return
	 */
	public List<Clone<HistoricoLicitacaoBD>> clone(List<Clone<LicitacaoBD>> cloneDeLicitacao) {
					
		Map<Long, LicitacaoBD> mapaLicitacaoesClonadas = new TreeMap<>();
		for (Clone<LicitacaoBD> clone : cloneDeLicitacao) {
			mapaLicitacaoesClonadas.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<HistoricoLicitacaoBD> listaParaInclusao = new ArrayList<>();

		List<HistoricoLicitacaoBD> historicosOriginais = mapaLicitacaoesClonadas.isEmpty() ? new ArrayList<>()
				: dao.selectHistoricoParaClonar(new ArrayList<>(mapaLicitacaoesClonadas.keySet()));
		
		Map<Long, HistoricoLicitacaoBD> mapaHistoricoOriginais = new TreeMap<>();
		//criar clones
		for (HistoricoLicitacaoBD historicoOriginal : historicosOriginais) {
			
			mapaHistoricoOriginais.put(historicoOriginal.getId(), historicoOriginal);

			LicitacaoBD licitacaoPaiClonada = mapaLicitacaoesClonadas.get(historicoOriginal.getIdentificadorDaLicitacao());

			if (licitacaoPaiClonada != null) {
						
				HistoricoLicitacaoBD clone = this.criarCloneParaInsercao(historicoOriginal, licitacaoPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
			
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma LicitacaoBD clonada para o HistoricoLicitacaoBD "
								+ historicoOriginal);
			}
		}
		
		//insert
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneHistoricoBatch(listaParaInclusao);
		}
		
		//consulta
		List<Long> idsLicitacoesClonadas = cloneDeLicitacao.stream().map(Clone::getObjetoClonado)
		.map(LicitacaoBD::getId).collect(Collectors.toList());
		
		List<HistoricoLicitacaoBD> historicosClonados = idsLicitacoesClonadas.isEmpty() ? new ArrayList<>()
				: dao.selectHistoricoParaClonar(new ArrayList<>(idsLicitacoesClonadas));
		
		//criar saida
		List<Clone<HistoricoLicitacaoBD>> clonesDeHistorico = new ArrayList<>();
		for (HistoricoLicitacaoBD clone : historicosClonados) {
			HistoricoLicitacaoBD original = mapaHistoricoOriginais.get(Long.parseLong(clone.getVersaoId()));
			clonesDeHistorico.add(new Clone<HistoricoLicitacaoBD>(original, clone));
		}
		
		return clonesDeHistorico;
	}
	
	/**
	 * criar clone do historico 
	 * @return
	 */
	protected HistoricoLicitacaoBD criarCloneParaInsercao (HistoricoLicitacaoBD original, 
			LicitacaoBD licitacao, String siglaEvento) {
		
		HistoricoLicitacaoBD clone = new HistoricoLicitacaoBD();
			
		clone.setIdentificadorDaLicitacao(licitacao.getId());
		clone.setEventoGerador(original.getEventoGerador());
		clone.setSituacaoDaLicitacao(original.getSituacaoDaLicitacao());
		clone.setConsideracoes(original.getConsideracoes());
		clone.setDataDeRegistro(original.getDataDeRegistro());
		clone.setNomeDoResponsavel(original.getNomeDoResponsavel());
		clone.setCpfDoResponsavel(original.getCpfDoResponsavel());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setVersaoNr(original.getVersaoNr() + 1);
		
		return clone;
	}

}
