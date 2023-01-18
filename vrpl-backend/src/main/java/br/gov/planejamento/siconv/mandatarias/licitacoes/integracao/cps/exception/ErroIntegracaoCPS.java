package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErroIntegracaoCPS extends BusinessException {

	private static final long serialVersionUID = 4939551351457305928L;

	public ErroIntegracaoCPS(Exception e) {
		super(ErrorInfo.ERRO_AO_ACIONAR_SERVICO_GPRC_CPS);

		log.error(e.getMessage(), e);
	}
}
