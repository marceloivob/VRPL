package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class GrupoPerguntaNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = -14061196320713540L;

	public GrupoPerguntaNaoEncontradoException() {
        super(ErrorInfo.GRUPOPERGUNTA_NAO_ENCONTRADO);
    }

}
