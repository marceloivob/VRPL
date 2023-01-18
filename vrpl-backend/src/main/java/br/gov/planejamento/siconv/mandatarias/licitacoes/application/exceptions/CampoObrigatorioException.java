package br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CampoObrigatorioException extends BusinessException {

    private static final long serialVersionUID = 8137929452463827245L;

    public CampoObrigatorioException() {
        super(ErrorInfo.ERRO_CAMPO_OBRIGATORIO);
    }

}
