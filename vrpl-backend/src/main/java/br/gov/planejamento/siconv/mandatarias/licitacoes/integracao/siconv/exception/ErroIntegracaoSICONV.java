package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ErroIntegracaoSICONV extends BusinessException {

	private static final long serialVersionUID = -5677796734046801020L;

	public ErroIntegracaoSICONV() {
		super(ErrorInfo.ERRO_AO_ACIONAR_SERVICO_NO_MODULO_SICONV);
	}

}
