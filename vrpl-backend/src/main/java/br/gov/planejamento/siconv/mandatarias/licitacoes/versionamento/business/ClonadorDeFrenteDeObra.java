package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeFrenteDeObra {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * Construtor
	 * @param dao
	 * @param evento
	 */
	public ClonadorDeFrenteDeObra(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	/**
	 * Retona lista de clones
	 * @param listaclonesDePo
	 * @return
	 */
	public List<Clone<FrenteObraBD>> clone(List<Clone<PoBD>> listaclonesDePo) {
		//criar mapa chave Po original -> clone Po
		Map<Long, PoBD> mapaChavePoOriginalClone = new TreeMap<>();
		for (Clone<PoBD> clone : listaclonesDePo) {
			mapaChavePoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<FrenteObraBD> frenteDeObraOriginais = mapaChavePoOriginalClone.isEmpty() ? new ArrayList<>()
				: dao.selectFrenteDeObraParaClonar(new ArrayList<>(mapaChavePoOriginalClone.keySet()));

		Map<Long, FrenteObraBD> mapaChaveFrenteDeObraOriginal = new TreeMap<>();
		List<FrenteObraBD> listaParaInclusao = new ArrayList<>();
		
		//criar clones
		for (FrenteObraBD frenteDeObraOriginal : frenteDeObraOriginais) {
			
			mapaChaveFrenteDeObraOriginal.put(frenteDeObraOriginal.getId(), frenteDeObraOriginal);

			PoBD poPaiClonada = mapaChavePoOriginalClone.get(frenteDeObraOriginal.getPoFk());
			if (poPaiClonada != null) {

				FrenteObraBD clone = criarClonesParaInclusao(frenteDeObraOriginal, poPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o FrenteDeObraBD " + frenteDeObraOriginal);
			}
		}
		
		//inserir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneFrenteDeObraBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsPosClonadas = listaclonesDePo.stream().map(Clone::getObjetoClonado).map(PoBD::getId)
		.collect(Collectors.toList());
		
		List<FrenteObraBD> frenteDeObraClonados = dao.selectFrenteDeObraParaClonar(idsPosClonadas);
		
		//criar saida
		List<Clone<FrenteObraBD>> clonesDeFrenteDeObra = new ArrayList<>();
		
		for (FrenteObraBD clone : frenteDeObraClonados) {
			FrenteObraBD frenteDeObraOriginal = mapaChaveFrenteDeObraOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDeFrenteDeObra.add(new Clone<FrenteObraBD>(frenteDeObraOriginal, clone));
		}	

		return clonesDeFrenteDeObra;
	}
	
	
	/**
	 * Cria um clone
	 * @param original
	 * @param PoPaiClonada
	 * @param siglaEvento
	 * @return
	 */
	protected FrenteObraBD criarClonesParaInclusao(FrenteObraBD original, 
			PoBD PoPaiClonada, String siglaEvento) {
		
		FrenteObraBD clone = new FrenteObraBD();
		

		clone.setPoFk(PoPaiClonada.getId());
		clone.setNmFrenteObra(original.getNmFrenteObra());
		clone.setNrFrenteObra(original.getNrFrenteObra());
		
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setNumeroVersao(original.getNumeroVersao() + 1);

											
		return clone;
		
	}

}
