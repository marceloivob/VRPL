package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ExistePoSemFrenteDeObraNaLicitacaoException extends BusinessException {

	private static final long serialVersionUID = 1514371588966445909L;

	public ExistePoSemFrenteDeObraNaLicitacaoException(String submeta) {
        super(ErrorInfo.EXISTE_PO_SEM_FRENTE_DE_OBRA_NA_LICITACAO, submeta);
    }
}
