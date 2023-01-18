package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvClientGRPCProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvGRPCConsumer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business.LiberarLoteBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions.EnviarParaAnaliseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.EventoQuadroResumoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.EnviarParaAnaliseEAceiteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.UsuarioPodeEnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.VersionamentoPorPropostaBC;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableWeld
class QuadroResumoActionFactoryTestIT {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator
    	.from(SiconvRest.class, SiconvRestMock.class, QuadroResumoActionFactory.class, QuadroResumoBaseAction.class,
    			QuadroResumoAction.class, EnviarParaAnaliseAction.class, EnviarParaAnaliseEAceiteRules.class,
    			QuadroResumoActionFactoryTestIT.class, QuadroResumoTestMocks.class, BusinessExceptionContext.class ,
    			UsuarioPodeEnviarParaAnaliseRules.class, BusinessExceptionProducer.class,VersionamentoPorPropostaBC.class,
    			LiberarLoteBC.class, SiconvClientGRPCProducer.class, SiconvGRPCConsumer.class, SiconvGRPCClient.class)
    	.activate(RequestScoped.class, SessionScoped.class).build();

	@Inject
	QuadroResumoActionFactory actionFactory;

	@Test
	void testInjection() {
		assertNotNull(actionFactory);
	}

	@Test
	void testQuadroResumoFactoryEnviarParaAnalise() {
		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.ENVIAR_PARA_ANALISE;
		QuadroResumoAction action = actionFactory.get(evento);
		assertEquals(EnviarParaAnaliseAction.class, action.getClass());
	}

	@Test
	void testQuadroResumoFactoryAssinarParecer() {
		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.fromSigla("APE");
		assertThrows(EventoQuadroResumoInvalidoException.class, () ->actionFactory.get(evento));
	}

}
