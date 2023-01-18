package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PlanilhaOrcamentariaNaoCadastrada extends BusinessException {

	private static final long serialVersionUID = -7100687497889656278L;

	public PlanilhaOrcamentariaNaoCadastrada() {
		super(ErrorInfo.NAO_EXISTE_PO_CADASTRADA);
	}

}
