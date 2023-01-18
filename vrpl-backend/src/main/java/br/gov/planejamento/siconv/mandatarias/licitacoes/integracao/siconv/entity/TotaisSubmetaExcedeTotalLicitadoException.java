package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;

public class TotaisSubmetaExcedeTotalLicitadoException extends BusinessException {

	private static final long serialVersionUID = -2422693625940651571L;

	private static final DecimalFormat formatador = new DecimalFormat("#,###,##0.00");

	public TotaisSubmetaExcedeTotalLicitadoException(BigDecimal totalLicitado, PoVRPLDTO po, BigDecimal totaisSubmeta) {
		super(ErrorInfo.TOTAIS_SUBMETA_EXCEDE_TOTAL_LICITADO, formatador.format(totalLicitado),
				po.getNomeIdentificador(),
				formatador.format(totaisSubmeta));
    }

}
