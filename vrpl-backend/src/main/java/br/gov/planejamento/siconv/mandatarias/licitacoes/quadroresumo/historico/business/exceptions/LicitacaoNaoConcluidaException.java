package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class LicitacaoNaoConcluidaException extends BusinessException {

	private static final long serialVersionUID = -4616741523376816068L;

	public LicitacaoNaoConcluidaException(String numeroLicitacao) {
        super(ErrorInfo.PROCESSO_LICITATORIO_NAO_CONCLUIDO, numeroLicitacao);
    }

}
