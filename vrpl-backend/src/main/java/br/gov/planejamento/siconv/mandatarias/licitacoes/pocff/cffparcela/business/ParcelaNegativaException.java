package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ParcelaNegativaException extends BusinessException {

    public ParcelaNegativaException() {
        super(ErrorInfo.MACROSERVICOPARCELA_NEGATIVA);
    }

}
