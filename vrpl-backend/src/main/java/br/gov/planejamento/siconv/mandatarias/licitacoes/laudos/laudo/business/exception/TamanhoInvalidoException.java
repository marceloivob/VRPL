package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class TamanhoInvalidoException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TamanhoInvalidoException(String campo) {
		super(ErrorInfo.ERRO_TAMANHO_INVALIDO_JUSTIFICATIVA_LAUDO, campo);
	}

	public TamanhoInvalidoException(String campo, String secao) {
		super(ErrorInfo.ERRO_TAMANHO_INVALIDO_LAUDO, campo, secao);
	}

}
