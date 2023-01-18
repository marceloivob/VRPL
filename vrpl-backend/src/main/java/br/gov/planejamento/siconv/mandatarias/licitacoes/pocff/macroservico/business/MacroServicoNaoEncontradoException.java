package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class MacroServicoNaoEncontradoException extends BusinessException {

    public MacroServicoNaoEncontradoException() {
        super(ErrorInfo.MACROSERVICO_NAO_ENCONTRADO);
    }

}
