package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class MacroServicoParcelaNaoEncontradoException extends BusinessException {

    public MacroServicoParcelaNaoEncontradoException() {
        super(ErrorInfo.MACROSERVICOPARCELA_NAO_ENCONTRADO);
    }

}
