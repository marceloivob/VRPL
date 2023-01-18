package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AcionarServicoVerificarExistenciaDocumentoLiquidacao extends BusinessException {

	/**
     *
     */
	private static final long serialVersionUID = 9161178541111980787L;

	public AcionarServicoVerificarExistenciaDocumentoLiquidacao(ErrorInfo errorInfo) {
		super(errorInfo);
	}

	public AcionarServicoVerificarExistenciaDocumentoLiquidacao (Exception e) {
		super(ErrorInfo.ERRO_AO_ACIONAR_SERVICO_VERIFICACAO_DOC_LIQUIDACAO);

        log.error(String.format("Mensagem: [%s]", ErrorInfo.ERRO_AO_ACIONAR_SERVICO_VERIFICACAO_DOC_LIQUIDACAO), e);
	}


}

