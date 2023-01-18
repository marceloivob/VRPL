package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.net.ssl.SSLException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.weld.junit5.EnableWeld;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.grpc.PropostaBooleanResponse;
import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.NaoExisteSPAHomologado;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.PropostaNotFoundException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.VRPLSemPermissaoAcessoProposta;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.integracao.LicitacaoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.UpdatePropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockClientLicitacoesInterface;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
public class PropostaBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy @InjectMocks
	private PropostaBC propostaBC;

	@Spy
	private LicitacaoBC licitacaoBC;

	@Spy
	private SiconvBC siconvBC;

	@Spy
	private ConstraintBeanValidation<ServicoBD> constraintBeanValidation;

	@Mock
	private PropostaDAO propostaDAO;
	
	private Logger logger = LoggerFactory.getLogger(PropostaBCIT.class);

	private Long idProposta = 1L;
	private String cpfUsuarioLogado = "00000000000";//"12345678912";

	@Mock
	private SiconvGRPCClient siconvGrpcClient;
	
	private SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, idProposta, cpfUsuarioLogado);
	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		logger.info("EXECUTING PRODUCER!!!");
		MockSiconvPrincipal usuarioLogado1 = new MockSiconvPrincipal(Profile.MANDATARIA, idProposta, cpfUsuarioLogado);
		logger.info("MockSiconvPrincipal: {}", usuarioLogado1);
		logger.info("CPF: {}", usuarioLogado1.getCpf());
		//this.usuarioLogado = usuarioLogado1;
		return usuarioLogado1;
	}

	@BeforeEach
	public void setUp() throws SSLException {
		MockitoAnnotations.openMocks(this);

		//this.usuarioLogado = (MockSiconvPrincipal) produceSiconvPrincipal();

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		siconvBC.setDao(daoFactory);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator beanValidator = factory.getValidator();

		constraintBeanValidation.setBeanValidator(beanValidator);

		propostaBC.setBeanValidator(constraintBeanValidation);
		propostaBC.setSiconvBC(siconvBC);

		propostaBC.setUsuarioLogado(usuarioLogado);
		propostaBC.setDao(daoFactory);

		licitacaoBC.setDao(daoFactory);
	}

	
	@Test
	public void verificaInicioVrpl_existePropostaVrpl() {
		Mockito.when (propostaBC.getPropostaDAO()).thenReturn(propostaDAO);
		Mockito.when (propostaDAO.existePropostaVrpl(propostaBC.getUsuarioLogado())).thenReturn(Boolean.TRUE);
		Mockito.doNothing().when(propostaBC).atualizaDadosImportados(Mockito.any());
		
		propostaBC.verificaInicioVrpl();
				
		Mockito.verify(propostaBC).atualizaDadosImportados(Mockito.any(Handle.class)); 
		
	}

	@Test
	public void verificaInicioVrpl_naoPermiteAcessoVrpl() {
		
		PropostaBooleanResponse response = PropostaBooleanResponse.newBuilder().setRetorno(Boolean.FALSE).build(); 
		

		Mockito.when (propostaDAO.existePropostaVrpl(propostaBC.getUsuarioLogado())).thenReturn(Boolean.FALSE);
		Mockito.when (siconvGrpcClient.isVRPLResponsavelAceiteProcessoExecucao(propostaBC.getUsuarioLogado().getIdProposta())).thenReturn(response);
		
		assertThrows(VRPLSemPermissaoAcessoProposta.class, () -> propostaBC.verificaInicioVrpl());
		
	}

	
	@Test
	public void verificaInicioVrpl_permiteAcessoVrpl_ImportarTodosOsDadosDaProposta() {
		
		PropostaBooleanResponse response = PropostaBooleanResponse.newBuilder().setRetorno(Boolean.TRUE).build(); 
		
		Mockito.when (propostaBC.getPropostaDAO()).thenReturn(propostaDAO);
		Mockito.when (propostaDAO.existePropostaVrpl(propostaBC.getUsuarioLogado())).thenReturn(Boolean.FALSE);
		Mockito.when (siconvGrpcClient.isVRPLResponsavelAceiteProcessoExecucao(propostaBC.getUsuarioLogado().getIdProposta())).thenReturn(response);
		Mockito.doNothing().when(propostaBC).importarTodosOsDadosDaProposta();
		Mockito.when (propostaBC.getSiconvBC().existeSPAHomologadoParaProposta(propostaBC.getUsuarioLogado())).thenReturn(true);
		
		
		propostaBC.verificaInicioVrpl();
				
		Mockito.verify(propostaBC).importarTodosOsDadosDaProposta(); 
		
	}
	
	@Test
	public void verificaInicioVrplException_SPAHomologadoException() {
		PropostaBooleanResponse response = PropostaBooleanResponse.newBuilder().setRetorno(Boolean.TRUE).build();
		
		Mockito.when(siconvGrpcClient.isVRPLResponsavelAceiteProcessoExecucao(propostaBC.getUsuarioLogado().getIdProposta())).thenReturn(response);
		
		assertThrows(NaoExisteSPAHomologado.class, () -> propostaBC.verificaInicioVrpl());
	}


	@Test
	public void verificaInicioVrplExecutaImportarDadosCall() {
		PropostaBooleanResponse response = PropostaBooleanResponse.newBuilder().setRetorno(Boolean.TRUE).build();
		Mockito.when(siconvGrpcClient.isVRPLResponsavelAceiteProcessoExecucao(propostaBC.getUsuarioLogado().getIdProposta())).thenReturn(response);

		
		daoFactory.getJdbi().useHandle(handle -> {
			handle.execute(DataFactory.getAcffo());
		});
		doNothing().doThrow(new RuntimeException()).when(propostaBC).importarTodosOsDadosDaProposta();
		propostaBC.verificaInicioVrpl();
	}


	

	private void atualizarDadosVrpl() {
		daoFactory.getJdbi().useHandle(handle -> {
			handle.execute(DataFactory.getAcffo());
			handle.execute(DataFactory.getConvenioInsert());
			handle.execute(DataFactory.getPropostaInsert());
			handle.execute(DataFactory.getOrgAdministrativoInsert());
		});
	}

	@Test
	public void testImportarDadosDoConvenioProposta() {
		this.atualizarDadosVrpl();
		LicitacaoBC licitacaoBCMock = Mockito.mock(LicitacaoBC.class);
		propostaBC.setLicitacaoBC(licitacaoBCMock);

		doReturn(Collections.emptyList()).when(propostaBC).importarSubitemInvestimento(any(), any());
		doReturn(Collections.emptyList()).when(propostaBC).importarSubmetas(any(), any(), any(), any(), any());

		PropostaBD proposta = propostaBC.getPropostaDAO().loadById(idProposta);
		assertNull(proposta);

		propostaBC.importarTodosOsDadosDaProposta();

		PropostaBD propostaBD = propostaBC.getPropostaDAO().loadById(idProposta);
		assertEquals(Integer.valueOf(2020), propostaBD.getAnoConvenio());
		assertEquals(Integer.valueOf(883675), propostaBD.getNumeroConvenio());
		assertEquals(LocalDate.of(2020, 1, 9), propostaBD.getDataAssinaturaConvenio());
		assertEquals("CAIXA ECONOMICA FEDERAL", propostaBD.getNomeMandataria());

	}

	@Test
	public void testImportarDadosDoConvenioPropostaSemDadosBasicos() {
		PropostaBD proposta = propostaBC.getPropostaDAO().loadById(idProposta);
		assertNull(proposta);

		assertThrows(PropostaNotFoundException.class, () -> propostaBC.importarTodosOsDadosDaProposta());

	}

	@Disabled
	@Test
	public void testAtualizarDadosImportadosDoConvenio() {
		this.atualizarDadosVrpl();
		LicitacaoBC licitacaoBCMock = Mockito.mock(LicitacaoBC.class);
		propostaBC.setLicitacaoBC(licitacaoBCMock);

		doReturn(Collections.emptyList()).when(propostaBC).importarSubitemInvestimento(any(), any());
		doReturn(Collections.emptyList()).when(propostaBC).importarSubmetas(any(), any(), any(), any(), any());
//		doReturn(Collections.emptyList()).when(propostaBC).consultaLicitacoesDoBancoLocal(any(), any());

		PropostaBD proposta = new PropostaBD();
		PropostaDAO propostaDAO = mock(PropostaDAO.class);

		Handle transaction = mock(Handle.class);
		when(transaction.attach(PropostaDAO.class)).thenReturn(propostaDAO);
		when(propostaDAO.recuperaUltimaVersaoDaProposta(any())).thenReturn(proposta);

		// Mock do update*
		UpdatePropostaDAO updatePropostaDAO = mock(UpdatePropostaDAO.class);
		when(transaction.attach(UpdatePropostaDAO.class)).thenReturn(updatePropostaDAO);

		propostaBC.atualizaDadosImportados(transaction);

		// O objeto proposta deve ter sido atualizado
		PropostaBD propostaBD = proposta;
		assertEquals(Integer.valueOf(2020), propostaBD.getAnoConvenio());
		assertEquals(Integer.valueOf(883675), propostaBD.getNumeroConvenio());
		assertEquals(LocalDate.of(2020, 1, 9), propostaBD.getDataAssinaturaConvenio());
		assertEquals("CAIXA ECONOMICA FEDERAL", propostaBD.getNomeMandataria());

	}
}
