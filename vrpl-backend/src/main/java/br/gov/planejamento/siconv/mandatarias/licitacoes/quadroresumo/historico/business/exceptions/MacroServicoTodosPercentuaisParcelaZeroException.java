package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class MacroServicoTodosPercentuaisParcelaZeroException extends BusinessException {

	private static final long serialVersionUID = -5501246953731879076L;

	public MacroServicoTodosPercentuaisParcelaZeroException(String submeta) {
        super(ErrorInfo.MACROSERVICO_SEM_PERCENTUAL_PARCELA, submeta);
    }
}
