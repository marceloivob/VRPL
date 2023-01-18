package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.mail.Address;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.GeradorDeTicket;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.EmailTemplate;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions.AceitarDocumentacaoAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.actions.EnviarParaAnaliseAction;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;


class QuadroResumoBaseActionUnitTest {

	@Spy
	QuadroResumoBaseAction action;

	@Mock
	QuadroResumoValidator validatorMock;

	@Mock
	private DAOFactory dao;
	
	@InjectMocks
	private ConcreteDAOFactory daoFactory;
	
	@InjectMocks
	private JDBIFactory jdbiFactory;

	@Mock
	private BusinessExceptionContext businessExceptionContext;
	
	@Mock
	Jdbi jdbi;
	
	@Mock
	MailerUtils mailer;

	@Spy
	@InjectMocks
	GeradorDeTicket geradorTicket;
	
	@Mock
	EmailTemplate emailTemplate;

	@Mock
	SiconvBC siconvBC;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		
		action.setDao(dao);
		action.setBusinessExceptionContext(businessExceptionContext);
		
		when(dao.getJdbi()).thenReturn(jdbi);
		when(action.getValidators(any(), any(), any())).thenReturn(Arrays.asList(validatorMock));
		when(action.getGeradorDeTicket()).thenReturn(geradorTicket);
		when(action.prepararEmail(any(), any())).thenReturn(mock(EmailInfo.class));
		when(action.getHistoricoBC()).thenReturn(mock(HistoricoLicitacaoBC.class));
		when(action.getMailer()).thenReturn(mailer);
		
		Mockito.doNothing()
	      .when(businessExceptionContext)
	      .throwException();
		
		// Cria uma DaoFactory real para ser Executada
		daoFactory.setJdbi(jdbiFactory.createJDBI());
	}

	@Test
	void testInit() {
		assertNotNull(action);
	}

	@Test
	void testValidar() {
		action.validar(null, null, null);
	}

	@Test
	void testExecuteChamaValidar() {
		action.execute(mock(HistoricoLicitacaoDTO.class), mock(PropostaBD.class), mock(LicitacaoBD.class) );
		verify(action).validar(any(), any(), any());
	}

	@Test
	void testExecuteChamaGerarHistorico() {
		action.setDao(dao);
		
		//Nesse ponto o DAO(mock) retorna uma instancia(jdbi) real; 
		when(dao.getJdbi()).thenReturn(daoFactory.getJdbi());

		action.execute(mock(HistoricoLicitacaoDTO.class), mock(PropostaBD.class), mock(LicitacaoBD.class));
		verify(action).gerarHistorico(any(), any(), any());
	}

	@Test
	void testExecuteChamaExecuteRest() throws Exception {
		
		//action2
		QuadroResumoBaseAction action2 =  Mockito.spy(EnviarParaAnaliseAction.class);
		action2.setDao(dao);
		
		action2.setBusinessExceptionContext(businessExceptionContext);

		//Nesse ponto o DAO(mock) retorna uma instancia(jdbi) real; 
		when(dao.getJdbi()).thenReturn(daoFactory.getJdbi());

		
		when(action2.getHistoricoBC()).thenReturn(mock(HistoricoLicitacaoBC.class));
		doNothing().when(action2).validar(any(), any(), any());
		doNothing().when(action2).executarIntegracoes(any(),any(), any(), any());
		
		action2.execute(mock(HistoricoLicitacaoDTO.class), mock(PropostaBD.class), mock(LicitacaoBD.class));
		verify(action2).executarIntegracoes(any(), any(), any(), any());

	}
	
	@Test
	void testExecuteChamaPrepararEmail() {
	
		Set<Address> address = new HashSet<>(); 
		
		//action3
		AceitarDocumentacaoAction action3 =  Mockito.spy(AceitarDocumentacaoAction.class);
		action3.setDao(dao);
		
		action3.setBusinessExceptionContext(businessExceptionContext);
		action3.setEmailTemplate(emailTemplate);
		action3.setSiconvBC(siconvBC);
		
		when(action3.getHistoricoBC()).thenReturn(mock(HistoricoLicitacaoBC.class));
		doNothing().when(action3).validar(any(), any(), any());
		doNothing().when(action3).executarIntegracoes(any(),any(), any(), any());
		when (siconvBC.buscarEmails(any(), any())).thenReturn(address);
		when(action3.getMailer()).thenReturn(mailer);
	
		action3.execute(mock(HistoricoLicitacaoDTO.class), mock(PropostaBD.class), mock(LicitacaoBD.class));
		verify(action3).prepararEmail(any(), any());
		verify(mailer).send(any());
	}

}
