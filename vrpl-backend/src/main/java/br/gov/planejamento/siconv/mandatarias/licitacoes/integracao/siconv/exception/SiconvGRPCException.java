package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class SiconvGRPCException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SiconvGRPCException(String mensagem) {
		super(mensagem);
	}

}
