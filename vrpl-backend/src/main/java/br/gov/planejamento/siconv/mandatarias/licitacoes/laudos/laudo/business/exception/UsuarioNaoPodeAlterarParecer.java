package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class UsuarioNaoPodeAlterarParecer extends BusinessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5219584497677849341L;

	public UsuarioNaoPodeAlterarParecer(){
		super(ErrorInfo.USUARIO_NAO_PODE_ALTERAR_PARECER);
	}
}
