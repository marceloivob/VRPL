package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeEventoFrenteDeObraBatch;

public class ClonadorDeEventoFrenteDeObra extends TemplateClonadorDeEventoFrenteDeObraBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeEventoFrenteDeObra(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeEventoFrenteDeObra(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	void apagarClone(List<EventoBD> eventosClonadosGeradasPelaRejeicao,
						List<FrenteObraBD> frenteObrasClonadosGeradasPelaRejeicao) {

		if (!eventosClonadosGeradasPelaRejeicao.isEmpty() && !frenteObrasClonadosGeradasPelaRejeicao.isEmpty()) {

			List<Long> idEventosClonadosGeradosPelaRejeicaoApagar = eventosClonadosGeradasPelaRejeicao.stream()
					.map(EventoBD::getId).collect(Collectors.toList());

			List<Long> idFrenteObraClonadosGeradosPelaRejeicaoApagar = frenteObrasClonadosGeradasPelaRejeicao.stream()
					.map(FrenteObraBD::getId).collect(Collectors.toList());

			if (!idEventosClonadosGeradosPelaRejeicaoApagar.isEmpty() &&
					!idFrenteObraClonadosGeradosPelaRejeicaoApagar.isEmpty()) {

				liberarLoteDAO.apagarEventoFrenteObras (idEventosClonadosGeradosPelaRejeicaoApagar,idFrenteObraClonadosGeradosPelaRejeicaoApagar);
			}
		}
	}

	@Override
	protected List<EventoFrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsEventosOriginais) {
		return liberarLoteDAO.selectEventoFrenteDeObraParaClonar(idsEventosOriginais);
	}

	@Override
	protected EventoFrenteObraBD criarCloneParaInclusao(EventoFrenteObraBD original, EventoBD eventoPaiClonada,
			FrenteObraBD frenteObraPaiClonada) {

		EventoFrenteObraBD clone = new EventoFrenteObraBD();

		clone.setEventoFk(eventoPaiClonada.getId());
		clone.setFrenteObraFk(frenteObraPaiClonada.getId());
		clone.setNrMesConclusao(original.getNrMesConclusao());
		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getEventoFk() + "_" + original.getFrenteObraFk());
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setVersaoNr(original.getVersaoNr()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<EventoFrenteObraBD> listaParaInclusao) {
		liberarLoteDAO.cloneEventoFrenteObraBatch(listaParaInclusao);
	}

	@Override
	protected List<EventoFrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsEventosClonadas) {
		return liberarLoteDAO.selectEventoFrenteDeObraParaClonar(idsEventosClonadas);
	}

}
