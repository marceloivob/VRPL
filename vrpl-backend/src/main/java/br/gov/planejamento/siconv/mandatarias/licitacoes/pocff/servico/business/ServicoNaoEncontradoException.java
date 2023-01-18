package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServicoNaoEncontradoException extends BusinessException {

	private static final long serialVersionUID = 1641801358766355320L;

	public ServicoNaoEncontradoException(ServicoBD servico) {
        super(ErrorInfo.SERVICO_NAO_ENCONTRADO);

		log.info("Mensagem: [{}], detalhe: [{}]", ErrorInfo.SERVICO_NAO_ENCONTRADO, servico);

    }

}
