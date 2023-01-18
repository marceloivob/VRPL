package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ExistePoSemEventosNaLicitacaoException extends BusinessException {

	private static final long serialVersionUID = 461552948087088121L;

	public ExistePoSemEventosNaLicitacaoException(String submeta) {
        super(ErrorInfo.EXISTE_PO_SEM_EVENTOS_NA_LICITACAO, submeta);
    }

}
