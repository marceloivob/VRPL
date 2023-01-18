package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class LaudoNaoEncontradoException extends BusinessException {

    public LaudoNaoEncontradoException() {
        super(ErrorInfo.LAUDO_NAO_ENCONTRADO);
    }

}
