package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PerguntaNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 8440748605652314994L;

	public PerguntaNaoEncontradoException() {
        super(ErrorInfo.PERGUNTA_NAO_ENCONTRADA);
    }

}
