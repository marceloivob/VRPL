package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.PermissaoUsuario;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvGRPCConsumer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.EnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.UsuarioPodeEnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.ENVIAR_PARA_ANALISE)
@QuadroResumoEventConfig(historico = true, servicos = true)
public class EnviarParaAnaliseAction extends QuadroResumoBaseAction {

	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;

	@Inject
	private EnviarParaAnaliseRules validator;

	@Inject UsuarioPodeEnviarParaAnaliseRules usuarioPodeEnviarParaAnaliseRules;

	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico, @NotNull PropostaBD proposta,
			@NotNull LicitacaoBD licitacao) {

		String justificativa = historico.getConsideracoes();

		siconvRest.enviarProcessoExecucaoParaAceite(licitacao.getIdLicitacaoFk(), justificativa, getUsuarioLogado());
	}

	@PermissaoUsuario(value = Profile.PROPONENTE)
	@Override
	public List<QuadroResumoValidator> getValidators(PropostaBD proposta, LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		usuarioPodeEnviarParaAnaliseRules.setProposta(proposta);
		usuarioPodeEnviarParaAnaliseRules.setLicitacao(licitacao);
		validator.setLicitacao(licitacao);
		return Arrays.asList(usuarioPodeEnviarParaAnaliseRules, validator);
	}
}
