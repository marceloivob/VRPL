package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class UsuarioJaAssinouParecerException extends BusinessException {

	private static final long serialVersionUID = 660729090398257840L;
	
	public UsuarioJaAssinouParecerException() {
		super(ErrorInfo.USUARIO_JA_ASSINOU_PARECER);
	}

}
