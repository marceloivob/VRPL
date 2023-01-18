package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.UpdatePropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.ObjectDiffer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ClonadorDeLicitacaoBCIT {

	private final static String cpfUsuarioLogado = "00000000000";

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private ClonadorDeProposta clonadorDeProposta;

	private ClonadorDeLicitacaoPorProposta clonadorDeLicitacao;

	private ObjectDiffer objectDiffer;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		objectDiffer = new ObjectDiffer();

		daoFactory.setJdbi(jdbiFactory.createJDBI());
		VersionamentoDAO dao = daoFactory.get(VersionamentoDAO.class);

		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.ACEITAR_DOCUMENTACAO;

		clonadorDeProposta = Mockito.spy(new ClonadorDeProposta(dao, evento));
		clonadorDeLicitacao = Mockito.spy(new ClonadorDeLicitacaoPorProposta(dao, evento));

	}

	@DisplayName("[Clone de Licitação] - Setup do Teste - Insert de Proposta")
	@Test
	@Order(1)
	public void setupDoTesteInsertDeProposta() {
		// Verifica que o banco está vazio antes da realização dos testes
		assertEquals(0, checkCount("vrpl_proposta"));

		PropostaBD propostaInicial = createPropostaInicial();
		assertEquals(propostaInicial.getId(), null);

		PropostaBD propostaInserida = daoFactory.getJdbi().onDemand(UpdatePropostaDAO.class)
				.inserirProposta(propostaInicial);

		assertNotNull(propostaInserida);

		// Após a primeira inserção, só deve existir um registro na tabela VRPL_PROPOSTA
		assertEquals(1, checkCount("vrpl_proposta"));

		assertEquals(propostaInserida.getId(), 1L);
		assertEquals(propostaInserida.getVersaoInAtual(), true);
		assertEquals(propostaInserida.getVersao(), 1L);
		assertEquals(propostaInserida.getVersaoId(), null);
		assertEquals(propostaInserida.getVersaoNr(), 0);
	}

	@DisplayName("[Clone de Licitação] - Setup do Teste - Insert de Licitação")
	@Test
	@Order(2)
	public void setupDoTesteInsertDeLicitacao() {
		// Verifica que o banco está vazio antes da realização dos testes
		assertEquals(0, checkCount("vrpl_licitacao"));

		LicitacaoBD licitacaoInicial = createLicitacaoInicial();
		assertEquals(licitacaoInicial.getId(), null);

		LicitacaoBD licitacaoInserida = daoFactory.getJdbi().onDemand(LicitacaoDAO.class)
				.insertLicitacao(licitacaoInicial);

		assertNotNull(licitacaoInserida);

		// Após a primeira inserção, só deve existir um registro na tabela
		// VRPL_LICITACAO
		assertEquals(1, checkCount("vrpl_licitacao"));

		assertEquals(licitacaoInserida.getId(), 1L);
		assertEquals(licitacaoInserida.getVersao(), 1L);
		assertEquals(licitacaoInserida.getVersaoId(), null);
		assertEquals(licitacaoInserida.getVersaoNr(), 0);
	}

	@DisplayName("[Clone de Licitação] - Setup do Teste - Clone da Proposta")
	@Test
	@Order(3)
	public void setupDoTesteCloneDaProposta() {
		PropostaBD propostaInserida = new PropostaBD();
		propostaInserida.setId(1L);

		Clone<PropostaBD> cloneDeProposta = clonadorDeProposta.clone(propostaInserida);

		// Após a realização do Clone, devem existir 2 registros na tabela VRPL_PROPOSTA
		assertEquals(2, checkCount("vrpl_proposta"));

		assertNotNull(cloneDeProposta.getObjetoOriginal());
		assertNotNull(cloneDeProposta.getObjetoClonado());

		assertEquals(cloneDeProposta.getObjetoOriginal().getId(), 1L);
		assertEquals(cloneDeProposta.getObjetoClonado().getId(), 2L);
	}

	@DisplayName("[Clone de Licitacao] - Caso Feliz")
	@Test
	@Order(4)
	public void licitacaoCasoFeliz() {
		// Pré-Condição para a realização deste teste é que devem existir apenas 1
		// registros na tabela VRPL_LICITACAO que foi inserido pelo teste anterior
		assertEquals(1, checkCount("vrpl_licitacao"));

		// Pré-Condição para realização deste teste é que exista o clone de Proposta
		PropostaBD propostaOriginal = new PropostaBD();
		propostaOriginal.setId(1L);

		PropostaBD propostaClone = new PropostaBD();
		propostaClone.setId(2L);

		Clone<PropostaBD> cloneDeProposta = new Clone<PropostaBD>(propostaOriginal, propostaClone);

		// Faz o clone da Licitação inserida no setup do teste (simulando uma inclusão
		// pelo usuário)
		List<Clone<LicitacaoBD>> cloneDeLicitacao = clonadorDeLicitacao.clone(cloneDeProposta);

		assertEquals(1, cloneDeLicitacao.size());

		assertNotNull(cloneDeLicitacao.get(0));
		assertNotNull(cloneDeLicitacao.get(0).getObjetoOriginal());
		assertNotNull(cloneDeLicitacao.get(0).getObjetoClonado());

		LicitacaoBD licitacaoOriginalRecuperada = daoFactory.getJdbi().onDemand(LicitacaoDAO.class)
				.findLicitacaoById(1L);

		LicitacaoBD licitacaoOriginalInserida = cloneDeLicitacao.get(0).getObjetoOriginal();
		LicitacaoBD licitacaoClonada = cloneDeLicitacao.get(0).getObjetoClonado();

		assertEquals(licitacaoOriginalRecuperada, licitacaoOriginalInserida);

		assertEquals(licitacaoOriginalInserida.getId(), 1L);
		assertEquals(licitacaoClonada.getId(), 2L);

		// Após o clone, devem existir apenas 2 registros na tabela VRPL_LICITACAO
		assertEquals(2, checkCount("vrpl_licitacao"));

		List<Alteracao<?>> mudancasEsperadas = Arrays.asList(//
				new Alteracao<Long>().oCampo("id").mudouDe(1L).para(2L), //
				new Alteracao<Long>().oCampo("identificadorDaProposta").mudouDe(1L).para(2L), //
				new Alteracao<String>().oCampo("versaoId").mudouDe(null).para("1"), //
				new Alteracao<String>().oCampo("versaoNmEvento").mudouDe(null).para("ACT"), //
				new Alteracao<Integer>().oCampo("numeroVersao").mudouDe(0).para(1), //
				new Alteracao<Integer>().oCampo("versaoNr").mudouDe(0).para(1));

		List<Alteracao<?>> alteracoesEncontradas = objectDiffer.extractChanges(licitacaoOriginalInserida,
				licitacaoClonada);

		assertTrue(alteracoesEncontradas.containsAll(mudancasEsperadas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertTrue(mudancasEsperadas.containsAll(alteracoesEncontradas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertEquals(alteracoesEncontradas.size(), mudancasEsperadas.size());
	}

	private Integer checkCount(String nomeDaTabela) {
		return daoFactory.getJdbi().withHandle(handle -> {
			return handle.createQuery("SELECT COUNT(*) FROM siconv." + nomeDaTabela).mapTo(Integer.class).one();
		});
	}

	private PropostaBD createPropostaInicial() {
		PropostaBD proposta = new PropostaBD();
		// Campos alterados durante o versionamento
		proposta.setVersao(1L);
		proposta.setVersaoInAtual(true); // Valor default no insert
		proposta.setVersaoId(null); // Valor default no insert
		proposta.setVersaoNmEvento(null); // Valor default no insert
		proposta.setVersaoNr(0); // Valor default no insert

		// Campos genéricos
		proposta.setAnoConvenio(2020);
		proposta.setAnoProposta(2020);
		proposta.setApelidoEmpreendimento("Empreendimento - Teste Unitário");
		proposta.setCategoria("OBRAS_SERVICOS_ENGENHARIA");
		proposta.setCodigoPrograma("5600020180001");
		proposta.setDataAssinaturaConvenio(LocalDate.now());
		proposta.setIdentificacaoProponente("45774064000188");
		proposta.setIdSiconv(1337197L);
		proposta.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		proposta.setNivelContrato("Nível I");
		proposta.setNomeMandataria("CAIXA ECONOMICA FEDERAL");
		proposta.setNomeObjeto("Construção de Rotatória");
		proposta.setNomePrograma("PLANEJAMENTO  URBANO");
		proposta.setNomeProponente("MUNICIPIO DE RIO CLARO");
		proposta.setNumeroConvenio(1234);
		proposta.setNumeroProposta(35769);
		proposta.setPcMinContrapartida(new BigDecimal("0.1"));
		proposta.setSpaHomologado(true);
		proposta.setUf("PE");
		proposta.setValorContrapartida(new BigDecimal("8786.31"));
		proposta.setValorGlobal(new BigDecimal("231643.45"));
		proposta.setValorRepasse(new BigDecimal("222857.14"));

		return proposta;
	}

	private LicitacaoBD createLicitacaoInicial() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setIdentificadorDaProposta(1L);
		licitacao.setNumeroAno("01/2020");
		licitacao.setObjeto("Aquisição de serviços");
		licitacao.setValorProcessoLicitatorio(new BigDecimal("15123.34"));
		licitacao.setSituacaoDaLicitacao("EPE");
		licitacao.setIdLicitacaoFk(673454L);
		licitacao.setSituacaoDoProcessoLicitatorio("Concluído");
		licitacao.setModalidade("Tomada de Preços");
		licitacao.setRegimeContratacao("Lei 8.666/1993");
		licitacao.setDataPublicacao(LocalDate.of(2018, 10, 26));
		licitacao.setDataHomologacao(LocalDate.of(2018, 12, 04));
		licitacao.setProcessoDeExecucao("Licitação");

		return licitacao;
	}

}