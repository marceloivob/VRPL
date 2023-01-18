package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class SomatorioPosDifereValorCTEFException extends BusinessException {

	private static final long serialVersionUID = 4658401139730289013L;

	public SomatorioPosDifereValorCTEFException() {
        super(ErrorInfo.SOMATORIO_POS_DIFERE_VALOR_CTEF);
    }

}
