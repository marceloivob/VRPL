package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeServicoBatch {
	protected String siglaEvento;

	protected abstract List<ServicoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsMacroServicosOriginais);
	protected abstract ServicoBD criarCloneParaInclusao(ServicoBD servicoOriginal,
			MacroServicoBD macroServicoClonado, EventoBD eventoClonado);
	protected abstract void inserirClonesBatchDAO(List<ServicoBD> listaParaInclusao);
	protected abstract List<ServicoBD> consultarClonesEntidadesDAO(List<Long> idsMacroServicosClonados);

	public List<Clone<ServicoBD>> clone (List<Clone<EventoBD>> listaClonesDeEventos,
			List<Clone<MacroServicoBD>> listaClonesDeMacroServicos) {

		// Criar mapas "Id Entidade Original - Clone" para agilizar operacoes
		Map<Long,Clone<MacroServicoBD>> mapaMacroServicosClonados = criarMapaMacroServicosClonados(listaClonesDeMacroServicos);
		Map<Long,Clone<EventoBD>> mapaEventosClonados = criarMapaEventosClonados(listaClonesDeEventos);

		List<ServicoBD> servicosOriginais = mapaMacroServicosClonados.isEmpty() ? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaMacroServicosClonados.keySet()));

		List<Clone<ServicoBD>> clonesDeServico = new ArrayList<>();
		Map<Long,ServicoBD> mapaServicosOriginais = new TreeMap<>();
		List<ServicoBD> listaParaInclusao = new ArrayList<>();

		for (ServicoBD servicoOriginal : servicosOriginais) {

			mapaServicosOriginais.put(servicoOriginal.getId(), servicoOriginal);

			Clone<EventoBD> eventoClonadoMapa = null;
			if (servicoOriginal.getEventoFk() != null) {
				eventoClonadoMapa = mapaEventosClonados.get(servicoOriginal.getEventoFk());
			}

			Clone<MacroServicoBD> macroServicosClonadoMapa = mapaMacroServicosClonados.get(servicoOriginal.getMacroServicoFk());

			EventoBD eventoClonado = new EventoBD();

			if (eventoClonadoMapa != null) {
				eventoClonado = eventoClonadoMapa.getObjetoClonado();
			}

			if (macroServicosClonadoMapa != null) {
				MacroServicoBD macroServicoClonado = macroServicosClonadoMapa.getObjetoClonado();

				ServicoBD cloneServico = criarCloneParaInclusao(servicoOriginal, macroServicoClonado, eventoClonado);

				listaParaInclusao.add(cloneServico);

			} else {
				throw new IllegalStateException(
						"É esperado que exista um EventoBD clonado e um MacroServicoBD clonad para o ServicoBD "
								+ servicoOriginal);
			}
		}

		// Inclusao em lote
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		// Consulta por macroservicos clonados
		List<Long> idsMacroServicosClonados = extrairIdsMacroServicosClonados(listaClonesDeMacroServicos);

		List<ServicoBD> servicosIncluidos = idsMacroServicosClonados.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsMacroServicosClonados);

		// Criar relacoes
		for (ServicoBD clone: servicosIncluidos) {
			ServicoBD servicoOriginal = mapaServicosOriginais.get(Long.parseLong(clone.getVersaoId()));
			clonesDeServico.add(new Clone<>(servicoOriginal, clone));
		}

		return clonesDeServico;
	}

	private List<Long> extrairIdsMacroServicosClonados(List<Clone<MacroServicoBD>> listaClonesDeMacroServicos) {
		return listaClonesDeMacroServicos.stream().map(Clone::getObjetoClonado).map(MacroServicoBD::getId).collect(Collectors.toList());
	}

	private Map<Long, Clone<EventoBD>> criarMapaEventosClonados(List<Clone<EventoBD>> listaClonesDeEventos) {

		Map<Long,Clone<EventoBD>> mapaEventosClonados = new TreeMap<>();

		for (Clone<EventoBD> clone : listaClonesDeEventos) {
			mapaEventosClonados.put(clone.getObjetoOriginal().getId(), clone);
		}

		return mapaEventosClonados;
	}

	private Map<Long, Clone<MacroServicoBD>> criarMapaMacroServicosClonados(
			List<Clone<MacroServicoBD>> listaClonesDeMacroServicos) {

		Map<Long,Clone<MacroServicoBD>> mapaMacroServicosClonados = new TreeMap<>();

		for (Clone<MacroServicoBD> clone : listaClonesDeMacroServicos) {
			mapaMacroServicosClonados.put(clone.getObjetoOriginal().getId(), clone);
		}

		return mapaMacroServicosClonados;
	}
}
