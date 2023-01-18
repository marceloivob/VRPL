package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessoExecucaoNaoEncontradoException extends BusinessException {

	public ProcessoExecucaoNaoEncontradoException(Long identificadorDaProposta) {
		super(ErrorInfo.ERRO_PROCESSO_EXECUCAO_NAO_ENCONTRADO);

		log.debug("Mensagem: [{}], IdProposta: {}]", ErrorInfo.ERRO_PROCESSO_EXECUCAO_NAO_ENCONTRADO,
				identificadorDaProposta);
	}

}
