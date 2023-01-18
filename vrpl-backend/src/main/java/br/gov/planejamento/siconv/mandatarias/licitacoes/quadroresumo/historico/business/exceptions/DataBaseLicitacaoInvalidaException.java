package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class DataBaseLicitacaoInvalidaException extends BusinessException {

	private static final long serialVersionUID = -5877163116110099043L;

	public DataBaseLicitacaoInvalidaException() {
		super(ErrorInfo.VALIDACAO_DATABASE_LICITACAO);
	}
	

}
