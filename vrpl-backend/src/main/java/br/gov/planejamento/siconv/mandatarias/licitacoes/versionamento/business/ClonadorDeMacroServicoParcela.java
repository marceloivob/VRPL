package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeMacroServicoParcela {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * Construtor
	 * @param dao
	 * @param evento
	 */
	public ClonadorDeMacroServicoParcela(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * retorna lista de clones
	 * @param cloneDeMacroServico
	 * @return
	 */
	public List<Clone<MacroServicoParcelaBD>> clone(List<Clone<MacroServicoBD>> cloneDeMacroServico) {
				
		
		// mapa chave original -> objeto clonado
		Map<Long, MacroServicoBD> mapaMacroServicosClonados = new TreeMap<>();
		for (Clone<MacroServicoBD> clone : cloneDeMacroServico) {
			mapaMacroServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());

		}
		
		// lista de macroServicoParcela originais
		List<MacroServicoParcelaBD> macroServicoParcelaOriginais = mapaMacroServicosClonados.isEmpty()
				? new ArrayList<>()
				: dao.selectMacroServicoParcelaParaClonar(new ArrayList<Long>(mapaMacroServicosClonados.keySet()));

		
		Map<Long, MacroServicoParcelaBD> mapaMacroServicosParcelaOriginais = new TreeMap<>();
		List<MacroServicoParcelaBD> listaParaInclusao = new ArrayList<>();
		
		for (MacroServicoParcelaBD macroServicoParcelaOriginal : macroServicoParcelaOriginais) {

			mapaMacroServicosParcelaOriginais.put(macroServicoParcelaOriginal.getId(), macroServicoParcelaOriginal);
			
			MacroServicoBD macroServicoClonado = mapaMacroServicosClonados.get(macroServicoParcelaOriginal.getMacroServicoFk());
			if (macroServicoClonado != null) {
				
				MacroServicoParcelaBD clone = criarCloneMacroServicoParcela(macroServicoParcelaOriginal,
						macroServicoClonado, siglaEvento);
				
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma MacroServicoBD clonada para o MacroServicoParcelaBD "
								+ macroServicoParcelaOriginal);
			}
		}
		
		//insert
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneMacroServicoParcelaBatch(listaParaInclusao);
		}
		//consulta
		List<Long> idsMacroServicosClonados = cloneDeMacroServico.stream().map(Clone::getObjetoClonado)
		.map(MacroServicoBD::getId).collect(Collectors.toList());
		
		List<MacroServicoParcelaBD> listaMacrosServicoParcelaClonados = idsMacroServicosClonados.isEmpty()
				? new ArrayList<>()
				: dao.selectMacroServicoParcelaParaClonar(idsMacroServicosClonados);
		
		//criar saida
		List<Clone<MacroServicoParcelaBD>> clonesDeMacroServico = new ArrayList<>();
		for (MacroServicoParcelaBD clone : listaMacrosServicoParcelaClonados) {
			MacroServicoParcelaBD original = mapaMacroServicosParcelaOriginais.get(Long.parseLong(clone.getVersaoId()));
			
			clonesDeMacroServico.add(new Clone<MacroServicoParcelaBD>(original, clone));
		}
		return clonesDeMacroServico;
	}
	
	protected MacroServicoParcelaBD criarCloneMacroServicoParcela(MacroServicoParcelaBD original, 
			MacroServicoBD macroServicoClonado, String siglaEvento) {
		MacroServicoParcelaBD clone = new MacroServicoParcelaBD();
		
		clone.setNrParcela(original.getNrParcela());
		clone.setPcParcela(original.getPcParcela());
		clone.setMacroServicoFk(macroServicoClonado.getId());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setNumeroVersao(original.getNumeroVersao() + 1);																									
		
		return clone;
	}
}
