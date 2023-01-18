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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.EmailInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.HistoricoLicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenviocomplementacao.UsuarioPodeCancelarEnvioComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(value = EventoQuadroResumoEnum.CANCELAR_ENVIO_ANALISE_COMPLEMENTACAO)
@QuadroResumoEventConfig(historico = true, email = true, servicos = true)
public class CancelarEnvioAnaliseComplementacaoAction extends QuadroResumoBaseAction {

	@Inject
	private SiconvBC siconvBC;
	
	@Inject
	private HistoricoLicitacaoBC historicoBC;
	
	@Inject
	private UsuarioPodeCancelarEnvioComplementacaoRules usuarioPodeCancelarEnvioComplementacaoRules;
	
	@Inject
	private EmailTemplate emailTemplate;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico, @NotNull PropostaBD proposta,
			@NotNull LicitacaoBD licitacao) {

		String justificativa = historico.getConsideracoes();
		
		siconvRest.estornarEnvioParaAceite(licitacao.getIdLicitacaoFk(), justificativa, getUsuarioLogado());
	}
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		usuarioPodeCancelarEnvioComplementacaoRules.setProposta(proposta);
		usuarioPodeCancelarEnvioComplementacaoRules.setLicitacao(licitacao);
		usuarioPodeCancelarEnvioComplementacaoRules
				.setSituacaoAnterior(historicoBC.getSituacaoAnteriorDocumentacaoOrdenacaoDescrescente(licitacao));
		return Arrays.asList(usuarioPodeCancelarEnvioComplementacaoRules);
	}	
	
	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoCancelarAnaliseDaComplementacaoProponente(licitacao));
		emailInfo.setConteudo(emailTemplate.getCancelarAnaliseDaComplementacaoProponente(licitacao));
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}
}
