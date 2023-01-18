package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CffComEventoFaltandoAssociacaoException extends BusinessException {

	private static final long serialVersionUID = 7785181623616669127L;

	public CffComEventoFaltandoAssociacaoException(String descricaoSubmeta, String evento) {
        super(ErrorInfo.CFF_COM_EVENTO_FALTANDO_ASSOCIACAO, descricaoSubmeta, evento);
    }

}
