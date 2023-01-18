package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class EventoFrenteObraSemMesConclusao extends BusinessException {
    
    public EventoFrenteObraSemMesConclusao(String descricao) {
        super(ErrorInfo.EVENTO_FRENTE_OBRA_SEM_MES, descricao);
    }
}
