package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.UpdatePropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.ObjectDiffer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ClonadorDePropostaBCIT {

	private final static String cpfUsuarioLogado = "00000000000";

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private ClonadorDeProposta clonadorDeProposta;

	private ObjectDiffer objectDiffer;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		objectDiffer = new ObjectDiffer();

		daoFactory.setJdbi(jdbiFactory.createJDBI());
		VersionamentoDAO dao = daoFactory.get(VersionamentoDAO.class);

		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.ACEITAR_DOCUMENTACAO;

		clonadorDeProposta = Mockito.spy(new ClonadorDeProposta(dao, evento));
	}

	@DisplayName("[Clone de Proposta] - Caso Feliz - Proposta na Versão Atual deixou de ser versão atual após o clone")
	@Test
	@Order(1)
	public void cenarioCasoFelizPropostaDeixouDeSerVersaoAtualAposClone() {
		// Verifica que o banco está vazio antes da realização dos testes
		assertEquals(0, checkCount());

		PropostaBD propostaInicial = createPropostaInicial();
		assertEquals(propostaInicial.getId(), null);

		PropostaBD propostaInicialComVersaoAtualSim = daoFactory.getJdbi().onDemand(UpdatePropostaDAO.class)
				.inserirProposta(propostaInicial);

		// Após a primeira inserção, só deve existir um registro na tabela VRPL_PROPOSTA
		assertEquals(1, checkCount());

		assertEquals(propostaInicialComVersaoAtualSim.getId(), 1L);
		assertEquals(propostaInicialComVersaoAtualSim.getVersaoAtual(), true);
		assertEquals(propostaInicialComVersaoAtualSim.getVersao(), 1L);
		assertEquals(propostaInicialComVersaoAtualSim.getVersaoId(), null);
		assertEquals(propostaInicialComVersaoAtualSim.getVersaoNr(), 0);

		Clone<PropostaBD> clone = clonadorDeProposta.clone(propostaInicialComVersaoAtualSim);

		// Após a realização do Clone, devem existir 2 registros na tabela VRPL_PROPOSTA
		assertEquals(2, checkCount());

		PropostaBD propostaInicialAtualizadaComVersaoAtualNao = clone.getObjetoOriginal();
		PropostaBD propostaClonadaComVersaoAtualSim = clone.getObjetoClonado();

		assertEquals(propostaInicialAtualizadaComVersaoAtualNao.getId(), 1L);
		assertEquals(propostaInicialAtualizadaComVersaoAtualNao.getVersaoAtual(), false);
		assertEquals(propostaInicialAtualizadaComVersaoAtualNao.getVersao(), 2L); // Houve atualização da versão
		assertEquals(propostaInicialAtualizadaComVersaoAtualNao.getVersaoId(), null);
		assertEquals(propostaInicialAtualizadaComVersaoAtualNao.getVersaoNr(), 0);

		assertEquals(propostaClonadaComVersaoAtualSim.getId(), 2L);
		assertEquals(propostaClonadaComVersaoAtualSim.getVersaoAtual(), true);
		assertEquals(propostaClonadaComVersaoAtualSim.getVersao(), 2L);
		assertEquals(propostaClonadaComVersaoAtualSim.getVersaoId(), "1");
		assertEquals(propostaClonadaComVersaoAtualSim.getVersaoNr(), 1);

		List<Alteracao<?>> mudancasEsperadas = Arrays.asList(//
				new Alteracao<Long>().oCampo("versao").mudouDe(1L).para(2L), //
				new Alteracao<Boolean>().oCampo("versaoAtual").mudouDe(true).para(false), //
				new Alteracao<Boolean>().oCampo("versaoInAtual").mudouDe(true).para(false));

		List<Alteracao<?>> alteracoesEncontradas = objectDiffer.extractChanges(propostaInicialComVersaoAtualSim,
				propostaInicialAtualizadaComVersaoAtualNao);

		assertEquals(alteracoesEncontradas, mudancasEsperadas);
		assertEquals(alteracoesEncontradas.size(), mudancasEsperadas.size());
	}

	@DisplayName("[Clone de Proposta] - Versão de objeto clonado ")
	@Test
	@Order(2)
	public void propostaCasoFeliz() {
		// A Pré-Condição para a realização deste teste é que devem existir apenas 2
		// registros na tabela VRPL_PROPOSTA
		assertEquals(2, checkCount());

		Long idPropostaClonada = 2L;
		PropostaBD propostaOriginalComVersaoAtualSim = daoFactory.getJdbi().onDemand(PropostaDAO.class)
				.loadById(idPropostaClonada);

		Clone<PropostaBD> clone = clonadorDeProposta.clone(propostaOriginalComVersaoAtualSim);

		// Após novo clone, devem existir apenas 3 registros na tabela VRPL_PROPOSTA
		assertEquals(3, checkCount());

		PropostaBD propostaClonada = clone.getObjetoClonado();

		List<Alteracao<?>> mudancasEsperadas = Arrays.asList(//
				new Alteracao<Long>().oCampo("id").mudouDe(2L).para(3L), //
				new Alteracao<Long>().oCampo("versao").mudouDe(2L).para(3L), //
				new Alteracao<String>().oCampo("versaoId").mudouDe(null).para("2"), //
				new Alteracao<Integer>().oCampo("versaoNr").mudouDe(1).para(2));

		List<Alteracao<?>> alteracoesEncontradas = objectDiffer.extractChanges(propostaOriginalComVersaoAtualSim,
				propostaClonada);

		assertTrue(alteracoesEncontradas.containsAll(mudancasEsperadas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertTrue(mudancasEsperadas.containsAll(alteracoesEncontradas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertEquals(alteracoesEncontradas.size(), mudancasEsperadas.size());
	}

	@Test
	@DisplayName("[Clone de Proposta] - Faz um update em Proposta só para mudar o número da versão do controle de concorrência")
	@Order(3)
	public void updateRow() {
		PropostaBD proposta = new PropostaBD();
		proposta.setId(3L);
		proposta.setVersao(3L);

		daoFactory.getJdbi().withHandle(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(cpfUsuarioLogado);

			return handle.createUpdate("UPDATE siconv.vrpl_proposta							" //
					+ "SET versao					 = (:versao + 1),						" //
					+ "    adt_login 				 = current_setting('vrpl.cpf_usuario'),	" //
					+ "    adt_data_hora			 = LOCALTIMESTAMP, 						" //
					+ "    adt_operacao			 	 = 'UPDATE' 							" //
					+ "WHERE id					 	 = :id;									") //
					.bind("versao", proposta.getVersao()) //
					.bind("id", proposta.getId()) //
					.execute();
		});
	}

	@DisplayName("[Clone de Proposta] - Clone do Clone ")
	@Test
	@Order(4)
	public void propostaCloneDoClone() {
		// A Pré-Condição para a realização deste teste é que devem existir apenas 3
		// registros na tabela VRPL_PROPOSTA
		assertEquals(3, checkCount());

		Long idPropostaClonada = 3L;
		PropostaBD propostaClonadaRecuperada = daoFactory.getJdbi().onDemand(PropostaDAO.class)
				.loadById(idPropostaClonada);

		Clone<PropostaBD> cloneDoClone = clonadorDeProposta.clone(propostaClonadaRecuperada);

		// Após novo clone, devem existir apenas 4 registros na tabela VRPL_PROPOSTA
		assertEquals(4, checkCount());

		PropostaBD propostaClonada = cloneDoClone.getObjetoClonado();

		List<Alteracao<?>> mudancasEsperadas = Arrays.asList(//
				new Alteracao<Long>().oCampo("id").mudouDe(3L).para(4L), //
				new Alteracao<Long>().oCampo("versao").mudouDe(4L).para(5L), //
				new Alteracao<String>().oCampo("versaoId").mudouDe(null).para("3"), //
				new Alteracao<Integer>().oCampo("versaoNr").mudouDe(2).para(3));

		List<Alteracao<?>> alteracoesEncontradas = objectDiffer.extractChanges(propostaClonadaRecuperada,
				propostaClonada);

		assertTrue(alteracoesEncontradas.containsAll(mudancasEsperadas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertTrue(mudancasEsperadas.containsAll(alteracoesEncontradas),
				alteracoesEncontradas.toString() + " \n " + mudancasEsperadas.toString());
		assertEquals(alteracoesEncontradas.size(), mudancasEsperadas.size());
	}

	private Integer checkCount() {
		return daoFactory.getJdbi().withHandle(handle -> {
			return handle.createQuery("SELECT COUNT(*) FROM siconv.vrpl_proposta").mapTo(Integer.class).one();
		});
	}

	private PropostaBD createPropostaInicial() {
		PropostaBD proposta = new PropostaBD();
		// Campos alterados durante o versionamento
		proposta.setVersao(1L);
		proposta.setVersaoAtual(true); // Valor default no insert
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

}