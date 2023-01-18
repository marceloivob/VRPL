package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ArquivoAnexoSemExtensaoException extends BusinessException {

    private static final long serialVersionUID = -6320356908245336900L;

    public ArquivoAnexoSemExtensaoException()  {
        super(ErrorInfo.ERRO_ANEXO_SEM_EXTENSAO);
    }

}
