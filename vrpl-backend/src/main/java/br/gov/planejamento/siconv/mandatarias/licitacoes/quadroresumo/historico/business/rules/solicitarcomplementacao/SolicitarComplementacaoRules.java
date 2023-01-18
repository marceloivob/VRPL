package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.solicitarcomplementacao;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ConsideracoesEmBrancoComplementacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.PropostaComParecerEmitidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class SolicitarComplementacaoRules implements QuadroResumoValidator {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private HistoricoLicitacaoDTO historico;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private boolean existeParecerEmitido;
	
	@Inject
	private BusinessExceptionContext businessExceptionContext;
	
	@Override
	public void validate() {
		this.verificarPreenchimentoConsideracoes(historico);
		this.verificarExistenciaParecerEmitidoParaALicitacao(licitacao, existeParecerEmitido);
	}
	
	public void verificarPreenchimentoConsideracoes(HistoricoLicitacaoDTO historico) {
		if (historico.getConsideracoes() == null || "".equals(historico.getConsideracoes())) {
			businessExceptionContext.add(new ConsideracoesEmBrancoComplementacaoException());
		}
	}
	
	public void verificarExistenciaParecerEmitidoParaALicitacao(LicitacaoBD licitacao, boolean existeParecerEmitido) {
		if (existeParecerEmitido) {
			businessExceptionContext.add(new PropostaComParecerEmitidoException());
		}
	}
}
