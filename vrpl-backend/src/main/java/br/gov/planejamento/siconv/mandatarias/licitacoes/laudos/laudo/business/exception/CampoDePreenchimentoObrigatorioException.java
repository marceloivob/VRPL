package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CampoDePreenchimentoObrigatorioException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CampoDePreenchimentoObrigatorioException(String campoObrigatorio) {
		super(ErrorInfo.ERRO_CAMPO_OBRIGATORIO_LAUDO, campoObrigatorio);
	}
	
	public CampoDePreenchimentoObrigatorioException(String campoObrigatorio, String sessao) {
		super(ErrorInfo.ERRO_CAMPO_OBRIGATORIO_LAUDO_COM_SESSAO, campoObrigatorio, sessao);
	}

}
