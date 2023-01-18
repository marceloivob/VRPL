package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeServicoFrenteDeObraBatch;

public class ClonadorDeServicoFrenteDeObra extends TemplateClonadorDeServicoFrenteDeObraBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeServicoFrenteDeObra(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeServicoFrenteDeObra(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public void apagarClone(List<ServicoBD> servicosClonadosGeradosPelaRejeicaoApagar, List<FrenteObraBD> frenteObrasClonadasGeradasPelaRejeicaoApagar) {

		if (!servicosClonadosGeradosPelaRejeicaoApagar.isEmpty() && !frenteObrasClonadasGeradasPelaRejeicaoApagar.isEmpty()) {
			List<Long> idServicosClonadosGeradosPelaRejeicaoApagar = servicosClonadosGeradosPelaRejeicaoApagar.stream()
					.map(ServicoBD::getId).collect(Collectors.toList());

			List<Long> idFrenteObrasClonadosGeradosPelaRejeicaoApagar = frenteObrasClonadasGeradasPelaRejeicaoApagar.stream()
					.map(FrenteObraBD::getId).collect(Collectors.toList());

			if (!idServicosClonadosGeradosPelaRejeicaoApagar.isEmpty() && !idFrenteObrasClonadosGeradosPelaRejeicaoApagar.isEmpty()) {
				liberarLoteDAO.apagarServicoFrenteObras(idServicosClonadosGeradosPelaRejeicaoApagar,idFrenteObrasClonadosGeradosPelaRejeicaoApagar);
			}
		}
	}

	@Override
	protected List<ServicoFrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(
			List<Long> idsServicosOriginais) {

		return liberarLoteDAO.selectServicoFrenteDeObraParaClonar(idsServicosOriginais);
	}

	@Override
	protected ServicoFrenteObraBD criarCloneParaInclusao(ServicoFrenteObraBD servicoFrenteObraOriginal,
			FrenteObraBD frenteObraClonada, ServicoBD servicoClonado) {

		ServicoFrenteObraBD servicoFrenteObraClone = new ServicoFrenteObraBD();

		servicoFrenteObraClone.setServicoFk(servicoClonado.getId());
		servicoFrenteObraClone.setFrenteObraFk(frenteObraClonada.getId());
		servicoFrenteObraClone.setQtItens(servicoFrenteObraOriginal.getQtItens());
		servicoFrenteObraClone.setVersao(servicoFrenteObraOriginal.getVersao());
		servicoFrenteObraClone.setVersaoId(servicoFrenteObraOriginal.getServicoFk() + "_" + servicoFrenteObraOriginal.getFrenteObraFk());
		servicoFrenteObraClone.setVersaoNmEvento(this.siglaEvento);
		servicoFrenteObraClone.setVersaoNr(servicoFrenteObraOriginal.getVersaoNr()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return servicoFrenteObraClone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<ServicoFrenteObraBD> listaParaInclusao) {
		liberarLoteDAO.cloneServicoFrenteObraBatch(listaParaInclusao);
	}

	@Override
	protected List<ServicoFrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsServicosClonados) {
		// Eh a mesma consulta selectServicoParaClonar no DAO
		return liberarLoteDAO.selectServicoFrenteDeObraParaClonar(idsServicosClonados);
	}
}
