package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class NumeroFrenteDeObraRepetidoException extends BusinessException {

	private static final long serialVersionUID = 1787591119561302796L;

	public NumeroFrenteDeObraRepetidoException() {
        super(ErrorInfo.FRENTE_DE_OBRA_NUMERO_REPETIDO);
    }

}
