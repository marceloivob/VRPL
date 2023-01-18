package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeEvento {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeEvento(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}


	public List<Clone<EventoBD>> clone(List<Clone<PoBD>> listaclonesDePo) {
		//criar mapa chave Po original -> clone Po
		Map<Long, PoBD> mapaChavePoOriginalClone = new TreeMap<>();
		for (Clone<PoBD> clone : listaclonesDePo) {
			mapaChavePoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<EventoBD> eventoOriginais = new ArrayList<>();
		
		if (!mapaChavePoOriginalClone.isEmpty()) {
				
			eventoOriginais = dao.selectEventoParaClonar(new ArrayList<>(mapaChavePoOriginalClone.keySet()));
		}
		
		Map<Long, EventoBD> mapaChaveEventoOriginal = new TreeMap<>();
		List<EventoBD> listaParaInclusao = new ArrayList<>();
		
		//criar clones
		for (EventoBD eventoOriginal : eventoOriginais) {
			
			mapaChaveEventoOriginal.put(eventoOriginal.getId(), eventoOriginal);

			PoBD PoPaiClonada = mapaChavePoOriginalClone.get(eventoOriginal.getIdPo());
			if (PoPaiClonada != null) {

				EventoBD clone = criarClonesParaInclusao(eventoOriginal, PoPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o EventoBD " + eventoOriginal);
			}
		}
		
		//inserir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneEventoBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsPosClonadas = listaclonesDePo.stream().map(Clone::getObjetoClonado).map(PoBD::getId)
		.collect(Collectors.toList());
		
		List<EventoBD> eventoClonados = idsPosClonadas.isEmpty()? new ArrayList<>() : dao.selectEventoParaClonar(idsPosClonadas);
		
		
		//criar saida
		List<Clone<EventoBD>> clonesDeEvento = new ArrayList<>();
		for (EventoBD clone : eventoClonados) {
			EventoBD eventoOriginal = mapaChaveEventoOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDeEvento.add(new Clone<EventoBD>(eventoOriginal, clone));
		}	

		return clonesDeEvento;
	}
	
	

	protected EventoBD criarClonesParaInclusao(EventoBD original, 
			PoBD PoPaiClonada, String siglaEvento) {
		
		EventoBD clone = new EventoBD();
		

		clone.setIdPo(PoPaiClonada.getId());
		clone.setIdAnalise(original.getIdAnalise());
		clone.setNomeEvento(original.getNomeEvento());
		clone.setNumeroEvento(original.getNumeroEvento());
		
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setNumeroVersao(original.getNumeroVersao() + 1);
											
		return clone;
		
	}
}
