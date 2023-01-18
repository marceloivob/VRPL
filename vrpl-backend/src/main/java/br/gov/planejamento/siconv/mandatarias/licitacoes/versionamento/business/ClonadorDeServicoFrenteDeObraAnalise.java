package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeServicoFrenteDeObraAnalise {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeServicoFrenteDeObraAnalise(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}
	
	/**
	 * retornar lista de clones
	 * @param listaCloneDeServico
	 * @return
	 */
	public List<Clone<ServicoFrenteObraAnaliseBD>> clone(List<Clone<ServicoBD>> listaCloneDeServico) {
		
		Map<Long, ServicoBD> mapaServicosClonados = new TreeMap<>();
		for (Clone<ServicoBD> clone: listaCloneDeServico) {
			mapaServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<ServicoFrenteObraAnaliseBD> servicosFrenteObraAnaliseOriginais = mapaServicosClonados.isEmpty()
				? new ArrayList<>()
				: dao.selectServicoFrenteDeObraAnaliseParaClonar(new ArrayList<>(mapaServicosClonados.keySet()));
		
		Map<Long, ServicoFrenteObraAnaliseBD> mapaServicosFrenteObraAnaliseOriginal = new TreeMap<>();
		List<ServicoFrenteObraAnaliseBD> listaParaInclusao = new ArrayList<>();
		
		for (ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseOriginal : servicosFrenteObraAnaliseOriginais) {
			
			mapaServicosFrenteObraAnaliseOriginal.put(servicoFrenteObraAnaliseOriginal.getId(), servicoFrenteObraAnaliseOriginal);
			
			ServicoBD servicoClonado = mapaServicosClonados.get(servicoFrenteObraAnaliseOriginal.getServicoFk());
			if (servicoClonado != null) {


				ServicoFrenteObraAnaliseBD clone = this.criarCloneServicoFrenteObraAnaliseParaInclusao(servicoFrenteObraAnaliseOriginal,
						servicoClonado, siglaEvento);
				
				listaParaInclusao.add(clone);
			
			} else {
				throw new IllegalStateException(
						"É esperado que exista um ServicoBD clonado para a ServicoFrenteObraAnaliseBD "
								+ servicoFrenteObraAnaliseOriginal);
			}
		}
		
		//incluir
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneServicoFrenteObraAnaliseBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsServicosClonados = listaCloneDeServico.stream().map(Clone::getObjetoClonado).map(ServicoBD::getId)
				.collect(Collectors.toList());
		
		List<ServicoFrenteObraAnaliseBD> listaClones = idsServicosClonados.isEmpty() ? new ArrayList<>()
				: dao.selectServicoFrenteDeObraAnaliseParaClonar(idsServicosClonados);
	
		//retornar lista
		List<Clone<ServicoFrenteObraAnaliseBD>> clonesDeServicoFrenteDeObraAnalise = new ArrayList<>();
		for (ServicoFrenteObraAnaliseBD clone : listaClones) {
			ServicoFrenteObraAnaliseBD original = mapaServicosFrenteObraAnaliseOriginal.get(Long.parseLong(clone.getVersaoId()));
			
			clonesDeServicoFrenteDeObraAnalise.add(new Clone<ServicoFrenteObraAnaliseBD>(original, clone));
		}
		return clonesDeServicoFrenteDeObraAnalise;
	}
	
	/**
	 * criar clone para Inclusao
	 * @param original ServicoFrenteObraAnaliseBD original
	 * @param servicoClonado ServicoBD clonado
	 * @param siglaEvento
	 * @return
	 */
	protected ServicoFrenteObraAnaliseBD criarCloneServicoFrenteObraAnaliseParaInclusao(
			ServicoFrenteObraAnaliseBD original, ServicoBD servicoClonado, String siglaEvento) {
		ServicoFrenteObraAnaliseBD clone = new ServicoFrenteObraAnaliseBD();
		
		clone.setServicoFk(servicoClonado.getId());
		clone.setQtItens(original.getQtItens());
		clone.setNmFrenteObra(original.getNmFrenteObra());
		clone.setNrFrenteObra(original.getNrFrenteObra());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setVersaoNr(original.getVersaoNr() + 1);		
		return clone;
	}
}
