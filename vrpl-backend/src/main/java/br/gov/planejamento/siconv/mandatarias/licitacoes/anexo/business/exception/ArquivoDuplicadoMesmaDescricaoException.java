package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ArquivoDuplicadoMesmaDescricaoException extends BusinessException {

    private static final long serialVersionUID = 7210766975623617077L;

    public ArquivoDuplicadoMesmaDescricaoException() {
        super(ErrorInfo.ERRO_EXISTE_ANEXO_COM_MESMA_DESCRICAO);
    }

}
