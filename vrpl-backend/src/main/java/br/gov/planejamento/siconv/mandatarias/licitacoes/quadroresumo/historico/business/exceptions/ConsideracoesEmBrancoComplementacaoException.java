package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ConsideracoesEmBrancoComplementacaoException extends BusinessException {

	private static final long serialVersionUID = -5504306426492759163L;

	public ConsideracoesEmBrancoComplementacaoException() {
        super(ErrorInfo.CONSIDERACOES_OBRIGATORIO_COMPLEMENTACAO);
    }
}
