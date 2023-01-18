package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.EmailTemplate;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.EmailInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.solicitarcomplementacao.SolicitarComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.solicitarcomplementacao.UsuarioPodeSolicitarComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(value = EventoQuadroResumoEnum.SOLICITAR_COMPLEMENTACAO_CONVENENTE)
@QuadroResumoEventConfig(historico = true, email = true)
public class SolicitarComplementacaoConvenenteAction extends QuadroResumoBaseAction {

	@Inject
	private SiconvBC siconvBC;
	
	@Inject
	private LaudoBC laudoBC;
	
	@Inject
	private UsuarioPodeSolicitarComplementacaoRules usuarioPodeSolicitarComplementacaoRules;
	
	@Inject 
	private SolicitarComplementacaoRules solicitarComplementacaoRules;
	
	@Inject
	private EmailTemplate emailTemplate;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		usuarioPodeSolicitarComplementacaoRules.setProposta(proposta);
		usuarioPodeSolicitarComplementacaoRules.setLicitacao(licitacao);
		
		solicitarComplementacaoRules.setHistorico(historico);
		solicitarComplementacaoRules.setLicitacao(licitacao);
		solicitarComplementacaoRules.setExisteParecerEmitido(this.laudoBC.existeParecerEmitidoParaALicitacao(licitacao));
		return Arrays.asList(usuarioPodeSolicitarComplementacaoRules, solicitarComplementacaoRules);
	}

	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoSolicitacaoComplementacaoParaProponente(getUsuarioLogado(), licitacao));
		emailInfo.setConteudo(emailTemplate.getSolicitacaoComplementacaoParaProponente());
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}	
}
