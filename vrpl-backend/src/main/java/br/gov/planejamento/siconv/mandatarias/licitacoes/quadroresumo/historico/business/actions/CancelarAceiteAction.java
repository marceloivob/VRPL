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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelaraceite.CancelarAceiteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelaraceite.UsuarioPodeCancelarAceiteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.CANCELAR_ACEITE)
@QuadroResumoEventConfig(servicos = true, historico = true, versionamentoSemHistorico = true, email = true)
public class CancelarAceiteAction extends QuadroResumoBaseAction {

	@Inject
	private SiconvBC siconvBC;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Inject
	private EmailTemplate emailTemplate;
	
	@Inject
	private UsuarioPodeCancelarAceiteRules usuarioPodeCancelarAceiteRules;
	
	@Inject
	private CancelarAceiteRules cancelarAceiteRules;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao,
			HistoricoLicitacaoDTO historico) {
		usuarioPodeCancelarAceiteRules.setProposta(proposta);
		usuarioPodeCancelarAceiteRules.setLicitacao(licitacao);
		
		cancelarAceiteRules.setLicitacao(licitacao);
		cancelarAceiteRules.setProposta(proposta);
		return Arrays.asList(usuarioPodeCancelarAceiteRules, cancelarAceiteRules);
	}
	
	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao) {
		siconvRest.estornarProcessoExecucao(licitacao.getIdLicitacaoFk(),
				historico.getConsideracoes(), getUsuarioLogado());
	}

	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoCancelarAceiteParaProponente(licitacao));
		emailInfo.setConteudo(emailTemplate.getCancelarAceiteParaProponente(licitacao));
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}
}