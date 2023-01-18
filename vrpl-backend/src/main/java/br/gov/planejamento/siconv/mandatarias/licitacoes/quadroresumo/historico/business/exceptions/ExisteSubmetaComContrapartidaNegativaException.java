package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ExisteSubmetaComContrapartidaNegativaException extends BusinessException {

	private static final long serialVersionUID = -4347765055022441836L;

		public ExisteSubmetaComContrapartidaNegativaException() {
        super(ErrorInfo.EXISTE_SUBMETA_CONTRAPARTIDA_NEGATIVA);
    }

}
