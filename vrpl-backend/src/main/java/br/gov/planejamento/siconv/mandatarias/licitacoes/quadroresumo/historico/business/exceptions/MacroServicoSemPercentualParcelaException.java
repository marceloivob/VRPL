package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class MacroServicoSemPercentualParcelaException  extends BusinessException {

	private static final long serialVersionUID = 9049010447694303184L;

	public MacroServicoSemPercentualParcelaException(String descricaoSubmeta, String macroservico) {
        super(ErrorInfo.MACROSERVICO_SEM_PERCENTUAL_PARCELA, descricaoSubmeta, macroservico);
    }
}
