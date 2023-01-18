package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class SomatorioLicitacaoExcedeValorAprovadoException  extends BusinessException {

	private static final long serialVersionUID = 559701127195433168L;

	private static final DecimalFormat formatador = new DecimalFormat("#,###,##0.00");

	public SomatorioLicitacaoExcedeValorAprovadoException(String tipoValor1, BigDecimal valorLicitado, 
			String tipoValor2, String instrumento, BigDecimal valorAprovado) {
		super(ErrorInfo.SOMATORIO_LICITACAO_EXCEDE_VALOR_APROVADO, tipoValor1, formatador.format(valorLicitado),
				tipoValor2, instrumento, formatador.format(valorAprovado));
    }

}
