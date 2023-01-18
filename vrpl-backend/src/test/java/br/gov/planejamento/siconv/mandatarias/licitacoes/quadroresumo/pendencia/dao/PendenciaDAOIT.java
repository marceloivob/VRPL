package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.dao;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PendenciaDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private Long idLaudo;
	private Long idSubmeta;

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
	public void inserirPendencia() {

		daoFactory.getJdbi().useTransaction(handle -> {
			LicitacaoBD licitacao = DataFactory.criaLicitacao();

			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			Long idLicitacao = novaLicitacao.getId();

			LaudoBD laudo = handle.attach(LaudoDAO.class).inserirLaudo(LaudoDAOIT.criaLaudoEmitidoViavel(idLicitacao));
			idLaudo = laudo.getId();
			
			List<MetaBD> metas = daoFactory.get(QciDAO.class).insertMetas(QciDAOIT.criaMetas());
			Collection<LoteBD> lotes = LoteLicitacaoDAOIT.criaLotes(idLicitacao);
			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(lotes);
			List<SubmetaBD> submetas = handle.attach(QciDAO.class).insertSubmetas(QciDAOIT.criaSubmetas(metas.get(0)));
			idSubmeta = submetas.get(0).getId();
			
			PendenciaBD pend = handle.attach(PendenciaDAO.class).inserirPendencia(criaPendencia(idLaudo));

			assertNotNull(pend);
		});
	}

	@Test
	@Order(2)
	public void recuperarPendenciaPorId() {
		PendenciaBD pend = daoFactory.get(PendenciaDAO.class).recuperarPendenciaPorId(1L);

		assertNotNull(pend);
	}

	@Test
	@Order(3)
	public void alterarPendencia() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PendenciaBD pend = daoFactory.get(PendenciaDAO.class).recuperarPendenciaPorId(1L);
			assertEquals("Desc", pend.getDescricao());

			pend.setDescricao("Descricao");

			handle.attach(PendenciaDAO.class).alterarPendencia(pend);
			pend = daoFactory.get(PendenciaDAO.class).recuperarPendenciaPorId(1L);
			assertEquals("Descricao", pend.getDescricao());
		});
	}

	@Test
	@Order(4)
	public void excluirPendencia() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PendenciaBD pend = handle.attach(PendenciaDAO.class).recuperarPendenciaPorId(1L);
			assertNotNull(pend);

			handle.attach(PendenciaDAO.class).excluirPendencia(1L);

			pend = handle.attach(PendenciaDAO.class).recuperarPendenciaPorId(1L);

			assertNull(pend);
		});
	}

	@Test
	@Order(5)
	public void inserirPendenciaBatch() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);
			List<PendenciaBD> pendencias = new ArrayList<>();
			pendencias.add(criaPendencia(idLaudo));

			handle.attach(PendenciaDAO.class).inserirPendenciaBatch(pendencias);

			PendenciaBD pend = handle.attach(PendenciaDAO.class).recuperarPendenciaPorId(2L);

			assertNotNull(pend);
		});
	}

	private PendenciaBD criaPendencia(Long idLaudo) {
		PendenciaBD p = new PendenciaBD();
		p.setDescricao("Desc");
		p.setInResolvida(Boolean.TRUE);
		p.setPrazo("PCF");
		p.setLaudoFk(idLaudo);
		p.setVersaoNr(1L);
		p.setSubmetaFk(idSubmeta);
		return p;
	}

}
