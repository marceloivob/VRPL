package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.validation.Validator;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvClientGRPCProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvGRPCConsumer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business.LiberarLoteBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions.EnviarParaAnaliseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.EnviarParaAnaliseEAceiteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.UsuarioPodeEnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.VersionamentoPorPropostaBC;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@EnableWeld
public class EnviarParaAnaliseActionTestIT {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator
    	.from(SiconvRest.class, SiconvRestMock.class, QuadroResumoActionFactory.class, QuadroResumoAction.class, QuadroResumoBaseAction.class, 
    			EnviarParaAnaliseAction.class, EnviarParaAnaliseEAceiteRules.class, EnviarParaAnaliseActionTestIT.class, UsuarioPodeEnviarParaAnaliseRules.class,
    			QuadroResumoTestMocks.class, BusinessExceptionContext.class ,BusinessExceptionProducer.class, VersionamentoPorPropostaBC.class, LiberarLoteBC.class, 
    			SiconvClientGRPCProducer.class,	SiconvGRPCConsumer.class, SiconvGRPCClient.class)
    	.activate(RequestScoped.class, SessionScoped.class).build();	
	
	@Inject
	QuadroResumoActionFactory actionFactory;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;	
	
	EnviarParaAnaliseAction action;

    Validator validator;

    @BeforeEach
    public void setUp() {
		assertNotNull(actionFactory);
		action = (EnviarParaAnaliseAction) actionFactory.get(EventoQuadroResumoEnum.ENVIAR_PARA_ANALISE);
    	when(dao.getJdbi()).thenReturn(mock(Jdbi.class));
    }
	
	@Test
	void testInjection() {
		assertNotNull(action);
	}
	
	@Disabled
	// TODO corrigir
	@Test
	void testExecute() {
		action.execute(mock(HistoricoLicitacaoDTO.class), mock(PropostaBD.class), mock(
				LicitacaoBD.class));
	}

	@Disabled
	// TODO corrigir
	@Test
	void testValidarComNull() {
		action.validar(null,  null, null);
	}

}
