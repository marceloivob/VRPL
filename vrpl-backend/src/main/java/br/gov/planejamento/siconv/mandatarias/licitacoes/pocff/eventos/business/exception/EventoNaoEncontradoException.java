package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class EventoNaoEncontradoException extends BusinessException {

    /**
     *
     */
    private static final long serialVersionUID = -8841233355074828185L;

    public EventoNaoEncontradoException() {
        super(ErrorInfo.EVENTO_NAO_ENCONTRADO);
    }

}
