package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PropostaComParecerEmitidoException extends BusinessException {

	private static final long serialVersionUID = 462024006017780367L;

	public PropostaComParecerEmitidoException() {
        super(ErrorInfo.PROPOSTA_COM_PARECER_EMITIDO);
    }
}
