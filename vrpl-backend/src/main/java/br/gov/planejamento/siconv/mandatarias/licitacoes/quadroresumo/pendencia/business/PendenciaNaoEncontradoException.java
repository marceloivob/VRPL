package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PendenciaNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 1833673470723562649L;

	public PendenciaNaoEncontradoException() {
        super(ErrorInfo.PENDENCIA_NAO_ENCONTRADA);
    }

}
