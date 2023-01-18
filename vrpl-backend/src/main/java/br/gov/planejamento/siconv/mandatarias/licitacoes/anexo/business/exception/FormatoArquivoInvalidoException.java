package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class FormatoArquivoInvalidoException extends BusinessException {

    private static final long serialVersionUID = 7018433494804392899L;

    public FormatoArquivoInvalidoException() {
        super(ErrorInfo.ERRO_FORMATO_ANEXO_NAO_PERMITIDO);
    }

}
