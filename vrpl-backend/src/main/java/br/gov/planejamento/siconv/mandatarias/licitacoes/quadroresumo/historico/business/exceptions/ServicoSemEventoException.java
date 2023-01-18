package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;

public class ServicoSemEventoException extends BusinessException {

	private static final long serialVersionUID = 784306033875538025L;

	public ServicoSemEventoException(ServicoBD servico, PoVRPLDTO po) {
		super(ErrorInfo.SERVICO_SEM_EVENTO_EXCEPTION, servico.getTxDescricao(), po.getNomeIdentificador());
    }
}
