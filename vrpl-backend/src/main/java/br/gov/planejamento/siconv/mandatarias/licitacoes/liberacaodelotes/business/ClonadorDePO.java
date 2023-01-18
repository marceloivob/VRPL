package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public class ClonadorDePO {

	private LiberarLoteDAO dao;
	private String siglaEvento;

	public ClonadorDePO(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDePO(LiberarLoteDAO dao) {
		this.dao = dao;
	}

	public List<Clone<PoBD>> clone(List<Clone<SubmetaBD>> clonesDeSubmeta) {
		
		List<Long> idsSubmetasOriginais = clonesDeSubmeta.stream().map(Clone::getObjetoOriginal)
				.map(SubmetaBD::getId).collect(Collectors.toList());
		
		List<Clone<PoBD>> clonesDePO = new ArrayList<>();

		List<PoBD> poOriginais = dao.selectPOParaClonar(idsSubmetasOriginais);

		for (PoBD poOriginal : poOriginais) {

			Optional<Clone<SubmetaBD>> poClonados = clonesDeSubmeta.stream().filter(
					submetaClonada -> submetaClonada.getObjetoOriginal().getId().equals(poOriginal.getSubmetaId()))
					.findFirst();

			if (poClonados.isPresent()) {
				SubmetaBD submetaClonada = poClonados.get().getObjetoClonado();

				PoBD clone = dao.clonePO(poOriginal, submetaClonada, siglaEvento);

				clonesDePO.add(new Clone<PoBD>(poOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma SubmetaBD clonada para o PoBD " + poOriginal);
			}
		}

		return clonesDePO;
	}
	
	
	public List<PoBD> consultarClonesGerados(List<SubmetaBD> submetasClonadasGeradasPelaRejeicaoApagar) {

		// PO foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone 
		return dao.selectPoParaApagar(submetasClonadasGeradasPelaRejeicaoApagar.stream().
												map((SubmetaBD submeta) -> submeta.getId()).collect(Collectors.toList()));
		
	}	

	
	public void apagarClone(List<PoBD> poGeradasRejeitarApagar) {

		if (!poGeradasRejeitarApagar.isEmpty()) {
			dao.apagarPOs(poGeradasRejeitarApagar.stream().
								map((PoBD po) -> po.getId()).collect(Collectors.toList()));
		}		
	}	

	
}
