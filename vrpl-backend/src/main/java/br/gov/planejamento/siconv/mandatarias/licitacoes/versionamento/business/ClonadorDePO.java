package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDePO {

	private VersionamentoDAO dao;
	private String siglaEvento;
	
	/**
	 * Construtor padrao
	 * @param dao
	 * @param evento
	 */
	public ClonadorDePO(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	/**
	 * retorna lista de clones
	 * @param listaClonesDeSubmeta
	 * @return
	 */
	public List<Clone<PoBD>> clone(List<Clone<SubmetaBD>> listaClonesDeSubmeta) {
		//criar mapa chave Submeta original -> clone Submeta
		Map<Long, SubmetaBD> mapaChaveSubmetaOriginalClone = new TreeMap<>();
		for (Clone<SubmetaBD> clone : listaClonesDeSubmeta) {
			mapaChaveSubmetaOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		List<PoBD> poOriginais = mapaChaveSubmetaOriginalClone.isEmpty() ? new ArrayList<>()
				: dao.selectPOParaClonar(new ArrayList<>(mapaChaveSubmetaOriginalClone.keySet()));
		
		Map<Long, PoBD> mapaChavePoOriginal = new TreeMap<>();
		List<PoBD> listaParaInclusao = new ArrayList<>();
		
		//criar clones
		for (PoBD poOriginal : poOriginais) {
			
			mapaChavePoOriginal.put(poOriginal.getId(), poOriginal);

			SubmetaBD SubmetaPaiClonada = mapaChaveSubmetaOriginalClone.get(poOriginal.getSubmetaId());
			if (SubmetaPaiClonada != null) {

				PoBD clone = criarClonesParaInclusao(poOriginal, SubmetaPaiClonada, siglaEvento);
				listaParaInclusao.add(clone);
				
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma SubmetaBD clonada para a PoBD " + poOriginal);
			}
		}
		
		//inserir
		if (!listaParaInclusao.isEmpty()) {
			dao.clonePOBatch(listaParaInclusao);
		}
		
		//consultar
		List<Long> idsSubmetasClonadas = listaClonesDeSubmeta.stream().map(Clone::getObjetoClonado).map(SubmetaBD::getId)
		.collect(Collectors.toList());
		
		List<PoBD> poClonados = idsSubmetasClonadas.isEmpty() ? new ArrayList<>() : dao.selectPOParaClonar(idsSubmetasClonadas);
		
		List<Clone<PoBD>> clonesDePo = new ArrayList<>();
		//criar saida
		for (PoBD clone : poClonados) {
			PoBD poOriginal = mapaChavePoOriginal.get(Long.parseLong(clone.getVersaoId()));
			clonesDePo.add(new Clone<PoBD>(poOriginal, clone));
		}	

		return clonesDePo;
	}
	
	
	/**
	 * Cria clones
	 * @param original
	 * @param submetaPaiClonada
	 * @param siglaEvento
	 * @return
	 */
	protected PoBD criarClonesParaInclusao(PoBD original, 
			SubmetaBD submetaPaiClonada, String siglaEvento) {
		
		PoBD clone = new PoBD();
		
		clone.setSubmetaId(submetaPaiClonada.getId());
		clone.setDtBaseAnalise(original.getDtBaseAnalise());
		clone.setDtBaseVrpl(original.getDtBaseVrpl());
		clone.setDtPrevisaoInicioObra(original.getDtPrevisaoInicioObra());
		clone.setDtPrevisaoInicioObraAnalise(original.getDtPrevisaoInicioObraAnalise());
		clone.setIdPoAnalise(original.getIdPoAnalise());
		clone.setInAcompanhamentoEventos(original.getInAcompanhamentoEventos());
		clone.setInDesonerado(original.getInDesonerado());
		clone.setQtMesesDuracaoObra(original.getQtMesesDuracaoObra());
		clone.setReferencia(original.getReferencia());
		clone.setSgLocalidade(original.getSgLocalidade());
		
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(siglaEvento);
		clone.setVersaoNr(original.getVersaoNr() + 1);
		
											
		return clone;
		
	}
}
