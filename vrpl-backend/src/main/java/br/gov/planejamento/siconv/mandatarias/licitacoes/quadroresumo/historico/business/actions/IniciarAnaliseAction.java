package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoBaseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoEventType;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciaranalise.UsuarioPodeIniciarAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

@QuadroResumoEventType(EventoQuadroResumoEnum.INICIAR_ANALISE_PELA_MANDATARIA)
@QuadroResumoEventConfig(historico = true)
public class IniciarAnaliseAction extends QuadroResumoBaseAction {

	@Inject
	private UsuarioPodeIniciarAnaliseRules usuarioPodeIniciarAnaliseRules;
	
	@Override
	public List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		usuarioPodeIniciarAnaliseRules.setProposta(proposta);
		usuarioPodeIniciarAnaliseRules.setLicitacao(licitacao);
		return Arrays.asList(usuarioPodeIniciarAnaliseRules);
	}
}
