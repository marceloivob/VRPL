package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PeriodoConclusaoFrenteObraNaoPreenchidoException extends BusinessException {

	private static final long serialVersionUID = 6966384293571577198L;

	public PeriodoConclusaoFrenteObraNaoPreenchidoException(String evento) {
        super(ErrorInfo.PERIODO_CONCLUSAO_NAO_PREENCHIDO, evento);
    }

}
