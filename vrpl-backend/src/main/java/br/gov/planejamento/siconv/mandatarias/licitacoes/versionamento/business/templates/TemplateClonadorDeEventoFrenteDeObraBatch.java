package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeEventoFrenteDeObraBatch {

	protected String siglaEvento;

	protected abstract List<EventoFrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsEventosOriginais);
	protected abstract EventoFrenteObraBD criarCloneParaInclusao(EventoFrenteObraBD original,
			EventoBD eventoPaiClonada, FrenteObraBD frenteObraPaiClonada);
	protected abstract void inserirClonesBatchDAO(List<EventoFrenteObraBD> listaParaInclusao);
	protected abstract List<EventoFrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsEventosClonadas);

	public List<Clone<EventoFrenteObraBD>> clone(List<Clone<EventoBD>> listaClonesDeEvento,
			List<Clone<FrenteObraBD>> listaClonesDeFrenteDeObra) {

		// Criar mapa chave Evento original -> clone Evento
		Map<Long, EventoBD> mapaChaveEventoOriginalClone = criarMapaChaveEventoOriginalClone(listaClonesDeEvento);

		//criar mapa chave Frente de Obra original -> clone FrenteObra
		Map<Long, FrenteObraBD> mapaChaveFrenteObraOriginalClone = criarMapaChaveFrenteObraOriginalClone(listaClonesDeFrenteDeObra);

		List<EventoFrenteObraBD> eventoFrenteObraOriginais = mapaChaveEventoOriginalClone.isEmpty() ? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaChaveEventoOriginalClone.keySet()));

		Map<String, EventoFrenteObraBD> mapaChaveEventoFrenteObraOriginal = new TreeMap<>();
		List<EventoFrenteObraBD> listaParaInclusao = new ArrayList<>();

		//criar clones
		for (EventoFrenteObraBD eventoFrenteObraOriginal : eventoFrenteObraOriginais) {

			mapaChaveEventoFrenteObraOriginal.put(eventoFrenteObraOriginal.getEventoFk() + "_" + eventoFrenteObraOriginal.getFrenteObraFk(), eventoFrenteObraOriginal);

			EventoBD eventoPaiClonada = mapaChaveEventoOriginalClone.get(eventoFrenteObraOriginal.getEventoFk());
			FrenteObraBD frenteObraPaiClonada = mapaChaveFrenteObraOriginalClone.get(eventoFrenteObraOriginal.getFrenteObraFk());

			if (eventoPaiClonada != null && frenteObraPaiClonada != null) {
				EventoFrenteObraBD clone = criarCloneParaInclusao(eventoFrenteObraOriginal, eventoPaiClonada, frenteObraPaiClonada);

				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista uma EventoBD e uma FrenteObraBD clonada para o EventoFrenteObraBD " + eventoFrenteObraOriginal);
			}
		}

		//inserir
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consultar
		List<Long> idsEventosClonadas = extrairIdsEventosClonados(listaClonesDeEvento);

		List<EventoFrenteObraBD> eventoFrenteObraClonados = idsEventosClonadas.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsEventosClonadas);

		List<Clone<EventoFrenteObraBD>> clonesDeEventoFrenteObra = new ArrayList<>();

		//criar saida
		for (EventoFrenteObraBD clone : eventoFrenteObraClonados) {
			EventoFrenteObraBD original = mapaChaveEventoFrenteObraOriginal.get(clone.getVersaoId());
			clonesDeEventoFrenteObra.add(new Clone<>(original, clone));
		}

		return clonesDeEventoFrenteObra;
	}

	private List<Long> extrairIdsEventosClonados(List<Clone<EventoBD>> listaClonesDeEvento) {
		return listaClonesDeEvento.stream().map(Clone::getObjetoClonado).map(EventoBD::getId)
					.collect(Collectors.toList());
	}

	private Map<Long, FrenteObraBD> criarMapaChaveFrenteObraOriginalClone(
			List<Clone<FrenteObraBD>> listaClonesDeFrenteDeObra) {

		Map<Long, FrenteObraBD> mapaChaveFrenteObraOriginalClone = new TreeMap<>();

		for (Clone<FrenteObraBD> clone : listaClonesDeFrenteDeObra) {
			mapaChaveFrenteObraOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaChaveFrenteObraOriginalClone;
	}

	private Map<Long, EventoBD> criarMapaChaveEventoOriginalClone(List<Clone<EventoBD>> listaClonesDeEvento) {
		Map<Long, EventoBD> mapaChaveEventoOriginalClone = new TreeMap<>();

		for (Clone<EventoBD> clone : listaClonesDeEvento) {
			mapaChaveEventoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaChaveEventoOriginalClone;
	}
}
