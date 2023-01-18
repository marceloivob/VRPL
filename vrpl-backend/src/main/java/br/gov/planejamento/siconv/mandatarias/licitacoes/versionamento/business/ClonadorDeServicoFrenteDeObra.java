package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeServicoFrenteDeObra {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * Construtor
	 * @param dao
	 * @param evento
	 */
	public ClonadorDeServicoFrenteDeObra(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * retorna lista de clones
	 * @param listaCloneDeServico
	 * @param listaCloneDeFrenteDeObra
	 * @return
	 */
	public List<Clone<ServicoFrenteObraBD>> clone(List<Clone<ServicoBD>> listaCloneDeServico,
			List<Clone<FrenteObraBD>> listaCloneDeFrenteDeObra) {

		Map<Long, ServicoBD> mapaServicosClonados = new TreeMap<>();
		for (Clone<ServicoBD> clone: listaCloneDeServico) {
			mapaServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}
		
		Map<Long, FrenteObraBD> mapaFrentesObraClonadas = new TreeMap<>();
		for (Clone<FrenteObraBD> clone : listaCloneDeFrenteDeObra) {
			mapaFrentesObraClonadas.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}
		
		List<ServicoFrenteObraBD> servicosFrenteObraOriginais = new ArrayList<>();
		
		if( mapaServicosClonados.keySet() != null && !mapaServicosClonados.keySet().isEmpty() ) {
			servicosFrenteObraOriginais = dao.selectServicoFrenteDeObraParaClonar(new ArrayList<>(mapaServicosClonados.keySet()));
		}


		
		
		Map<String, ServicoFrenteObraBD> mapaServicosFrenteObraOriginal = new TreeMap<>();
		List<ServicoFrenteObraBD> listaParaInclusao = new ArrayList<>();

		for (ServicoFrenteObraBD servicoFrenteObraOriginal : servicosFrenteObraOriginais) {
			
			mapaServicosFrenteObraOriginal.put(servicoFrenteObraOriginal.getServicoFk()  +  "_" +  servicoFrenteObraOriginal.getFrenteObraFk(), servicoFrenteObraOriginal);
			
			ServicoBD servicoClonado = mapaServicosClonados.get(servicoFrenteObraOriginal.getServicoFk());
			
			FrenteObraBD frenteObraClonada = mapaFrentesObraClonadas.get(servicoFrenteObraOriginal.getFrenteObraFk());

			if (servicoClonado != null && frenteObraClonada != null) {
				
				ServicoFrenteObraBD clone  = criarCloneServicoFrenteObraParaInclusao(servicoFrenteObraOriginal, frenteObraClonada,
						servicoClonado, siglaEvento);
				
				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista um ServicoBD clonado e uma FrenteObraBD clonada para a ServicoFrenteObraBD "
								+ servicoFrenteObraOriginal);
			}
		}
		
		//incluir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneServicoFrenteObraBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsServicosClonados = listaCloneDeServico.stream().map(Clone::getObjetoClonado).map(ServicoBD::getId)
				.collect(Collectors.toList());
		
		List<ServicoFrenteObraBD> servicosFrenteDeObraClonados = idsServicosClonados.isEmpty() ? new ArrayList<>()
				: dao.selectServicoFrenteDeObraParaClonar(idsServicosClonados);
		
		//retornar lista
		List<Clone<ServicoFrenteObraBD>> clonesDeServicoFrenteDeObra = new ArrayList<>();
		for (ServicoFrenteObraBD clone : servicosFrenteDeObraClonados) {
			
			ServicoFrenteObraBD servicoFrenteOriginal = mapaServicosFrenteObraOriginal.get(clone.getVersaoId());
			clonesDeServicoFrenteDeObra.add(new Clone<ServicoFrenteObraBD>(servicoFrenteOriginal, clone));
		}

		return clonesDeServicoFrenteDeObra;
	}
	
	/**
	 * cria clone para insercao
	 * @param servicoFrenteObraOriginal
	 * @param frenteObraClonada
	 * @param servicoClonado
	 * @param siglaEvento
	 * @return
	 */
	protected ServicoFrenteObraBD criarCloneServicoFrenteObraParaInclusao(ServicoFrenteObraBD servicoFrenteObraOriginal, 
			FrenteObraBD frenteObraClonada, ServicoBD servicoClonado, String siglaEvento) {
		
		ServicoFrenteObraBD servicoFrenteObraClone = new ServicoFrenteObraBD();
		
		servicoFrenteObraClone.setServicoFk(servicoClonado.getId());
		servicoFrenteObraClone.setFrenteObraFk(frenteObraClonada.getId());
		servicoFrenteObraClone.setQtItens(servicoFrenteObraOriginal.getQtItens());
		servicoFrenteObraClone.setVersao(servicoFrenteObraOriginal.getVersao());
		servicoFrenteObraClone.setVersaoId(servicoFrenteObraOriginal.getServicoFk() + "_" + servicoFrenteObraOriginal.getFrenteObraFk());
		servicoFrenteObraClone.setVersaoNmEvento(siglaEvento);
		servicoFrenteObraClone.setVersaoNr(servicoFrenteObraOriginal.getVersaoNr() + 1);
		
		return servicoFrenteObraClone;
		
	}
}
