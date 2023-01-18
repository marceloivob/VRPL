package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.EmailTemplate;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.EmailInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.rejeitardocumentacao.RejeitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.rejeitardocumentacao.UsuarioPodeRejeitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.REJEITAR)
@QuadroResumoEventConfig(versionamentoComHistorico = true, servicos = true, email = true)
public class RejeitarDocumentacaoAction extends QuadroResumoBaseAction {

	@Inject
	private SiconvBC siconvBC;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Inject
	private EmailTemplate emailTemplate;
	
	@Inject
	private UsuarioPodeRejeitarDocumentacaoRules usuarioPodeRejeitarDocumentacaoRules;
	
	@Inject 
	private RejeitarDocumentacaoRules rejeitarDocumentacaoRules;
	
	@Inject
	private LaudoBC laudoBC;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao,
			HistoricoLicitacaoDTO historico) {
		
		usuarioPodeRejeitarDocumentacaoRules.setProposta(proposta);
		usuarioPodeRejeitarDocumentacaoRules.setLicitacao(licitacao);
		
		rejeitarDocumentacaoRules.setLicitacao(licitacao);
		rejeitarDocumentacaoRules.setHistorico(historico);
		rejeitarDocumentacaoRules.setExisteParecerViavelEmitido(this.laudoBC.existeParecerEmitidoViavelParaALicitacao(licitacao));
		
		return Arrays.asList(usuarioPodeRejeitarDocumentacaoRules, rejeitarDocumentacaoRules);
	}

	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao) {
		// Chamada ao REST do Siconvao
		siconvRest.aceitarRejeitarProcessoExecucao(licitacao.getIdLicitacaoFk(), EventoAceiteRejeicao.REJEITAR,
				historico.getPapelDoResponsavel(), historico.getDataDeRegistro(), historico.getConsideracoes(),
				getUsuarioLogado());
	}
	
	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoRejeitarParaProponente(licitacao));
		emailInfo.setConteudo(emailTemplate.getRejeitarParaProponente(licitacao));
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}
}
