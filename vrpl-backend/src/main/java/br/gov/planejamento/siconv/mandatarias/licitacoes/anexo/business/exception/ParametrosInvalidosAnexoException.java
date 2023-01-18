package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ParametrosInvalidosAnexoException extends BusinessException {

    private static final long serialVersionUID = 6205721339467937010L;

    public ParametrosInvalidosAnexoException() {
        super(ErrorInfo.ERRO_PARAMETROS_DO_ANEXO_INVALIDOS);
    }

}
