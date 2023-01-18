package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class LicitacaoAnexoNotFoundException extends BusinessException {

    private static final long serialVersionUID = -8454279209481968046L;

    public LicitacaoAnexoNotFoundException() {
        super(ErrorInfo.ERRO_LICITACAO_DO_ANEXO_NAO_ENCONTRADA);
    }

}
