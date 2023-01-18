package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class DataBaseAnaliseInvalidaException extends BusinessException {

	private static final long serialVersionUID = 7952427308119543740L;
	
	public DataBaseAnaliseInvalidaException() {
		super(ErrorInfo.VALIDACAO_DATABASE_ANALISE);
	}
	

}
