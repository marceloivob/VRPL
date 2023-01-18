package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeEventoFrenteDeObra {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * Construtor
	 * @param dao
	 * @param evento
	 */
	public ClonadorDeEventoFrenteDeObra(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * Criar lista de clones
	 * @param listaClonesDeEvento
	 * @param listaClonesDeFrenteDeObra
	 * @return
	 */
	public List<Clone<EventoFrenteObraBD>> clone(List<Clone<EventoBD>> listaClonesDeEvento,
			List<Clone<FrenteObraBD>> listaClonesDeFrenteDeObra) {

		//criar mapa chave Evento original -> clone Evento
		Map<Long, EventoBD> mapaChaveEventoOriginalClone = new TreeMap<>();
		for (Clone<EventoBD> clone : listaClonesDeEvento) {
			mapaChaveEventoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}
		
		//criar mapa chave Frente de Obra original -> clone FrenteObra
		Map<Long, FrenteObraBD> mapaChaveFrenteObraOriginalClone = new TreeMap<>();
		for (Clone<FrenteObraBD> clone : listaClonesDeFrenteDeObra) {
			mapaChaveFrenteObraOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<EventoFrenteObraBD> eventoFrenteObraOriginais = new ArrayList<>();
		
		if (!mapaChaveEventoOriginalClone.isEmpty()) {
			eventoFrenteObraOriginais = dao.selectEventoFrenteDeObraParaClonar(new ArrayList<>(mapaChaveEventoOriginalClone.keySet()));
		}
		Map<String, EventoFrenteObraBD> mapaChaveEventoFrenteObraOriginal = new TreeMap<>();
		List<EventoFrenteObraBD> listaParaInclusao = new ArrayList<>();
		
		//criar clones
		for (EventoFrenteObraBD eventoFrenteObraOriginal : eventoFrenteObraOriginais) {
			
			mapaChaveEventoFrenteObraOriginal.put(eventoFrenteObraOriginal.getEventoFk() + "_" + eventoFrenteObraOriginal.getFrenteObraFk(), eventoFrenteObraOriginal);

			EventoBD eventoPaiClonada = mapaChaveEventoOriginalClone.get(eventoFrenteObraOriginal.getEventoFk());
			FrenteObraBD frenteObraPaiClonada = mapaChaveFrenteObraOriginalClone.get(eventoFrenteObraOriginal.getFrenteObraFk());
			if (eventoPaiClonada != null && frenteObraPaiClonada != null) {

				EventoFrenteObraBD clone = criarClonesParaInclusao(eventoFrenteObraOriginal, eventoPaiClonada, frenteObraPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma EventoBD e uma FrenteObraBD clonada para o EventoFrenteObraBD " + eventoFrenteObraOriginal);
			}
		}
		
		//inserir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneEventoFrenteObraBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsEventosClonadas = listaClonesDeEvento.stream().map(Clone::getObjetoClonado).map(EventoBD::getId)
		.collect(Collectors.toList());
		
		List<EventoFrenteObraBD> eventoFrenteObraClonados = idsEventosClonadas.isEmpty() ?  new ArrayList<>(): dao.selectEventoFrenteDeObraParaClonar(idsEventosClonadas);
		
		List<Clone<EventoFrenteObraBD>> clonesDeEventoFrenteObra = new ArrayList<>();
		//criar saida
		for (EventoFrenteObraBD clone : eventoFrenteObraClonados) {
			EventoFrenteObraBD EventoFrenteObraOriginal = mapaChaveEventoFrenteObraOriginal.get(clone.getVersaoId());
			clonesDeEventoFrenteObra.add(new Clone<EventoFrenteObraBD>(EventoFrenteObraOriginal, clone));
		}	

		return clonesDeEventoFrenteObra;
	}
		
		
	/**
	 * Criar Clone
	 * @param original
	 * @param EventoPaiClonada
	 * @param frenteObraPaiClonada
	 * @param siglaEvento
	 * @return
	 */
	protected EventoFrenteObraBD criarClonesParaInclusao(EventoFrenteObraBD original, 
			EventoBD EventoPaiClonada, FrenteObraBD frenteObraPaiClonada, String siglaEvento) {
		
		EventoFrenteObraBD clone = new EventoFrenteObraBD();
		

		clone.setEventoFk(EventoPaiClonada.getId());
		clone.setFrenteObraFk(frenteObraPaiClonada.getId());
		clone.setNrMesConclusao(original.getNrMesConclusao());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getEventoFk() + "_" + original.getFrenteObraFk());
		clone.setVersaoNmEvento(siglaEvento);
		clone.setVersaoNr(original.getVersaoNr() + 1);
											
		return clone;
		
	}
}
