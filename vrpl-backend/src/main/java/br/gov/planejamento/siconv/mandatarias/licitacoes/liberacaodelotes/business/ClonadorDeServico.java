package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeServicoBatch;

public class ClonadorDeServico extends TemplateClonadorDeServicoBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeServico(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeServico(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public List<ServicoBD> consultarClonesGerados(List<MacroServicoBD> macroServicosClonadosGeradosPelaRejeicaoApagar) {

		// Servicos que foram gerados pela Rejeição no método clone e serão apagados pelo método apagarClone
		return liberarLoteDAO.selectServicoParaApagar(macroServicosClonadosGeradosPelaRejeicaoApagar.stream().
														map(MacroServicoBD::getId).collect(Collectors.toList()));
	}

	public void apagarClone(List<ServicoBD> servicosGeradosRejeitarApagar) {

		if (!servicosGeradosRejeitarApagar.isEmpty()) {
			liberarLoteDAO.apagarServicos(servicosGeradosRejeitarApagar.stream().
								map(ServicoBD::getId).collect(Collectors.toList()));
		}
	}

	@Override
	protected List<ServicoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsMacroServicosOriginais) {
		return liberarLoteDAO.selectServicoParaClonar(idsMacroServicosOriginais);
	}

	@Override
	protected ServicoBD criarCloneParaInclusao(ServicoBD servicoOriginal, MacroServicoBD macroServicoClonado,
			EventoBD eventoClonado) {

		ServicoBD servicoClone = new ServicoBD();

		servicoClone.setMacroServicoFk(macroServicoClonado.getId());
		if (eventoClonado != null) {
			servicoClone.setEventoFk(eventoClonado.getId());
		}

		servicoClone.setTxObservacao(servicoOriginal.getTxObservacao());
		servicoClone.setNrServico(servicoOriginal.getNrServico());
		servicoClone.setCdServico(servicoOriginal.getCdServico());
		servicoClone.setTxDescricao(servicoOriginal.getTxDescricao());
		servicoClone.setSgUnidade(servicoOriginal.getSgUnidade());
		servicoClone.setVlCustoUnitarioRef(servicoOriginal.getVlCustoUnitarioRef());
		servicoClone.setPcBdi(servicoOriginal.getPcBdi());
		servicoClone.setQtTotalItensAnalise(servicoOriginal.getQtTotalItensAnalise());
		servicoClone.setVlCustoUnitario(servicoOriginal.getVlCustoUnitario());
		servicoClone.setVlCustoUnitarioDatabase(servicoOriginal.getVlCustoUnitarioDatabase());
		servicoClone.setVlPrecoUnitario(servicoOriginal.getVlPrecoUnitario());
		servicoClone.setVlPrecoTotal(servicoOriginal.getVlPrecoTotal());
		servicoClone.setPcBdiLicitado(servicoOriginal.getPcBdiLicitado());
		servicoClone.setVlPrecoUnitarioLicitado(servicoOriginal.getVlPrecoUnitario()); // O preco licitado eh "resetado" para o preco da analise
		servicoClone.setInFonte(servicoOriginal.getInFonte());
		servicoClone.setIdServicoAnalise(servicoOriginal.getIdServicoAnalise());
		servicoClone.setVersao(servicoOriginal.getVersao());
		servicoClone.setVersaoId(servicoOriginal.getId() + "");
		servicoClone.setVersaoNmEvento(this.siglaEvento);
		servicoClone.setVersaoNr(servicoOriginal.getVersaoNr()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return servicoClone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<ServicoBD> listaParaInclusao) {
		liberarLoteDAO.cloneServicoBatch(listaParaInclusao);
	}

	@Override
	protected List<ServicoBD> consultarClonesEntidadesDAO(List<Long> idsMacroServicosClonados) {
		// Eh a mesma consulta selectServicoParaClonar no DAO
		return liberarLoteDAO.selectServicoParaClonar(idsMacroServicosClonados);
	}

}
