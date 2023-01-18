package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeMacroServicoBatch;

public class ClonadorDeMacroServico extends TemplateClonadorDeMacroServicoBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeMacroServico(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeMacroServico(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public List<MacroServicoBD> consultarClonesGerados(List<PoBD> poClonadasGeradasPelaRejeicaoApagar) {

		// Macro Servico que foram geradas pela Rejeição no método clone e serão apagadas pelo método apagarClone
		return liberarLoteDAO.selectMacroServicoParaApagar(poClonadasGeradasPelaRejeicaoApagar.stream().
													map(PoBD::getId).collect(Collectors.toList()));
	}


	public void apagarClone(List<MacroServicoBD> macroServicosGeradosRejeitarApagar) {

		if (!macroServicosGeradosRejeitarApagar.isEmpty()) {
			liberarLoteDAO.apagarMacroServicos(macroServicosGeradosRejeitarApagar.stream().
												map(MacroServicoBD::getId).collect(Collectors.toList()));
		}
	}

	@Override
	protected List<MacroServicoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais) {
		return liberarLoteDAO.selectMacroServicoParaClonar(idsPosOriginais);
	}

	@Override
	protected MacroServicoBD criarCloneParaInclusao(MacroServicoBD original, PoBD poPaiClonada) {
		MacroServicoBD clone = new MacroServicoBD();

		clone.setTxDescricao(original.getTxDescricao());
		clone.setPoFk(poPaiClonada.getId());
		clone.setNrMacroServico(original.getNrMacroServico());
		clone.setIdMacroServicoAnalise(original.getIdMacroServicoAnalise());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setVersaoNr(original.getVersaoNr()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<MacroServicoBD> listaParaInclusao) {
		liberarLoteDAO.cloneMacroServicoBatch(listaParaInclusao);
	}

	@Override
	protected List<MacroServicoBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas) {
		return liberarLoteDAO.selectMacroServicoParaClonar(idsPosClonadas);
	}

}
