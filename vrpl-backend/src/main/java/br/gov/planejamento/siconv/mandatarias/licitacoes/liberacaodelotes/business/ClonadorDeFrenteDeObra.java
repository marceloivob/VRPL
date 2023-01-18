package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;

import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeFrenteDeObraBatch;

public class ClonadorDeFrenteDeObra extends TemplateClonadorDeFrenteDeObraBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeFrenteDeObra(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeFrenteDeObra(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public List<FrenteObraBD> consultarClonesGerados(List<PoBD> poClonadasGeradasPelaRejeicaoApagar) {

		// Frente de Obra que foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone
		return liberarLoteDAO.selectFrenteDeObraParaApagar(poClonadasGeradasPelaRejeicaoApagar.stream().
													map(PoBD::getId).collect(Collectors.toList()));
	}


	public void apagarClone(List<FrenteObraBD> frenteObraGeradasRejeitarApagar) {

		if (!frenteObraGeradasRejeitarApagar.isEmpty()) {
			liberarLoteDAO.apagarFrenteObras(frenteObraGeradasRejeitarApagar.stream().
								map(FrenteObraBD::getId).collect(Collectors.toList()));
		}
	}

	@Override
	protected FrenteObraBD criarCloneParaInclusao(FrenteObraBD frenteDeObraOriginal, PoBD poPaiClonada) {

		FrenteObraBD clone = new FrenteObraBD();

		clone.setPoFk(poPaiClonada.getId());
		clone.setNmFrenteObra(frenteDeObraOriginal.getNmFrenteObra());
		clone.setNrFrenteObra(frenteDeObraOriginal.getNrFrenteObra());

		clone.setVersao(frenteDeObraOriginal.getVersao());
		clone.setVersaoId(frenteDeObraOriginal.getId() + "");
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setNumeroVersao(frenteDeObraOriginal.getNumeroVersao()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected List<FrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais) {
		return liberarLoteDAO.selectFrenteDeObraParaClonar(idsPosOriginais);
	}

	@Override
	protected void inserirClonesBatchDAO(List<FrenteObraBD> listaParaInclusao) {
		liberarLoteDAO.cloneFrenteDeObraBatch(listaParaInclusao);
	}

	@Override
	protected List<FrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas) {
		// Eh a mesma consulta selectFrenteDeObraParaClonar no DAO
		return liberarLoteDAO.selectFrenteDeObraParaClonar(idsPosClonadas);
	}
}
