package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ConsideracoesEmBrancoRejeicaoParecerViavel extends BusinessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6417843182425402608L;

	public ConsideracoesEmBrancoRejeicaoParecerViavel() {
		 super(ErrorInfo.CONSIDERACOES_OBRIGATORIO_REJEITAR_VIAVEL);
	}

}
