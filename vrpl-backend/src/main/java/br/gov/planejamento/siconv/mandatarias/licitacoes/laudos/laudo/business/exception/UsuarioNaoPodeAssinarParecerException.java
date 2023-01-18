package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class UsuarioNaoPodeAssinarParecerException extends BusinessException  {

	private static final long serialVersionUID = -7610305684422332303L;
	
	public UsuarioNaoPodeAssinarParecerException() {
		super(ErrorInfo.USUARIO_NAO_PODE_ASSINAR_PARECER);
	}
}
