package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeMacroServicoParcelaBatch {

	protected String siglaEvento;

	protected abstract List<MacroServicoParcelaBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(
			List<Long> idsMacroServicosOriginais);
	protected abstract MacroServicoParcelaBD criarCloneParaInclusao(MacroServicoParcelaBD original,
			MacroServicoBD macroServicoClonado);
	protected abstract void inserirClonesBatchDAO(List<MacroServicoParcelaBD> listaParaInclusao);
	protected abstract List<MacroServicoParcelaBD> consultarClonesEntidadesDAO(List<Long> idsMacroServicosClonados);

	public List<Clone<MacroServicoParcelaBD>> clone(List<Clone<MacroServicoBD>> listaCloneDeMacroServico) {

		// mapa chave original -> objeto clonado
		Map<Long, MacroServicoBD> mapaMacroServicosClonados = criarMapaMacroServicosClonados(listaCloneDeMacroServico);

		// lista de macroServicoParcela originais
		List<MacroServicoParcelaBD> macroServicoParcelaOriginais = mapaMacroServicosClonados.isEmpty()
				? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaMacroServicosClonados.keySet()));

		Map<Long, MacroServicoParcelaBD> mapaMacroServicosParcelaOriginais = new TreeMap<>();

		List<MacroServicoParcelaBD> listaParaInclusao = new ArrayList<>();

		for (MacroServicoParcelaBD macroServicoParcelaOriginal : macroServicoParcelaOriginais) {

			mapaMacroServicosParcelaOriginais.put(macroServicoParcelaOriginal.getId(), macroServicoParcelaOriginal);

			MacroServicoBD macroServicoClonado = mapaMacroServicosClonados.get(macroServicoParcelaOriginal.getMacroServicoFk());

			if (macroServicoClonado != null) {
				MacroServicoParcelaBD clone = criarCloneParaInclusao(macroServicoParcelaOriginal,
						macroServicoClonado);

				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista uma MacroServicoBD clonada para o MacroServicoParcelaBD "
								+ macroServicoParcelaOriginal);
			}
		}

		//insert
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consulta
		List<Long> idsMacroServicosClonados = extrairIdsMacroServicosClonados(listaCloneDeMacroServico);

		List<MacroServicoParcelaBD> listaMacrosServicoParcelaClonados = idsMacroServicosClonados.isEmpty()
				? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsMacroServicosClonados);

		//criar saida
		List<Clone<MacroServicoParcelaBD>> clonesDeMacroServico = new ArrayList<>();

		for (MacroServicoParcelaBD clone : listaMacrosServicoParcelaClonados) {
			MacroServicoParcelaBD original = mapaMacroServicosParcelaOriginais.get(Long.parseLong(clone.getVersaoId()));

			clonesDeMacroServico.add(new Clone<>(original, clone));
		}

		return clonesDeMacroServico;
	}

	private List<Long> extrairIdsMacroServicosClonados(List<Clone<MacroServicoBD>> listaCloneDeMacroServico) {
		return listaCloneDeMacroServico.stream().map(Clone::getObjetoClonado)
				.map(MacroServicoBD::getId).collect(Collectors.toList());
	}

	private Map<Long, MacroServicoBD> criarMapaMacroServicosClonados(
			List<Clone<MacroServicoBD>> listaCloneDeMacroServico) {

		Map<Long, MacroServicoBD> mapaMacroServicosClonados = new TreeMap<>();

		for (Clone<MacroServicoBD> clone : listaCloneDeMacroServico) {
			mapaMacroServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaMacroServicosClonados;
	}
}
