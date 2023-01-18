package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AnexoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private Long idAnexo;

	private Long idLicitacao;

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
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Order(1)
	public void insertAnexo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = handle.attach(LicitacaoDAO.class).insertLicitacao(licitacao);
			idLicitacao = novaLicitacao.getId();
			
			Long id = handle.attach(AnexoDAO.class).insertAnexo(DataFactory.criaAnexo(idLicitacao));

			assertNotNull(id);
		});
	}

	@Test
	@Order(2)
	public void recuperarAnexo() {
		AnexoBD anexo = daoFactory.get(AnexoDAO.class).recuperarAnexo(1L);
		idAnexo = anexo.getIdentificadorDoAnexo();
		assertNotNull(anexo);
	}

	@Test
	@Order(3)
	public void findAnexosByIdLicitacao() {
		List<AnexoBD> anexos = daoFactory.get(AnexoDAO.class).findAnexosByIdLicitacao(1L);
		assertNotNull(anexos);
		assertTrue(anexos.size() == 1);
	}

	@Test
	@Order(4)
	public void findAnexosByTipo() {
		List<AnexoBD> anexos = daoFactory.get(AnexoDAO.class).findAnexosByTipo(1L,
				TipoAnexoEnum.ATA_HOMOLOGACAO_LICITACAO);
		assertNotNull(anexos);
		assertTrue(anexos.size() == 1);
	}

	@Test
	@Order(5)
	public void recuperarOutrosAnexosDaLicitacao() {
		List<AnexoBD> anexos = daoFactory.get(AnexoDAO.class).recuperarOutrosAnexosDaLicitacao(1L, idAnexo);
		assertNotNull(anexos);
		assertTrue(anexos.isEmpty());
	}

	@Test
	@Order(6)
	public void updateAnexo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			AnexoBD anexo = handle.attach(AnexoDAO.class).recuperarAnexo(idAnexo);
			assertEquals("Descricao", anexo.getDescricaoDoAnexo());
			assertEquals(anexo.getVersao(), 1);

			anexo.setDescricaoDoAnexo("Desc");

			AnexoBD anexoAtualizado = handle.attach(AnexoDAO.class).updateAnexo(anexo);

			assertEquals("Desc", anexoAtualizado.getDescricaoDoAnexo());
			assertEquals(anexoAtualizado.getVersao(), 2);
		});
	}

	@Test
	@Order(7)
	public void deleteAnexoPorIdLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			AnexoBD anexo = handle.attach(AnexoDAO.class).recuperarAnexo(1L);

			Boolean excluido = handle.attach(AnexoDAO.class).deleteAnexoPorIdLicitacao(anexo);
			assertTrue(excluido);

			AnexoBD anexoRecuperado = handle.attach(AnexoDAO.class).recuperarAnexo(1L);
			assertNull(anexoRecuperado);
		});
	}

	@Test
	@Order(8)
	public void deleteAnexo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(AnexoDAO.class).insertAnexo(DataFactory.criaAnexo(idLicitacao));

			AnexoBD anexo = handle.attach(AnexoDAO.class).recuperarAnexo(2L);
			assertNotNull(anexo);
			assertEquals(anexo.getVersao(), 1);

			Long idAnexoExcluido = handle.attach(AnexoDAO.class).deleteAnexo(anexo);
			assertNotNull(idAnexoExcluido);
			assertEquals(idAnexoExcluido, 2);

			anexo = handle.attach(AnexoDAO.class).recuperarAnexo(1L);
			assertNull(anexo);
		});
	}

}
