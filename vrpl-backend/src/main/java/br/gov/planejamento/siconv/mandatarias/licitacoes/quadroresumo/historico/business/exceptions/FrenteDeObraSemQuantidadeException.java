package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class FrenteDeObraSemQuantidadeException extends BusinessException {

	private static final long serialVersionUID = -9208905290526298234L;

	public FrenteDeObraSemQuantidadeException(String po, List<String> frentesObra) {
		super(ErrorInfo.FRENTE_DE_OBRA_SEM_QUANTIDADE_INFORMADA, po, frentesObra);
	}
	
	public FrenteDeObraSemQuantidadeException(String po, String frentesObra) {
		super(ErrorInfo.FRENTE_DE_OBRA_SEM_QUANTIDADE_INFORMADA, po, frentesObra);
	}

}
