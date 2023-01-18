package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.PermissaoUsuario;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.HistoricoLicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenvioanalise.UsuarioPodeCancelarEnvioAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.CANCELAR_ENVIO_ANALISE)
@QuadroResumoEventConfig(historico = true, servicos = true)
public class CancelarEnvioAnaliseAction extends QuadroResumoBaseAction {

	@Inject
	private HistoricoLicitacaoBC historicoBC;
	
	@Inject
	private UsuarioPodeCancelarEnvioAnaliseRules usuarioPodeCancelarEnvioAnaliseRules;
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico, @NotNull PropostaBD proposta,
			@NotNull LicitacaoBD licitacao) {

		String justificativa = historico.getConsideracoes();
		
		siconvRest.estornarEnvioParaAceite(licitacao.getIdLicitacaoFk(), justificativa, getUsuarioLogado());
	}
	
	@PermissaoUsuario(value = Profile.PROPONENTE)
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		usuarioPodeCancelarEnvioAnaliseRules.setProposta(proposta);
		usuarioPodeCancelarEnvioAnaliseRules.setLicitacao(licitacao);
		usuarioPodeCancelarEnvioAnaliseRules
				.setSituacaoAnterior(historicoBC.getSituacaoAnteriorDocumentacaoOrdenacaoDescrescente(licitacao));
		return Arrays.asList(usuarioPodeCancelarEnvioAnaliseRules);
	}
}
