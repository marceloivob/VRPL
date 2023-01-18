package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoteLicitacaoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private static ConcreteDAOFactory daoFactory;

	private static Long id;

	private final static String usuarioLogado = "00000000000";

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.execute(DataFactory.dependenciasPo());
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Order(1)
	public void insertLotesLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			id = novaLicitacao.getId();

			List<LoteBD> lotes = daoFactory.get(LoteLicitacaoDAO.class).insertLotesLicitacao(criaLotes(id));

			assertNotNull(lotes);
			assertFalse(lotes.isEmpty());
			assertEquals(lotes.size(), 2);

			for (LoteBD lote : lotes) {
				assertNotNull(lote);
				assertNotNull(lote.getId());
			}
		});
	}

	@Test
	@Order(2)
	public void findLotesByIdLicitacaoFK() {
		List<LoteBD> lotes = daoFactory.get(LoteLicitacaoDAO.class).findLotesByIdLicitacao(id);
		assertTrue(lotes.size() == 2);
	}

	@Test
	@Order(3)
	public void findLotesSemSubmeta() {
		List<LoteBD> lotes = daoFactory.get(LoteLicitacaoDAO.class).findLotesSemSubmeta(id);
		assertTrue(lotes.size() == 2);
	}

	@Test
	@Order(4)
	public void recuperarLotePorPo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);

			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);

			LoteBD lote = handle.attach(LoteLicitacaoDAO.class).recuperarLotePorPo(insertPos.get(0).getId());

			assertNotNull(lote);
		});
	}

	@Test
	@Order(5)
	public void deleteLoteLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> ids = new ArrayList<>();
			ids.add(1L);
			ids.add(2L);
			handle.attach(LoteLicitacaoDAO.class).deleteLoteLicitacao(id, ids);

			List<LoteBD> lotes = handle.attach(LoteLicitacaoDAO.class).findLotesByIdLicitacao(id);
			assertTrue(lotes.isEmpty());
		});
	}

	public static Collection<LoteBD> criaLotes(Long id) {
		LoteBD lote1 = new LoteBD();
		lote1.setIdentificadorDaLicitacao(id);
		lote1.setNumeroDoLote(1L);

		LoteBD lote2 = new LoteBD();
		lote2.setIdentificadorDaLicitacao(id);
		lote2.setNumeroDoLote(2L);

		Collection<LoteBD> lotes = new ArrayList<>();
		lotes.add(lote1);
		lotes.add(lote2);

		return lotes;
	}

}
