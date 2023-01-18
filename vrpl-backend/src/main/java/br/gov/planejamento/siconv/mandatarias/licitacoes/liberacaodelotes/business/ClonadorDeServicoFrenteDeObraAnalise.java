package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeServicoFrenteDeObraAnaliseBatch;

public class ClonadorDeServicoFrenteDeObraAnalise extends TemplateClonadorDeServicoFrenteDeObraAnaliseBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeServicoFrenteDeObraAnalise(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeServicoFrenteDeObraAnalise(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public void apagarClone(List<ServicoBD> servicosClonadosGeradosPelaRejeicaoApagar) {

		if (!servicosClonadosGeradosPelaRejeicaoApagar.isEmpty()) {
			List<Long> idServicosClonadosGeradosPelaRejeicaoApagar = servicosClonadosGeradosPelaRejeicaoApagar.stream()
					.map(ServicoBD::getId).collect(Collectors.toList());

			if (!idServicosClonadosGeradosPelaRejeicaoApagar.isEmpty() ) {
				liberarLoteDAO.apagarServicosFrenteObraAnalise(idServicosClonadosGeradosPelaRejeicaoApagar);
			}
		}
	}

	@Override
	protected ServicoFrenteObraAnaliseBD criarCloneParaInclusao(
			ServicoFrenteObraAnaliseBD original, ServicoBD servicoClonado) {

		ServicoFrenteObraAnaliseBD clone = new ServicoFrenteObraAnaliseBD();

		clone.setServicoFk(servicoClonado.getId());
		clone.setQtItens(original.getQtItens());
		clone.setNmFrenteObra(original.getNmFrenteObra());
		clone.setNrFrenteObra(original.getNrFrenteObra());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setVersaoNr(original.getVersaoNr()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected List<ServicoFrenteObraAnaliseBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(
			List<Long> idsServicosOriginais) {

		return liberarLoteDAO
				.selectServicoFrenteDeObraAnaliseParaClonar(idsServicosOriginais);
	}

	@Override
	protected void inserirClonesBatchDAO(List<ServicoFrenteObraAnaliseBD> listaParaInclusao) {
		liberarLoteDAO.cloneServicoFrenteObraAnaliseBatch(listaParaInclusao);
	}

	@Override
	protected List<ServicoFrenteObraAnaliseBD> consultarClonesEntidadesDAO(List<Long> idsServicosClonados) {
		return liberarLoteDAO
				.selectServicoFrenteDeObraAnaliseParaClonar(idsServicosClonados);
	}

}
