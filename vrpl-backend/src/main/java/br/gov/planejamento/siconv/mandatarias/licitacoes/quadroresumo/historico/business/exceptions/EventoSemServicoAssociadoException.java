package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class EventoSemServicoAssociadoException extends BusinessException {

	private static final long serialVersionUID = -2575486403292688510L;

	public EventoSemServicoAssociadoException(String mensagem) {
		super(ErrorInfo.EVENTO_SEM_SERVICO_ASSOCIADO, mensagem);
	}
}
