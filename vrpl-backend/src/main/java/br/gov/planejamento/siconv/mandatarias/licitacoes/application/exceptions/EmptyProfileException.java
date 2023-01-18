package br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class EmptyProfileException extends BusinessException {

    public EmptyProfileException() {
        super(ErrorInfo.ERRO_ACESSO_PERFIL_NAO_INFORMADO);
    }

}
