package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LicitacaoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private Long id;

	private List<MetaBD> metas;

	private List<SubmetaBD> submetas;

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
	public void insertLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD novo = DataFactory.criaLicitacao();

			LicitacaoBD inserido = handle.attach(LicitacaoDAO.class).insertLicitacao(novo);
			assertNotNull(inserido);

			assertNotNull(inserido.getId());

			id = inserido.getId();

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			metas = handle.attach(QciDAO.class).insertMetas(QciDAOIT.criaMetas());
			submetas = handle.attach(QciDAO.class).insertSubmetas(QciDAOIT.criaSubmetas(metas.get(0)));
		});
	}

	@Test
	@Order(2)
	public void insertLicitacaoEmLote() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD a = DataFactory.criaLicitacao();
			LicitacaoBD b = DataFactory.criaLicitacao();
			LicitacaoBD c = DataFactory.criaLicitacao();
			List<LicitacaoBD> l = Arrays.asList(a, b, c);

			List<LicitacaoBD> resultado = handle.attach(LicitacaoDAO.class).insertLicitacao(l);
			assertNotNull(resultado);
			assertFalse(resultado.isEmpty());
			assertEquals(resultado.size(), 3);
		});
	}

	@Test
	@Order(3)
	public void findLicitacaoById() {
		LicitacaoBD licitacao = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(id);
		assertNotNull(licitacao);
		assertEquals("00012019", licitacao.getNumeroAno());
	}

	@Test
	@Order(4)
	public void updateLicitacaoEmLote() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD a = DataFactory.criaLicitacao();
			a.setId(1L);
			a.setNumeroVersao(1);
			a.setVersao(1L);
			a.setNumeroAno("0123/2019");

			LicitacaoBD b = DataFactory.criaLicitacao();
			b.setId(2L);
			b.setNumeroVersao(1);
			b.setVersao(1L);
			b.setNumeroAno("0123/2020");

			LicitacaoBD c = DataFactory.criaLicitacao();
			c.setId(3L);
			c.setNumeroVersao(1);
			c.setVersao(1L);
			c.setNumeroAno("0123/2021");

			List<LicitacaoBD> l = Arrays.asList(a, b, c);

			handle.attach(LicitacaoDAO.class).atualizaLicitacao(l);

			LicitacaoBD licitacaoA = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(1L);
			LicitacaoBD licitacaoB = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(2L);
			LicitacaoBD licitacaoC = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(3L);

			assertNotNull(licitacaoA);
			assertNotNull(licitacaoB);
			assertNotNull(licitacaoC);

			assertEquals(licitacaoA.getNumeroAno(), "0123/2019");
			assertEquals(licitacaoB.getNumeroAno(), "0123/2020");
			assertEquals(licitacaoC.getNumeroAno(), "0123/2021");

		});
	}

	@Test
	@Order(6)
	public void recuperarLicitacoesAssociadasDaProposta() {
		PropostaBD proposta = new PropostaBD();
		proposta.setId(1L);
		proposta.setVersao(1L);

		List<LicitacaoBD> lics = daoFactory.get(LicitacaoDAO.class)
				.recuperarLicitacoesAssociadasDaProposta(proposta, true);

		assertNotNull(lics);
		assertFalse(lics.isEmpty());
	}

	@Test
	@Order(8)
	public void recuperarValorRepassePorListaLicitacoes() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		BigDecimal valor = daoFactory.get(LicitacaoDAO.class).recuperarValorRepassePorListaLicitacoes(ids);

		assertNotNull(valor);
	}

	@Test
	@Order(9)
	public void updateSituacaoDaLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(id);
			assertEquals("EPE", licitacao.getSituacaoDaLicitacao());

			licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());

			handle.attach(LicitacaoDAO.class).updateSituacaoDaLicitacao(licitacao);

			licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(id);
			assertEquals("COM", licitacao.getSituacaoDaLicitacao());
		});
	}

	@Test
	@Order(10)
	public void updateSituacaoSubmeta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			SubmetaBD submeta = handle.attach(QciDAO.class).recuperarSubmetaVRPL(submetas.get(0).getId());
			assertEquals("EPE", submeta.getSituacao());

			List<SubmetaBD> submetas = Arrays.asList(submeta);

			HistoricoLicitacaoBD novoHistorico = new HistoricoLicitacaoBD();
			novoHistorico.setIdentificadorDaLicitacao(1L);
			novoHistorico.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());

			boolean encontrado = handle.attach(LicitacaoDAO.class).updateSituacaoSubmetasAssociadasDaLicitacao(novoHistorico);

			assertTrue(encontrado);

			SubmetaBD submetaRecuperadaAposAlteracao = handle.attach(QciDAO.class)
					.recuperarSubmetaVRPL(submetas.get(0).getId());
			assertEquals("COM", submetaRecuperadaAposAlteracao.getSituacao());
		});
	}

	@Test
	@Order(11)
	public void delete() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> idsSubmetas = new ArrayList<>();
			for (SubmetaBD sub : submetas) {
				idsSubmetas.add(sub.getId());
			}

			handle.attach(QciDAO.class).excluirSubmetaPorIds(idsSubmetas);
			handle.attach(QciDAO.class).removerMetasVRPLSemFilhos();

			List<Long> idsLotes = new ArrayList<>();
			idsLotes.add(1L);
			idsLotes.add(2L);
			handle.attach(LoteLicitacaoDAO.class).deleteLoteLicitacao(1L, idsLotes);

			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(id);
			assertNotNull(licitacao);

			handle.attach(LicitacaoDAO.class).delete(id);

			licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(id);
			assertNull(licitacao);
		});
	}

}
