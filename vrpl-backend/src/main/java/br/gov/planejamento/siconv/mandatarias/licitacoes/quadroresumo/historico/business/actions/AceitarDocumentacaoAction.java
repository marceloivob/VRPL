package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.VincularCPS;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitardocumentacao.AceitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitardocumentacao.UsuarioPodeAceitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import lombok.Setter;

@QuadroResumoEventType(EventoQuadroResumoEnum.ACEITAR_DOCUMENTACAO)
@QuadroResumoEventConfig(historico = true, servicos = true, email = true)
public class AceitarDocumentacaoAction extends QuadroResumoBaseAction {

	@Inject
	@Setter
	private SiconvBC siconvBC;
	
	@Inject
	private LaudoBC laudoBC;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Inject
	private VincularCPS cps;
	
	@Inject
	@Setter
	private EmailTemplate emailTemplate;

	@Inject
	private AceitarDocumentacaoRules validator;
	
	@Inject
	private UsuarioPodeAceitarDocumentacaoRules usuarioPodeAceitarDocumentacaoRules;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao,
			HistoricoLicitacaoDTO historico) {
		usuarioPodeAceitarDocumentacaoRules.setProposta(proposta);
		usuarioPodeAceitarDocumentacaoRules.setLicitacao(licitacao);
		usuarioPodeAceitarDocumentacaoRules
				.setExisteParecerTecnicoEmitidoEViavel(laudoBC.existeParecerEmitidoViavelParaALicitacao(licitacao));
		validator.setLicitacao(licitacao);
		return Arrays.asList(usuarioPodeAceitarDocumentacaoRules, validator);
	}

	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao) {
		
		//Chamada ao GRPC do CPS
		cps.vincular(getUsuarioLogado(), proposta, licitacao);
		
		//Chamada ao REST do Siconvao
		siconvRest.aceitarRejeitarProcessoExecucao(licitacao.getIdLicitacaoFk(),
				EventoAceiteRejeicao.ACEITAR,
				historico.getPapelDoResponsavel(), historico.getDataDeRegistro(),
				historico.getConsideracoes(), getUsuarioLogado());
	}
	
	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setAssunto(emailTemplate.getAssuntoAceitarDocumentacaoParaProponente(licitacao));
		emailInfo.setConteudo(emailTemplate.getAceitarDocumentacaoParaProponente(licitacao));
		emailInfo.setDestinatarios(siconvBC.buscarEmails(proposta, Profile.PROPONENTE));
		emailInfo.setProfile(Profile.PROPONENTE);
		emailInfo.setProposta(proposta);
		return emailInfo;
	}
}
