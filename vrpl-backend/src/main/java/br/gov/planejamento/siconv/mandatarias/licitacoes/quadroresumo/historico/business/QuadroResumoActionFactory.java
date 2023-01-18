package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.EventoQuadroResumoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;

public class QuadroResumoActionFactory {

	//TODO: Verify why this injection is not working on tests
	@Inject
	Instance<QuadroResumoAction> actionBeans;

	public QuadroResumoAction get(EventoQuadroResumoEnum evento) {
		Instance<QuadroResumoAction> localActionBeans = CDI.current().select(QuadroResumoAction.class);
		Instance<QuadroResumoAction> actionLocal = localActionBeans.select(new QuadroResumoEventTypeLiteral(evento));
		if (actionLocal.isUnsatisfied()) {
			throw new EventoQuadroResumoInvalidoException(evento.getDescricao());
		}
		return actionLocal.get();
	}

}
