package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;

public class EventoQuadroResumoInvalidoException extends MandatariasException {

	private static final long serialVersionUID = -5385164732302997209L;

	public EventoQuadroResumoInvalidoException(String evento) {
		super("Não existe implementação de action correspondente ao evento " + evento);
	}
}
