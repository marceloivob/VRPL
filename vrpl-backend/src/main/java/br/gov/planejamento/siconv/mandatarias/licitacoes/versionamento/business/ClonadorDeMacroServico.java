package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeMacroServico {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/*
	 * Construtor
	 */
	public ClonadorDeMacroServico(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * Clonar 
	 * @param listaclonesDePO
	 * @return
	 */
	public List<Clone<MacroServicoBD>> clone(List<Clone<PoBD>> listaclonesDePO) {
		List<Clone<MacroServicoBD>> clonesDeMacroServico = new ArrayList<>();
		

		//criar mapa chave po original -> clone po
		Map<Long, PoBD> mapaChavePOOriginalClone = new TreeMap<>();
		for (Clone<PoBD> clone : listaclonesDePO) {
			mapaChavePOOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<MacroServicoBD> macroServicoOriginais = mapaChavePOOriginalClone.isEmpty() ? new ArrayList<>()
				: dao.selectMacroServicoParaClonar(new ArrayList<>(mapaChavePOOriginalClone.keySet()));
		
		Map<Long, MacroServicoBD> mapaChaveMacroservicoOriginal = new TreeMap<>();
		List<MacroServicoBD> listaParaInclusao = new ArrayList<>();
		
		//criar clones
		for (MacroServicoBD macroServicoOriginal : macroServicoOriginais) {
			
			mapaChaveMacroservicoOriginal.put(macroServicoOriginal.getId(), macroServicoOriginal);

			PoBD poPaiClonada = mapaChavePOOriginalClone.get(macroServicoOriginal.getPoFk());
			if (poPaiClonada != null) {

				MacroServicoBD clone = criarClonesParaInclusao(macroServicoOriginal, poPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o MacroServicoBD " + macroServicoOriginal);
			}
		}
		
		//inserir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneMacroServicoBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsPosClonadas = listaclonesDePO.stream().map(Clone::getObjetoClonado).map(PoBD::getId)
		.collect(Collectors.toList());
		
		List<MacroServicoBD> macroServicoClonados = idsPosClonadas.isEmpty() ? new ArrayList<>()
				: dao.selectMacroServicoParaClonar(idsPosClonadas);
		
		
		//criar saida
		for (MacroServicoBD clone : macroServicoClonados) {
			MacroServicoBD macroServicoOriginal = mapaChaveMacroservicoOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDeMacroServico.add(new Clone<MacroServicoBD>(macroServicoOriginal, clone));
		}	

		return clonesDeMacroServico;
	}
	
	
	/**
	 * Criar clone para inclusao
	 * @param original
	 * @param poPaiClonada
	 * @param siglaEvento
	 * @return
	 */
	protected MacroServicoBD criarClonesParaInclusao(MacroServicoBD original, 
			PoBD poPaiClonada, String siglaEvento) {
		
		MacroServicoBD clone = new MacroServicoBD();
		
		clone.setTxDescricao(original.getTxDescricao());
		clone.setPoFk(poPaiClonada.getId());
		clone.setNrMacroServico(original.getNrMacroServico());
		clone.setIdMacroServicoAnalise(original.getIdMacroServicoAnalise());
		
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setVersaoNr(original.getVersaoNr() + 1);
											
		return clone;
		
	}

}
