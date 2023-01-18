package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ServicoSemFrenteObraException extends BusinessException {

	private static final long serialVersionUID = -3398991136133458588L;

	public ServicoSemFrenteObraException(String servico) {
        super(ErrorInfo.SERVICO_SEM_FRENTE_DE_OBRA_EXCEPTION, servico);
    }
}
