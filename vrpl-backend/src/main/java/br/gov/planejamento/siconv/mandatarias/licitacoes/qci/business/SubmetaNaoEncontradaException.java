package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class SubmetaNaoEncontradaException extends BusinessException {



    /**
     *
     */
    private static final long serialVersionUID = -147526539631106109L;

    public SubmetaNaoEncontradaException() {
        super(ErrorInfo.SUBMETA_NAO_ENCONTRADA);
    }
}
