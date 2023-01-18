package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class FrenteDeObraComQuantidadeZeroException extends BusinessException {

	private static final long serialVersionUID = 9018424986062536371L;

	public FrenteDeObraComQuantidadeZeroException(String po, List<String> frentesObra) {
		super(ErrorInfo.FRENTE_DE_OBRA_SERVICO_QUANTIDADE_ZERADA, po, frentesObra);
	}

}
