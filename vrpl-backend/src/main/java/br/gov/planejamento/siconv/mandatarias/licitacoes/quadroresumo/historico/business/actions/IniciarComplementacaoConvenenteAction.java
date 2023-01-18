package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciarcomplementacaoconvenente.UsuarioPodeIniciarComplementacaoConvenenteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.INICIAR_COMPLEMENTACAO_CONVENENTE)
@QuadroResumoEventConfig(versionamentoComHistorico = true, servicos = true)
public class IniciarComplementacaoConvenenteAction extends QuadroResumoBaseAction {

	@Inject
	private UsuarioPodeIniciarComplementacaoConvenenteRules usuarioPodeIniciarComplementacaoConvenenteRules;
	
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
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao,
			HistoricoLicitacaoDTO historico) {
		usuarioPodeIniciarComplementacaoConvenenteRules.setProposta(proposta);
		usuarioPodeIniciarComplementacaoConvenenteRules.setLicitacao(licitacao);
		return Arrays.asList(usuarioPodeIniciarComplementacaoConvenenteRules);
	}

}
