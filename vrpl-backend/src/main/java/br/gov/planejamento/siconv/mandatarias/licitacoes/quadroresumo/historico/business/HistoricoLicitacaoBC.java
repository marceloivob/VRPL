package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao.HistoricoLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

public class HistoricoLicitacaoBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private SiconvPrincipal usuarioLogado;

	public List<HistoricoLicitacaoDTO> findHistoricoLicitacaoByIdLicitacao(Long identificadorDaLicitacao) {
		List<HistoricoLicitacaoBD> listaHistoricoBD = dao.get(HistoricoLicitacaoDAO.class)
				.findHistoricoLicitacaoByIdLicitacao(identificadorDaLicitacao);

		List<HistoricoLicitacaoDTO> retorno = new ArrayList<>();

		for (HistoricoLicitacaoBD historicoBD : listaHistoricoBD) {
			retorno.add(historicoBD.converterParaDTO());
		}

		return retorno;
	}

	@RefreshRowPolice
	public void gerarHistorico(LicitacaoBD licitacaoEstadoAtual, HistoricoLicitacaoDTO novoHistorico) {

		dao.getJdbi().useTransaction(transaction -> {
			gerarHistoricoEmTransacao(licitacaoEstadoAtual, novoHistorico, transaction);
		});
	}

	public void gerarHistoricoEmTransacao(LicitacaoBD licitacaoEstadoAtual, HistoricoLicitacaoDTO novoHistorico,
			Handle transaction) {
		HistoricoLicitacaoBD novoHistoricoBD = new HistoricoLicitacaoBD();

		novoHistoricoBD.setEventoGerador(novoHistorico.getEventoGerador().getSigla());
		novoHistoricoBD.setConsideracoes(novoHistorico.getConsideracoes());
		novoHistoricoBD.setIdentificadorDaLicitacao(licitacaoEstadoAtual.getId());
		SituacaoLicitacaoEnum proximoEvento = novoHistorico.getEventoGerador().getProximaSituacaoNoCicloDeVida();

		novoHistoricoBD.setSituacaoDaLicitacao(proximoEvento.getSigla());
		novoHistoricoBD.setCpfDoResponsavel(usuarioLogado.getCpf());
		novoHistoricoBD.setNomeDoResponsavel(usuarioLogado.getName());

		licitacaoEstadoAtual.setSituacaoDaLicitacao(proximoEvento.getSigla());
		licitacaoEstadoAtual.setVersao(novoHistorico.getVersaoDaLicitacao());

		transaction.attach(LicitacaoDAO.class).updateSituacaoDaLicitacao(licitacaoEstadoAtual);
		transaction.attach(LicitacaoDAO.class).updateSituacaoSubmetasAssociadasDaLicitacao(novoHistoricoBD);
		transaction.attach(HistoricoLicitacaoDAO.class).insertHistoricoLicitacao(novoHistoricoBD);
	}

	
	/**
	 * para recuperar o historico no quadro resumo os dados sao ordenados em ordem decrescente, poranto a situacao 
	 * anterior sempre sera a posicao 1 e a atual a 0
	 * @param licitacaoEstadoAtual
	 * @return
	 */
	public SituacaoLicitacaoEnum getSituacaoAnteriorDocumentacaoOrdenacaoDescrescente(LicitacaoBD licitacaoEstadoAtual) {
		List<HistoricoLicitacaoDTO> listaHistorico = this
				.findHistoricoLicitacaoByIdLicitacao(licitacaoEstadoAtual.getId());

		if (listaHistorico.size() > 1) {
			
			SituacaoLicitacaoEnum resultado = listaHistorico.get(1).getSituacaoDaLicitacao();
			
			return resultado;
		}

		return SituacaoLicitacaoEnum.EM_PREENCHIMENTO;
	}

	@RefreshRowPolice
	public void salvarHistorico(HistoricoLicitacaoDTO novoHistorico) {
		HistoricoLicitacaoBD novoHistoricoBD = new HistoricoLicitacaoBD();

		novoHistoricoBD.setEventoGerador(novoHistorico.getEventoGerador().getSigla());
		novoHistoricoBD.setConsideracoes(novoHistorico.getConsideracoes());
		novoHistoricoBD.setIdentificadorDaLicitacao(novoHistorico.getIdentificadorDaLicitacao());

		LicitacaoBD licBD = dao.get(LicitacaoDAO.class).findLicitacaoById(novoHistorico.getIdentificadorDaLicitacao());

		novoHistoricoBD.setSituacaoDaLicitacao(licBD.getSituacaoDaLicitacao());
		novoHistoricoBD.setCpfDoResponsavel(usuarioLogado.getCpf());
		novoHistoricoBD.setNomeDoResponsavel(usuarioLogado.getName());

		dao.getJdbi().useTransaction(transaction -> {
			transaction.attach(HistoricoLicitacaoDAO.class).insertHistoricoLicitacao(novoHistoricoBD);
		});
	}

	public List<HistoricoLicitacaoBD> findHistoricoByIdLicitacaoEvento(Long idLicitacao, String evento) {
		return dao.get(HistoricoLicitacaoDAO.class).findHistoricoByIdLicitacaoEvento(idLicitacao, evento);
	}
	
	public boolean consultarSeHouveCancelamentos(Long idLicitacao, String eventoCancelamento) {
		return dao.get(HistoricoLicitacaoDAO.class).consultarSeHouveCancelamentos(idLicitacao, eventoCancelamento) > 0;
	}
	
	public List<HistoricoLicitacaoBD> findHistoricoByIdLicitacaoEventoConsiderandoCancelamento(
             Long idLicitacao, String eventoCancelamento, String evento){
		return dao.get(HistoricoLicitacaoDAO.class).findHistoricoByIdLicitacaoEventoConsiderandoCancelamento(idLicitacao, eventoCancelamento, evento);
	}
	
	public void setDao(DAOFactory daoFactory) {
		this.dao = daoFactory;
	}

	public void setUsuarioLogado(SiconvPrincipal usuarioLogado) {
		this.usuarioLogado = usuarioLogado;

	}
	
	public String quemEmitiuLaudo(Long idLicitacao, String tipoParecer) {
		
		Long qntCancelamentos = 0l;
		List<HistoricoLicitacaoBD> historicos = new ArrayList<HistoricoLicitacaoBD>();
		
		String eventoCancelar = EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_ENGENHARIA.getSigla();
		String evento = EventoQuadroResumoEnum.EMITIR_PARECER_ENGENHARIA.getSigla();
		
		if("VRPLS".equals(tipoParecer)) {
			eventoCancelar = EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_SOCIAL.getSigla();
			evento = EventoQuadroResumoEnum.EMITIR_PARECER_SOCIAL.getSigla();
		}
			
		qntCancelamentos = dao.get(HistoricoLicitacaoDAO.class).consultarSeHouveCancelamentos(idLicitacao, eventoCancelar);
		
		if(qntCancelamentos > 0) {
			historicos = dao.get(HistoricoLicitacaoDAO.class).findHistoricoByIdLicitacaoEventoConsiderandoCancelamento(
					idLicitacao, eventoCancelar, evento);
			
		} else {
			historicos = dao.get(HistoricoLicitacaoDAO.class).findHistoricoByIdLicitacaoEvento(
					idLicitacao, evento);	
		}
		
		if(!historicos.isEmpty()) {
			return historicos.get(0).getCpfDoResponsavel();
		}
		
		return null;
	}
	
	
	public void excluirHistoricoDaLicitacao(Long idLicitacao, Handle transaction) {
		List<HistoricoLicitacaoBD> historicos = transaction.attach(HistoricoLicitacaoDAO.class).findHistoricoLicitacaoByIdLicitacao(idLicitacao);
		for (HistoricoLicitacaoBD historicoLicitacaoBD : historicos) {
			transaction.attach(HistoricoLicitacaoDAO.class).deleteHistorico(
					historicoLicitacaoBD.getIdentificadorDaLicitacao(),
					historicoLicitacaoBD.getVersao());
		}
	}

}



















