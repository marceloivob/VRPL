package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import javax.enterprise.util.AnnotationLiteral;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;

public class QuadroResumoEventTypeLiteral extends AnnotationLiteral<QuadroResumoEventType> implements QuadroResumoEventType {

	private static final long serialVersionUID = 4954951665364620713L;

	private EventoQuadroResumoEnum type;

	public QuadroResumoEventTypeLiteral(EventoQuadroResumoEnum type) {
		this.type = type;
	}
	
	@Override
	public EventoQuadroResumoEnum value() {
		return type;
	}

}
