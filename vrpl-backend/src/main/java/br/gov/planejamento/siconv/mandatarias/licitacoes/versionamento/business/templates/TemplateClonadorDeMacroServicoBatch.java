package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeMacroServicoBatch {

	protected String siglaEvento;

	protected abstract List<MacroServicoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais);
	protected abstract MacroServicoBD criarCloneParaInclusao(MacroServicoBD original, PoBD poPaiClonada);
	protected abstract void inserirClonesBatchDAO(List<MacroServicoBD> listaParaInclusao);
	protected abstract List<MacroServicoBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas);

	public List<Clone<MacroServicoBD>> clone(List<Clone<PoBD>> listaclonesDePO) {

		//criar mapa chave po original -> clone po
		Map<Long, PoBD> mapaChavePOOriginalClone = criarMapaChavePOOriginalClone(listaclonesDePO);

		List<MacroServicoBD> macroServicoOriginais = mapaChavePOOriginalClone.isEmpty() ? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaChavePOOriginalClone.keySet()));

		Map<Long, MacroServicoBD> mapaChaveMacroservicoOriginal = new TreeMap<>();
		List<MacroServicoBD> listaParaInclusao = new ArrayList<>();

		//criar clones
		for (MacroServicoBD macroServicoOriginal : macroServicoOriginais) {

			mapaChaveMacroservicoOriginal.put(macroServicoOriginal.getId(), macroServicoOriginal);

			PoBD poPaiClonada = mapaChavePOOriginalClone.get(macroServicoOriginal.getPoFk());

			if (poPaiClonada != null) {
				MacroServicoBD clone = criarCloneParaInclusao(macroServicoOriginal, poPaiClonada);
				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o MacroServicoBD " + macroServicoOriginal);
			}
		}

		//inserir
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consultar
		List<Long> idsPosClonadas = extrairIdsPosClonadas(listaclonesDePO);

		List<MacroServicoBD> macroServicoClonados = idsPosClonadas.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsPosClonadas);


		//criar saida
		List<Clone<MacroServicoBD>> clonesDeMacroServico = new ArrayList<>();

		for (MacroServicoBD clone : macroServicoClonados) {
			MacroServicoBD macroServicoOriginal = mapaChaveMacroservicoOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDeMacroServico.add(new Clone<>(macroServicoOriginal, clone));
		}

		return clonesDeMacroServico;
	}

	private List<Long> extrairIdsPosClonadas(List<Clone<PoBD>> listaclonesDePO) {
		return listaclonesDePO.stream().map(Clone::getObjetoClonado).map(PoBD::getId)
					.collect(Collectors.toList());
	}

	private Map<Long, PoBD> criarMapaChavePOOriginalClone(List<Clone<PoBD>> listaclonesDePO) {
		Map<Long, PoBD> mapaChavePOOriginalClone = new TreeMap<>();

		for (Clone<PoBD> clone : listaclonesDePO) {
			mapaChavePOOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaChavePOOriginalClone;
	}
}
