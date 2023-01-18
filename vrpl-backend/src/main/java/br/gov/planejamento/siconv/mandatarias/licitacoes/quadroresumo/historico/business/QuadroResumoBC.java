package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business.PropostaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.EventoQuadroResumoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitardocumentacao.UsuarioPodeAceitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelaraceite.UsuarioPodeCancelarAceiteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarcomplementacaoconvenente.UsuarioPodeCancelarComplementacaoConvenenteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenvioanalise.UsuarioPodeCancelarEnvioAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenviocomplementacao.UsuarioPodeCancelarEnvioComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao.UsuarioPodeCancelarRejeicaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.UsuarioPodeEnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciaranalise.UsuarioPodeIniciarAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciarcomplementacaoconvenente.UsuarioPodeIniciarComplementacaoConvenenteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.rejeitardocumentacao.UsuarioPodeRejeitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.solicitarcomplementacao.UsuarioPodeSolicitarComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao.HistoricoLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.QuadroResumoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuadroResumoBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter
	@Setter
	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private HistoricoLicitacaoBC historicoBC;

	@Inject
	private PropostaBC propostaBC;

	@Inject
	private UsuarioPodeCancelarEnvioAnaliseRules usuarioPodeCancelarEnvioAnaliseRules;
	
	@Inject
	private UsuarioPodeIniciarAnaliseRules usuarioPodeIniciarAnaliseRules;
	
	@Inject
	private UsuarioPodeCancelarRejeicaoRules usuarioPodeCancelarRejeicao;

	@Inject
	private UsuarioPodeSolicitarComplementacaoRules usuarioPodeSolicitarComplementacaoRules;
	
	@Inject
	private UsuarioPodeCancelarEnvioComplementacaoRules usuarioPodeCancelarEnvioComplementacaoRules;
	
	@Inject
	private UsuarioPodeCancelarComplementacaoConvenenteRules usuarioPodeCancelarComplementacaoConvenenteRules;
	
	@Inject
	private UsuarioPodeIniciarComplementacaoConvenenteRules usuarioPodeIniciarComplementacaoConvenenteRules;

	@Inject
	private UsuarioPodeAceitarDocumentacaoRules usuarioPodeAceitarDocumentacaoRules;
	
	@Inject
	private UsuarioPodeRejeitarDocumentacaoRules usuarioPodeRejeitarDocumentacaoRules;
	
	@Inject
	private UsuarioPodeCancelarAceiteRules usuarioPodeCancelarAceiteRules;
	
	@Inject 
	private UsuarioPodeEnviarParaAnaliseRules usuarioPodeEnviarParaAnaliseRules;
	
	@Inject
	private LaudoBC laudo;

	@Inject
	@Getter
	@Setter
	private QuadroResumoActionFactory factory;

	public QuadroResumoDTO recuperarQuadroResumo(Long identificadorDaLicitacao, Long versaoDaProposta) {
		Objects.requireNonNull(identificadorDaLicitacao);
		Objects.requireNonNull(versaoDaProposta);

		List<HistoricoLicitacaoDTO> historicoDaLicitacao = historicoBC
				.findHistoricoLicitacaoByIdLicitacao(identificadorDaLicitacao);

		LicitacaoBD licitacaoBD = getLicitacaoDAO().findLicitacaoById(identificadorDaLicitacao);

		if(historicoDaLicitacao.isEmpty()) {
			HistoricoLicitacaoBD historico = new HistoricoLicitacaoBD(licitacaoBD, usuarioLogado);
			historico.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());
			dao.get(HistoricoLicitacaoDAO.class).insertHistoricoLicitacao(historico);
			
			historicoDaLicitacao = historicoBC
					.findHistoricoLicitacaoByIdLicitacao(identificadorDaLicitacao);
		}
		
		for (HistoricoLicitacaoDTO historico : historicoDaLicitacao) {
			historico.setVersaoDaLicitacao(licitacaoBD.getVersao());
		}

		QuadroResumoDTO quadroResumo = new QuadroResumoDTO();
		SituacaoLicitacaoEnum situacaoAnterior = historicoBC
				.getSituacaoAnteriorDocumentacaoOrdenacaoDescrescente(licitacaoBD);

		quadroResumo.setListaHistorico(historicoDaLicitacao);

		PropostaBD proposta = propostaBC.recuperarProposta(usuarioLogado, versaoDaProposta);

		Boolean podeEnviarParaAnalise = usuarioPodeEnviarParaAnaliseRules.usuarioTemPermissaoParaExecutarAcao(proposta,
				licitacaoBD, usuarioLogado);
		Boolean podeCancelarEnvio = usuarioPodeCancelarEnvioAnaliseRules.usuarioTemPermissaoParaExecutarAcao(proposta,
				licitacaoBD, usuarioLogado, situacaoAnterior);
		
		Boolean podeIniciarAnalise = usuarioPodeIniciarAnaliseRules.usuarioTemPermissaoParaExecutarAcao(proposta,
				licitacaoBD, usuarioLogado);

		Boolean podeCancelarEnvioComplementacao = usuarioPodeCancelarEnvioComplementacaoRules
				.usuarioTemPermissaoParaExecutarAcao(proposta, licitacaoBD, usuarioLogado, situacaoAnterior);
		Boolean podeSolicitarComplementacao = usuarioPodeSolicitarComplementacaoRules
				.usuarioTemPermissaoParaExecutarAcao(proposta, licitacaoBD, usuarioLogado);
		
		Boolean podeCancelarSolicitacaoComplementacao = usuarioPodeCancelarComplementacaoConvenenteRules
				.usuarioTemPermissaoParaExecutarAcao(proposta, licitacaoBD, usuarioLogado);
				
		Boolean podeIniciarComplementacao = usuarioPodeIniciarComplementacaoConvenenteRules
				.usuarioTemPermissaoParaExecutarAcao(proposta, licitacaoBD, usuarioLogado);
		
		boolean existeParecerTecnicoEmitidoEViavel = this.laudo.existeParecerEmitidoViavelParaALicitacao(licitacaoBD);
		Boolean podeAceitarDocumentacao = usuarioPodeAceitarDocumentacaoRules.usuarioTemPermissaoParaExecutarAcao(
				proposta, licitacaoBD, existeParecerTecnicoEmitidoEViavel, usuarioLogado);
				
		Boolean podeRejeitarDocumentacao = usuarioPodeRejeitarDocumentacaoRules
				.usuarioTemPermissaoParaExecutarAcao(proposta, licitacaoBD, usuarioLogado);

		Boolean podeCancelarAceite = usuarioPodeCancelarAceiteRules.usuarioTemPermissaoParaExecutarAcao(proposta,
				licitacaoBD, usuarioLogado);
		
		Boolean podeCancelarRejeite = usuarioPodeCancelarRejeicao.usuarioTemPermissaoParaExecutarAcao(proposta,
				licitacaoBD, usuarioLogado);

		quadroResumo.setPodeEnviarParaAnalise(podeEnviarParaAnalise);
		quadroResumo.setPodeCancelarEnvio(podeCancelarEnvio);
		quadroResumo.setPodeCancelarEnvioComplementacao(podeCancelarEnvioComplementacao);
		quadroResumo.setPodeSolicitarComplementacao(podeSolicitarComplementacao);
		quadroResumo.setPodeIniciarAnalise(podeIniciarAnalise);
		quadroResumo.setPodeCancelarSolicitacaoComplementacao(podeCancelarSolicitacaoComplementacao);
		quadroResumo.setPodeIniciarComplementacao(podeIniciarComplementacao);
		quadroResumo.setPodeAceitarDocumentacao(podeAceitarDocumentacao);
		quadroResumo.setPodeRejeitarDocumentacao(podeRejeitarDocumentacao);
		quadroResumo.setPodeCancelarAceite(podeCancelarAceite);
		quadroResumo.setPodeCancelarRejeite(podeCancelarRejeite);

		return quadroResumo;
	}

	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE,
			Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE,
			Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA,
			Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE })
	public void processarNovoHistorico(HistoricoLicitacaoDTO historicoRecebido) {
		// Verifica as pré-condições
		if (historicoRecebido == null || !historicoRecebido.isValid()) {
			log.info("Não foi fornecido um Histórico válido para prosseguir: {}", historicoRecebido);

			throw new IllegalArgumentException("Não foi fornecido um Histórico válido para prosseguir.");
		}

		historicoRecebido.setDataDeRegistro(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		
		LicitacaoBD licitacaoEstadoAtual = getLicitacaoDAO()
				.findLicitacaoById(historicoRecebido.getIdentificadorDaLicitacao());
		
		PropostaBD propostaDaLicitacao = getPropostaDAO().loadById( licitacaoEstadoAtual.getIdentificadorDaProposta() );

		try {
			QuadroResumoAction action = factory.get(historicoRecebido.getEventoGerador());
			action.execute(historicoRecebido, propostaDaLicitacao, licitacaoEstadoAtual);

		} catch (EventoQuadroResumoInvalidoException ex) {
			//TODO Ações da aba Parecer (Emitir / Cancelar Emissão / Assinar) estão chamando este serviço do quadro resumo. 
			// No entanto, não existe ação para os eventos Emitir e Assinar neste método,
			// de maneira que a factory não consegue instanciar uma classe correspondente.
			// Por esse motivo a exceção foi abafada. 
			// Reavaliar a necessidade de chamar o serviço na emissão e assinatura de parecer.
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Gets/Sets
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected LicitacaoDAO getLicitacaoDAO() {
		return dao.get(LicitacaoDAO.class);
	}

	protected PropostaDAO getPropostaDAO() {
		return dao.get(PropostaDAO.class);
	}

	public void setLaudo(LaudoBC laudo) {
		this.laudo = laudo;
	}
}
