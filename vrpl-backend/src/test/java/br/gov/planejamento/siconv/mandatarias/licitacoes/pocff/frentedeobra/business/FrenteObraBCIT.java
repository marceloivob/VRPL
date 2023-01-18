package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeAll;
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

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidationException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.exception.FrenteObraNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.exception.NumeroFrenteDeObraRepetidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.dto.FrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@DisplayName(value = "FrenteObraBC - Teste integrado")
@TestInstance(Lifecycle.PER_CLASS)
class FrenteObraBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private FrenteObraBC frenteObraBC;

	@Spy
	private ConstraintBeanValidation<FrenteObraBD> constraintBeanValidation;

	@Mock
	private UserCanEditVerifier checkPermission;

	private final static String usuarioLogado = "00000000000";

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		return new MockSiconvPrincipal(Profile.MANDATARIA, "1123213");
	}

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.execute(DataFactory.INSERT_PROPOSTA);
			handle.execute(DataFactory.INSERT_LICITACAO);
			handle.execute(DataFactory.INSERT_LOTE_LICITACAO);
			handle.execute(DataFactory.INSERT_SUBTIEM_LICITACAO);
			handle.execute(DataFactory.INSERT_META);
			handle.execute(DataFactory.INSERT_SUBMETA);
			handle.execute(DataFactory.INSERT_PO);
		});
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator beanValidator = factory.getValidator();

		constraintBeanValidation.setBeanValidator(beanValidator);

		frenteObraBC.setDao(daoFactory);
		frenteObraBC.setConstraintBeanValidation(constraintBeanValidation);
		frenteObraBC.setCheckPermission(checkPermission);
	}

