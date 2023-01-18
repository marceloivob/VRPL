package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ExisteLoteComPosDeOrcamentosDiferentesException extends BusinessException {

	private static final long serialVersionUID = -4757824037648184149L;

	public ExisteLoteComPosDeOrcamentosDiferentesException(String relatorio) {
		super(ErrorInfo.EXISTE_LOTE_COM_ORCAMENTOS_DE_REFERENCIA_DIFERENTE, relatorio);
	}

}
