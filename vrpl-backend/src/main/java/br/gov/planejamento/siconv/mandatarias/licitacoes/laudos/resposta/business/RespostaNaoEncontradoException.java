package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class RespostaNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 2074861071704925292L;

	public RespostaNaoEncontradoException() {
        super(ErrorInfo.RESPOSTA_NAO_ENCONTRADA);
    }

}
