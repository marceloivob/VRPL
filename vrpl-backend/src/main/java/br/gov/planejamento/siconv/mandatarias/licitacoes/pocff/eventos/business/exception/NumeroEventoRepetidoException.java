package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class NumeroEventoRepetidoException extends BusinessException {

    private static final long serialVersionUID = 1968346452170305579L;

    public NumeroEventoRepetidoException() {
        super(ErrorInfo.EVENTO_NUMERO_REPETIDO);
    }
}
