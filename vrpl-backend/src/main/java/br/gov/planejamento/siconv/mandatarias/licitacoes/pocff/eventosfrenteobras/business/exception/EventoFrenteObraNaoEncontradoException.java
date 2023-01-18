package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.business.exception;



import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class EventoFrenteObraNaoEncontradoException extends BusinessException {

    public EventoFrenteObraNaoEncontradoException() {
        super(ErrorInfo.EVENTOFRENTEOBRA_NAO_ENCONTRADO);
    }

}
