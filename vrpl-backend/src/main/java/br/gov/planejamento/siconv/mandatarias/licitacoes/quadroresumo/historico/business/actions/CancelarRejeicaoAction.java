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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao.CancelarRejeicaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao.UsuarioPodeCancelarRejeicaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.CANCELAR_REJEITE)
@QuadroResumoEventConfig(servicos = true, versionamentoComHistorico = true, email = true)
public class CancelarRejeicaoAction extends QuadroResumoBaseAction {

	@Inject
	private SiconvBC siconvBC;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Inject
	private EmailTemplate emailTemplate;
	
	@Inject
	private UsuarioPodeCancelarRejeicaoRules usuarioPodeCancelarRejeicaoRules;
	
	@Inject
	private CancelarRejeicaoRules cancelarRejeicaoRules;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao,
			HistoricoLicitacaoDTO historico) {
		usuarioPodeCancelarRejeicaoRules.setProposta(proposta);
		usuarioPodeCancelarRejeicaoRules.setLicitacao(licitacao);
		
		cancelarRejeicaoRules.setLicitacao(licitacao);
		cancelarRejeicaoRules.setProposta(proposta);
		return Arrays.asList(usuarioPodeCancelarRejeicaoRules, cancelarRejeicaoRules);
	}

	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao) {
		siconvRest.estornarProcessoExecucao(licitacao.getIdLicitacaoFk(), historico.getConsideracoes(), getUsuarioLogado());
	}
	
	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoCancelarRejeicaoParaProponente(licitacao));
		emailInfo.setConteudo(emailTemplate.getCancelarRejeicaoParaProponente(licitacao));
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}
}
