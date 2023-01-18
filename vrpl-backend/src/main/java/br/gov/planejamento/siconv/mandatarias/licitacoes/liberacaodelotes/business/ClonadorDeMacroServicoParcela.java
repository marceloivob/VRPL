package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeMacroServicoParcelaBatch;

public class ClonadorDeMacroServicoParcela extends TemplateClonadorDeMacroServicoParcelaBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeMacroServicoParcela(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeMacroServicoParcela(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public void apagarClone(List<MacroServicoBD> macroServicosClonadosGeradosPelaRejeicao) {

		if (!macroServicosClonadosGeradosPelaRejeicao.isEmpty()) {
			liberarLoteDAO.apagarMacroServicoParcelas(macroServicosClonadosGeradosPelaRejeicao.stream().
											map(MacroServicoBD::getId).collect(Collectors.toList()));
		}
	}

	@Override
	protected List<MacroServicoParcelaBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(
			List<Long> idsMacroServicosOriginais) {

		return liberarLoteDAO
				.selectMacroServicoParcelaParaClonar(idsMacroServicosOriginais);
	}

	@Override
	protected MacroServicoParcelaBD criarCloneParaInclusao(MacroServicoParcelaBD original,
			MacroServicoBD macroServicoClonado) {

		MacroServicoParcelaBD clone = new MacroServicoParcelaBD();

		clone.setNrParcela(original.getNrParcela());
		clone.setPcParcela(original.getPcParcela());
		clone.setMacroServicoFk(macroServicoClonado.getId());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setNumeroVersao(original.getNumeroVersao()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<MacroServicoParcelaBD> listaParaInclusao) {
		liberarLoteDAO.cloneMacroServicoParcelaBatch(listaParaInclusao);
	}

	@Override
	protected List<MacroServicoParcelaBD> consultarClonesEntidadesDAO(List<Long> idsMacroServicosClonados) {
		return liberarLoteDAO
				.selectMacroServicoParcelaParaClonar(idsMacroServicosClonados);
	}

}
