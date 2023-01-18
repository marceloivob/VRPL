package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ValorLicitadoExcedeAprovadoException extends BusinessException {

	private static final long serialVersionUID = -2089856665050608549L;

	private static final DecimalFormat formatador = new DecimalFormat("#,###,##0.00");

	public ValorLicitadoExcedeAprovadoException(BigDecimal valorLicitado, BigDecimal valorAprovado, String submeta) {
		super(ErrorInfo.VALOR_LICITADO_EXCEDE_VALOR_APROVADO, formatador.format(valorLicitado), formatador.format(valorAprovado),
				submeta);
    }
}
