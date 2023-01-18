package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeEventoBatch {

	protected String siglaEvento;

	protected abstract List<EventoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais);
	protected abstract EventoBD criarCloneParaInclusao(EventoBD original, PoBD poPaiClonada);
	protected abstract void inserirClonesBatchDAO(List<EventoBD> listaParaInclusao);
	protected abstract List<EventoBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas);

	public List<Clone<EventoBD>> clone(List<Clone<PoBD>> listaclonesDePo) {
		//criar mapa chave Po original -> clone Po
		Map<Long, PoBD> mapaChavePoOriginalClone = criarMapaChavePoOriginalClone(listaclonesDePo);

		List<EventoBD> eventoOriginais = mapaChavePoOriginalClone.isEmpty() ? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaChavePoOriginalClone.keySet()));

		Map<Long, EventoBD> mapaChaveEventoOriginal = new TreeMap<>();
		List<EventoBD> listaParaInclusao = new ArrayList<>();

		//criar clones
		for (EventoBD eventoOriginal : eventoOriginais) {

			mapaChaveEventoOriginal.put(eventoOriginal.getId(), eventoOriginal);

			PoBD poPaiClonada = mapaChavePoOriginalClone.get(eventoOriginal.getIdPo());

			if (poPaiClonada != null) {
				EventoBD clone = criarCloneParaInclusao(eventoOriginal, poPaiClonada);
				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o EventoBD " + eventoOriginal);
			}
		}

		//inserir
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consultar
		List<Long> idsPosClonadas = extrairIdsPosClonadas(listaclonesDePo);

		List<EventoBD> eventoClonados = idsPosClonadas.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsPosClonadas);

		//criar saida
		List<Clone<EventoBD>> clonesDeEvento = new ArrayList<>();
		for (EventoBD clone : eventoClonados) {
			EventoBD eventoOriginal = mapaChaveEventoOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDeEvento.add(new Clone<>(eventoOriginal, clone));
		}

		return clonesDeEvento;
	}

	private List<Long> extrairIdsPosClonadas(List<Clone<PoBD>> listaclonesDePo) {
		return listaclonesDePo.stream().map(Clone::getObjetoClonado).map(PoBD::getId)
				.collect(Collectors.toList());
	}

	private Map<Long, PoBD> criarMapaChavePoOriginalClone(List<Clone<PoBD>> listaclonesDePo) {
		Map<Long, PoBD> mapaChavePoOriginalClone = new TreeMap<>();

		for (Clone<PoBD> clone : listaclonesDePo) {
			mapaChavePoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaChavePoOriginalClone;
	}
}
