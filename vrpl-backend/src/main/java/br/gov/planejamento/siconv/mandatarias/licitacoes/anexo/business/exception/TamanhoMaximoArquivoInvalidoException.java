package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class TamanhoMaximoArquivoInvalidoException extends BusinessException {

    private static final long serialVersionUID = -9171953282568254389L;

    public TamanhoMaximoArquivoInvalidoException() {
        super(ErrorInfo.ERRO_TAMANHO_MAXIMO_ANEXO_EXCEDIDO);
    }

}
