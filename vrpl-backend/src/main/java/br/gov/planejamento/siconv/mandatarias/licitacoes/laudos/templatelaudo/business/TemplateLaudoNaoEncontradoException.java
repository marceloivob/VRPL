package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class TemplateLaudoNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 4198013859283055482L;

	public TemplateLaudoNaoEncontradoException() {
        super(ErrorInfo.TEMPLATELAUDO_NAO_ENCONTRADO);
    }

}
