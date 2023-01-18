package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ArquivoDuplicadoMesmoNomeException extends BusinessException {

    private static final long serialVersionUID = 7525895077418132260L;

    public ArquivoDuplicadoMesmoNomeException() {
        super(ErrorInfo.ERRO_EXISTE_ANEXO_COM_MESMO_NOME);
    }

}
