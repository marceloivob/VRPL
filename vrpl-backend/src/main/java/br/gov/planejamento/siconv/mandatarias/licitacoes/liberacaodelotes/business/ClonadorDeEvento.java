package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates.TemplateClonadorDeEventoBatch;

public class ClonadorDeEvento extends TemplateClonadorDeEventoBatch {

	protected LiberarLoteDAO liberarLoteDAO;

	public ClonadorDeEvento(LiberarLoteDAO dao, EventoQuadroResumoEnum evento) {
		this.liberarLoteDAO = dao;
		this.siglaEvento = evento.getSigla();
	}

	public ClonadorDeEvento(LiberarLoteDAO dao) {
		this.liberarLoteDAO = dao;
	}

	public List<EventoBD> consultarClonesGerados(List<PoBD> poClonadasGeradasPelaRejeicaoApagar) {

		// Eventos que foram gerados pela Rejeição no método clone e serão apagados pelo método apagarClone
		return liberarLoteDAO.selectEventoApagar(poClonadasGeradasPelaRejeicaoApagar.stream().
																	map(PoBD::getId).collect(Collectors.toList()));
	}

	public void apagarClone(List<EventoBD> eventosGeradosRejeitarApagar) {

		if (!eventosGeradosRejeitarApagar.isEmpty()) {
			liberarLoteDAO.apagarEventos(eventosGeradosRejeitarApagar.stream().
								map(EventoBD::getId).collect(Collectors.toList()));
		}
	}

	@Override
	protected List<EventoBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais) {
		return liberarLoteDAO.selectEventoParaClonar(idsPosOriginais);
	}

	@Override
	protected EventoBD criarCloneParaInclusao(EventoBD original, PoBD poPaiClonada) {

		EventoBD clone = new EventoBD();

		clone.setIdPo(poPaiClonada.getId());
		clone.setIdAnalise(original.getIdAnalise());
		clone.setNomeEvento(original.getNomeEvento());
		clone.setNumeroEvento(original.getNumeroEvento());

		clone.setVersao(original.getVersao());
		clone.setVersaoId(original.getId() + "");
		clone.setVersaoNmEvento(this.siglaEvento);
		clone.setNumeroVersao(original.getNumeroVersao()); // Na liberacao do lote o numero de versao NAO eh incrementado

		return clone;
	}

	@Override
	protected void inserirClonesBatchDAO(List<EventoBD> listaParaInclusao) {
		liberarLoteDAO.cloneEventoBatch(listaParaInclusao);
	}

	@Override
	protected List<EventoBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas) {
		return liberarLoteDAO.selectEventoParaClonar(idsPosClonadas);
	}
}