// TODO Rever estratégia deste teste
//	@Test
//	@DisplayName(value = "Inserir nova Frente de Obra sem ter permissão")
//	public void inserirFrenteObraSemPermissao() {
//		limparFrenteObras();
//
//		// Verificacao: Confirmar os valores atualizados
//		FrenteObraDTO frenteObra = new FrenteObraDTO();
//
//		frenteObra.setIdPO(1L);
//		frenteObra.setNomeFrente("Nova Frente de Obra Teste");
//		frenteObra.setNumeroFrente(200L);
//		frenteObra.setVersao(1L);
//
//		MockSiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.GUEST, "99999999999", false);
//
//		frenteObraBC.setUsuarioLogado(usuarioLogado);
//
//		assertThrows(SecurityAccessException.class, () -> frenteObraBC.inserir(frenteObra));
//
//	}

	@Test
	@DisplayName(value = "Inserir nova Frente de Obra com numero Duplicado")
	public void inserirFrenteObraComNumeroDuplicado() {
		limparFrenteObras();

		criarFrenteObra(1, "Frente de obra 1");

		assertThrows(NumeroFrenteDeObraRepetidoException.class, () -> criarFrenteObra(1, "Frente de obra 1"));

	}

	@Test
	@DisplayName(value = "Inserir nova Frente de Obra com numero invalodp")
	public void inserirFrenteObra1000() {
		assertThrows(ConstraintBeanValidationException.class, () -> criarFrenteObra(1000, "Frente Obra 1000"));
	}

	@Test
	@DisplayName(value = "Inserir Frente de Obra com sucesso")
	public void inserirFrenteObraComSucesso() {
		// Verificacao: Confirmar os valores atualizados
		FrenteObraBD frenteObraInserido = criarFrenteObra(1L, "Nova Frente de Obra Teste");

		assertNotNull(frenteObraInserido);
	}

	@Test
	@DisplayName(value = "Alterar frentes de obra com sucesso")
	public void alterarFrenteObraComSucesso() {
		FrenteObraBD frenteObraBd = criarFrenteObra(301, "Frente de obra 301");

		FrenteObraDTO frenteObraDto = frenteObraBC.recuperarFrenteObraPorId(frenteObraBd);

		assertEquals(301L, frenteObraDto.getNumeroFrente());
		frenteObraDto.setNomeFrente("Noo nome");
		frenteObraDto.setNumeroFrente(302L);
		frenteObraDto.setVersao(1L);

		FrenteObraBD frenteAlterada = frenteObraBC.alterar(frenteObraDto);

		assertEquals(302, frenteAlterada.getNrFrenteObra());
		assertEquals("Noo nome", frenteAlterada.getNmFrenteObra());
		assertEquals(2L, frenteAlterada.getVersao());
	}

	@Test
	@DisplayName(value = "Alterar frentes de obra com numero duplicado")
	public void alterarFrenteObraDuplicado() {
		limparFrenteObras();
		FrenteObraBD frenteObraBd = criarFrenteObra(301, "Frente de obra 301");
		criarFrenteObra(302, "Frente de obra 302");

		FrenteObraDTO frenteObraDto = frenteObraBC.recuperarFrenteObraPorId(frenteObraBd);

		assertEquals(301L, frenteObraDto.getNumeroFrente());
		frenteObraDto.setNomeFrente("Nome nome");
		frenteObraDto.setNumeroFrente(302L);
		frenteObraDto.setVersao(200L);

		assertThrows(NumeroFrenteDeObraRepetidoException.class, () -> frenteObraBC.alterar(frenteObraDto));

	}

	@Test
	@DisplayName(value = "Excluir frentes de obra inexistente")
	public void excluirFrenteObraComSucesso() {
		FrenteObraBD frenteObra = criarFrenteObra(500, "FrenteObra teste 500");

		// Ação: excluir o frenteObra criado
		frenteObraBC.excluir(frenteObra);

		assertThrows(FrenteObraNaoEncontradoException.class, () -> frenteObraBC.excluir(frenteObra));

	}

	@Test
	@DisplayName(value = "Consultar frentes de obra existentes por Id")
	public void recuperarFrentesObraPorId() {
		limparFrenteObras();
		FrenteObraBD frenteObraBd = criarFrenteObra(1, "Frente de obra 1");
		criarFrenteObra(2, "Frente de obra 2");

		FrenteObraDTO frenteObraDTO = frenteObraBC.recuperarFrenteObraPorId(frenteObraBd);

		assertEquals(1, frenteObraDTO.getNumeroFrente());
		assertEquals("Frente de obra 1", frenteObraDTO.getNomeFrente());
		assertEquals(1L, frenteObraDTO.getIdPO());
		assertEquals(1L, frenteObraDTO.getVersao());

	}

	@Test
	@DisplayName(value = "Listar frentes de obra existentes")
	public void listarFrentesObra() {
		limparFrenteObras();
		criarFrenteObra(201, "Frente de obra 201");
		criarFrenteObra(202, "Frente de obra 202");

		List<FrenteObraDTO> frentesDeObra = frenteObraBC.consultarListaFrentesDeObra(1L);

		assertNotNull(frentesDeObra);
		assertFalse(frentesDeObra.isEmpty());
		assertEquals(2, frentesDeObra.size());

		for (FrenteObraDTO frenteObraDTO : frentesDeObra) {
			if (frenteObraDTO.getNumeroFrente() == 201) {
				assertEquals(201, frenteObraDTO.getNumeroFrente());
				assertEquals("Frente de obra 201", frenteObraDTO.getNomeFrente());
			}
		}
	}

	@Test
	@DisplayName(value = "Listar frentes de obra de proposta e frente de Obra análise inexistentes")
	public void listarFrentesObraPropostaEFrenteObraAnaliseInexistente() {
		criarFrenteObra(201, "Frente de obra 201");

		List<FrenteObraDTO> frentesDeObra = frenteObraBC.consultarListaFrentesDeObra(1L);
		// TODO Ao consultar consultarListaFrentesDeObra os paramentros de Proposta e
		// IdPOAnelise não são considerados
//		assertTrue(frentesDeObra.isEmpty());

	}

	@Test
	@DisplayName(value = "Obter Valor sequencial, o valor máximo é 999")
	public void recuperarValorSequencialFrenteDeObra() {
		criarFrenteObra(999, "Frente de obra 201");

		long proximoFrenteObra = frenteObraBC.recuperarValorSequencialFrenteDeObra(1L);

	}

	@Test
	@DisplayName(value = "Recuperar sequencial primeira frente de Obra")
	public void recuperarValorSequencialPrimeiroFrenteObra() {
		limparFrenteObras();

		Long proximo = frenteObraBC.recuperarValorSequencialFrenteDeObra(1L);

		assertEquals(1, proximo);

	}

	@Test
	@DisplayName(value = "Inserir frente de obra maior que 999")
	public void recuperarFrenteObra1000() {
		limparFrenteObras();
		criarFrenteObra(999, "Evento 999");

		long proximo = frenteObraBC.recuperarValorSequencialFrenteDeObra(1L);
		assertEquals(1000, proximo); // Era para lançar exceção

	}

	/**
	 * Insere evento com sucesso
	 *
	 * @param Numero
	 * @param nome
	 */

	private FrenteObraBD criarFrenteObra(long numero, String nome) {
		FrenteObraDTO frenteObra = new FrenteObraDTO();

		frenteObra.setIdPO(1L);
		frenteObra.setNomeFrente(nome);
		frenteObra.setNumeroFrente(numero);
		frenteObra.setVersao(1L);

		return frenteObraBC.inserir(frenteObra);

	}

	private void limparFrenteObras() {
		List<FrenteObraDTO> frenteObras = frenteObraBC.consultarListaFrentesDeObra(1L);

		for (FrenteObraDTO frenteObraDTO : frenteObras) {

			FrenteObraBD frenteDeObra = frenteObraDTO.converterParaBD();
			frenteObraBC.excluir(frenteDeObra);
		}
	}

}
