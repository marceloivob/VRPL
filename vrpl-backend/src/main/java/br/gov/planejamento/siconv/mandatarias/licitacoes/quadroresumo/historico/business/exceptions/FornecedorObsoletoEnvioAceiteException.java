package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class FornecedorObsoletoEnvioAceiteException extends BusinessException {

	private static final long serialVersionUID = -7330117330502114923L;

	public FornecedorObsoletoEnvioAceiteException(String licitacaoFornecedor) {
		super(ErrorInfo.FORNECEDORES_OBSOLETOS, licitacaoFornecedor);
	}
	
	public FornecedorObsoletoEnvioAceiteException() {
		super(ErrorInfo.FORNECEDORES_OBSOLETOS_ENVIO_ANALISE_ACEITE);
	}

}
