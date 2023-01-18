package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;

public class ServicoSemQuantidadesInformadasException extends BusinessException {

	private static final long serialVersionUID = 1481654711418894719L;

	public ServicoSemQuantidadesInformadasException(ServicoBD servico) {
		super(ErrorInfo.SERVICO_SEM_QUANTIDADES_INFORMADAS_EXCEPTION, servico.getTxDescricao());
    }
}
