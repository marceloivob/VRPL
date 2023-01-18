package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventoDoServicoNaoEncontradoException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventoDoServicoNaoEncontradoException(ServicoBD servico) {
        super(ErrorInfo.EVENTO_NAO_ENCONTRADO_SERVICO);

		log.info("Mensagem: [{}], detalhe: [{}]", ErrorInfo.EVENTO_NAO_ENCONTRADO_SERVICO, servico);

    }
}
