package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CtefNotFoundException extends BusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 5240683081691805438L;

    /**
     * RN: não há
     */
    public CtefNotFoundException() {
        super(ErrorInfo.ERRO_ACIONAMENTO_SERVICO_SICONV_CTEF);
    }
}
