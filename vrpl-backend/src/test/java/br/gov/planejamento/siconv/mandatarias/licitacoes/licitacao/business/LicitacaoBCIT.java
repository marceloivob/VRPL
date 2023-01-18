package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.SalvarLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.rules.SalvarLicitacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.AssociacaoLicitacaoLoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.integracao.LicitacaoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.MockClientLicitacoesInterface;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@DisplayName(value = "LicitacaoBC - Testando validador do método salvar associação de lotes")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LicitacaoBCIT {
	
	@Spy
	private LicitacaoBC licitacaoBC;
	
	@Mock
	private ConcreteDAOFactory daoFactory;
	
	@Mock
	private PropostaDAO propostaDAO;
	
	@Mock
	private LicitacaoDAO licitacaoDAO;
	
	@InjectMocks
	private MockClientLicitacoesInterface clientLicitacoes;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.endpoint", "nodes.estaleiro.serpro");
		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.port", "30287");
		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.useSSL", "false");

		licitacaoBC.setClientLicitacoes(clientLicitacoes);

		licitacaoBC.setSalvarLicitacaoRules(new SalvarLicitacaoRules());
		licitacaoBC.setDao(daoFactory);
	}
	
	@Test
	public void ehPropostaAtualEhProponenteEmPreenchimento() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.PROPONENTE) );
		
		//Null pointer quer dizer que passou pelas validações
		assertThrows( NullPointerException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void ehPropostaAtualEhProponenteEmComplementacao() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.PROPONENTE) );
		
		//Null pointer quer dizer que passou pelas validações
		assertThrows( NullPointerException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void naoEhPropostaAtualEhProponenteEmPreenchimento() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(false) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.PROPONENTE) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void ehPropostaAtualNaoEhProponenteEmComplementacao() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.MANDATARIA) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void ehPropostaAtualEhProponenteEnviadaParaAnalise() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.PROPONENTE) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void ehPropostaAtualEhProponenteEmAnalise() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.PROPONENTE) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void ehPropostaAtualEhConcedenteSolicitadaComplementacao() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(true) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.CONCEDENTE) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	@Test
	public void naoEhPropostaAtualEhMandatariaHomologada() {
		PropostaDAO propostaDAO = mock(PropostaDAO.class);
		when(propostaDAO.loadById(any())).thenReturn( montaProposta(false) );
		when(daoFactory.get(PropostaDAO.class)).thenReturn(propostaDAO);
		
		LicitacaoDAO licitacaoDAO = mock(LicitacaoDAO.class);
		when(licitacaoDAO.findLicitacaoById(any())).thenReturn( montaLicitacao(SituacaoLicitacaoEnum.HOMOLOGADA.getSigla()) );
		when(daoFactory.get(LicitacaoDAO.class)).thenReturn(licitacaoDAO);
		
		licitacaoBC.setUsuarioLogado( montaUsuarioLogado(Profile.MANDATARIA) );
		
		//SalvarLicitacaoException quer dizer que não passou nas validações
		assertThrows( SalvarLicitacaoException.class, () -> licitacaoBC.salvarLotesAssociados(new AssociacaoLicitacaoLoteDTO()) );
	}
	
	private PropostaBD montaProposta(Boolean ehVersaoAtual) {
		PropostaBD prop = new PropostaBD();
		prop.setVersaoAtual(ehVersaoAtual);
		return prop;
	}
	
	private LicitacaoBD montaLicitacao(String situacao) {
		LicitacaoBD lic = new LicitacaoBD();
		lic.setSituacaoDaLicitacao(situacao);
		return lic;
	}
	
	private SiconvPrincipal montaUsuarioLogado(Profile perfil) {
		return new MockSiconvPrincipal(perfil, "1123213");
	}
	
	@Test
	public void meDeAsLicitacoesASeremAtualizadas() {

		Long id615289 = 615289L;
		Long id3325 = 3325L;
		Long id4321 = 4321L;
		Long id98173 = 98173L;
		Long id6475 = 6475L;

		LicitacaoBD licitacaoBancoLocal615289 = new LicitacaoBD();
		licitacaoBancoLocal615289.setIdLicitacaoFk(id615289);
		licitacaoBancoLocal615289.setSituacaoDaLicitacao("EPE");

		LicitacaoBD licitacaoBancoLocal3325 = new LicitacaoBD();
		licitacaoBancoLocal3325.setIdLicitacaoFk(id3325);
		licitacaoBancoLocal3325.setSituacaoDaLicitacao("EPE");

		LicitacaoBD licitacaoBancoLocal4321 = new LicitacaoBD();
		licitacaoBancoLocal4321.setIdLicitacaoFk(id4321);
		licitacaoBancoLocal4321.setSituacaoDaLicitacao("SCP");

		LicitacaoBD licitacaoBancoLocal98173 = new LicitacaoBD();
		licitacaoBancoLocal98173.setIdLicitacaoFk(id98173);
		licitacaoBancoLocal98173.setSituacaoDaLicitacao("HOM");

		List<LicitacaoBD> licitacoesDoBancoLocal = Arrays.asList(licitacaoBancoLocal615289, licitacaoBancoLocal3325,
				licitacaoBancoLocal4321, licitacaoBancoLocal98173);

		LicitacaoIntegracao licitacaoIntegracao615289 = new LicitacaoIntegracao(id615289);
		LicitacaoIntegracao licitacaoIntegracao3325 = new LicitacaoIntegracao(id3325);
		LicitacaoIntegracao licitacaoIntegracao4321 = new LicitacaoIntegracao(id4321);
		LicitacaoIntegracao licitacaoIntegracao6475 = new LicitacaoIntegracao(id6475);

		List<LicitacaoIntegracao> licitacaoesDoServico = Arrays.asList(licitacaoIntegracao615289,
				licitacaoIntegracao3325, licitacaoIntegracao4321, licitacaoIntegracao6475);

		List<LicitacaoBD> licitacoesASeremAtualizadas = licitacaoBC
				.meDeAsLicitacoesASeremAtualizadas(licitacoesDoBancoLocal, licitacaoesDoServico);

		List<Long> licitacoes = licitacoesASeremAtualizadas.stream().map(LicitacaoBD::getIdLicitacaoFk)
				.collect(Collectors.toList());

		assertFalse(licitacoes.isEmpty());
		assertEquals(licitacoes.size(), 3);
		assertTrue(licitacoes.contains(id615289));
		assertTrue(licitacoes.contains(id3325));
		assertTrue(licitacoes.contains(id4321));

	}

	@Test
	public void meDeAsNovasLicitacoes() {

		Long id615289 = 615289L;
		Long id3325 = 3325L;
		Long id4321 = 4321L;
		Long id98173 = 98173L;
		Long id6475 = 6475L;

		LicitacaoBD licitacaoBancoLocal615289 = new LicitacaoBD();
		licitacaoBancoLocal615289.setIdLicitacaoFk(id615289);
		licitacaoBancoLocal615289.setSituacaoDaLicitacao("ELA");

		LicitacaoBD licitacaoBancoLocal3325 = new LicitacaoBD();
		licitacaoBancoLocal3325.setIdLicitacaoFk(id3325);
		licitacaoBancoLocal3325.setSituacaoDaLicitacao("ELA");

		LicitacaoBD licitacaoBancoLocal4321 = new LicitacaoBD();
		licitacaoBancoLocal4321.setIdLicitacaoFk(id4321);
		licitacaoBancoLocal4321.setSituacaoDaLicitacao("SCP");

		LicitacaoBD licitacaoBancoLocal98173 = new LicitacaoBD();
		licitacaoBancoLocal98173.setIdLicitacaoFk(id98173);
		licitacaoBancoLocal98173.setSituacaoDaLicitacao("HOM");

		List<LicitacaoBD> licitacoesDoBancoLocal = Arrays.asList(licitacaoBancoLocal615289, licitacaoBancoLocal3325,
				licitacaoBancoLocal4321, licitacaoBancoLocal98173);

		LicitacaoIntegracao licitacaoIntegracao615289 = new LicitacaoIntegracao(id615289);
		LicitacaoIntegracao licitacaoIntegracao3325 = new LicitacaoIntegracao(id3325);
		LicitacaoIntegracao licitacaoIntegracao4321 = new LicitacaoIntegracao(id4321);
		LicitacaoIntegracao licitacaoIntegracao6475 = new LicitacaoIntegracao(id6475);

		List<LicitacaoIntegracao> licitacaoesDoServico = Arrays.asList(licitacaoIntegracao615289,
				licitacaoIntegracao3325, licitacaoIntegracao4321, licitacaoIntegracao6475);

		PropostaBD proposta = new PropostaBD();
		proposta.setId(99999L);

		List<LicitacaoBD> licitacoesASeremAtualizadas = licitacaoBC.meDeAsNovasLicitacoesDaProposta(proposta,
				licitacoesDoBancoLocal, licitacaoesDoServico);

		List<Long> licitacoes = licitacoesASeremAtualizadas.stream().map(LicitacaoBD::getIdLicitacaoFk)
				.collect(Collectors.toList());

		assertFalse(licitacoes.isEmpty());
		assertEquals(licitacoes.size(), 1);
		assertTrue(licitacoes.contains(id6475));
	}

}
