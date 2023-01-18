package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@DisplayName(value = "Testes da PoDAO")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PoDAOIT {
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

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

			handle.execute(DataFactory.dependenciasPo());
		});

	}

	@Test
	@Order(1)
	public void insertPos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);

			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);

			assertNotNull(insertPos);
			assertTrue(insertPos.size() == 1);
			assertTrue(insertPos.get(0).getId() == 1);
		});

	}

	@Test
	@Order(2)
	public void recuperarPoPorId() {

		PoBD po = daoFactory.get(PoDAO.class).recuperarPoPorId(1L);

		assertNotNull(po);
		assertTrue(po.getIdPoAnalise() == 1);
		assertTrue(po.getSubmetaId() == 1);
		assertTrue(po.getSgLocalidade().equals("PE"));

		assertTrue(po.getInAcompanhamentoEventos());
		assertTrue(po.getInDesonerado());
		assertTrue(po.getQtMesesDuracaoObra() == 3);

	}

	@Test
	@Order(3)
	public void alterarDadosPo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = handle.attach(PoDAO.class).recuperarPoPorId(1L);

			po.setQtMesesDuracaoObra(10L);

			handle.attach(PoDAO.class).alterarDadosPo(po);

			po = handle.attach(PoDAO.class).recuperarPoPorId(1L);

			assertNotNull(po);
			assertTrue(po.getIdPoAnalise() == 1);
			assertTrue(po.getSubmetaId() == 1);
			assertTrue(po.getQtMesesDuracaoObra().equals(10L));

			assertTrue(po.getInAcompanhamentoEventos());
			assertTrue(po.getInDesonerado());
			assertTrue(po.getVersao() == 2);
			assertTrue(po.getQtMesesDuracaoObra() == 10);
		});

	}

	@Test
	@Order(5)
	public void recuperarPosPorLicitacao() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setVersao(0L);

		List<PoVRPLDTO> pos = daoFactory.get(PoDAO.class).recuperarPosPorLicitacao(licitacao);

		assertNotNull(pos);
		assertTrue(pos.size() == 1);
		assertTrue(pos.get(0).getId() == 1);
	}

	@Test
	@Order(6)
	public void recuperarPosPorLicitacaoELote() {
		List<PoBD> pos = daoFactory.get(PoDAO.class).recuperarPosPorLicitacaoELote(1L, 1L);

		assertNotNull(pos);
		assertTrue(pos.size() == 1);
		assertTrue(pos.get(0).getId() == 1);
	}

	@Test
	@Order(7)
	public void recuperarIdsPOPorNumerosLotes() {
		List<Long> listLotes = new ArrayList<>();
		listLotes.add(1L);
		LicitacaoDTO licitacao = new LicitacaoDTO();
		licitacao.setId(1L);
		licitacao.setVersaoAtual(true);

		List<Long> ids = daoFactory.get(PoDAO.class).recuperarIdsPOPorNumerosLotes(licitacao, listLotes);

		assertNotNull(ids);
		assertTrue(ids.size() == 1);
		assertTrue(ids.get(0) == 1);
	}

	@Test
	@Order(8)
	public void recuperarIdPoPorSubmeta() {
		Long idPO = daoFactory.get(PoDAO.class).recuperarIdPoPorSubmeta(1L);
		assertNotNull(idPO);
		assertTrue(idPO == 1);
	}

	@Test
	@Order(9)
	public void recuperarPoVRPL() {
		PoBD po = daoFactory.get(PoDAO.class).recuperarPoVRPL(1L);

		assertNotNull(po);
	}

	@Test
	@Order(10)
	public void recuperarValorTotalAnalise() {
		BigDecimal valor = daoFactory.get(PoDAO.class).recuperarValorTotalAnalise(1L);

		assertNotNull(valor);
	}

	@Test
	@Order(11)
	public void recuperarValorTotalVRPL() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(1L));
			FrenteObraBD frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(DataFactory.criaFrente(1L));
			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(null, macro.getId()));

			BigDecimal valor = handle.attach(PoDAO.class).recuperarValorTotalVRPL(1L);

			assertNotNull(valor);

			handle.attach(ServicoDAO.class).excluirServico(servicos.get(0));
			handle.attach(FrenteObraDAO.class).excluirFrenteObra(frente);
			handle.attach(MacroServicoDAO.class).excluirMacroServico(macro.getId());
		});
	}

	@Test
	@Order(12)
	public void alterarReferenciaPo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = handle.attach(PoDAO.class).recuperarPoPorId(1L);

			assertEquals("analise", po.getReferencia());

			po.setReferencia("database");

			handle.attach(PoDAO.class).alterarReferenciaPo(po);

			po = handle.attach(PoDAO.class).recuperarPoPorId(1L);

			assertEquals("database", po.getReferencia());
		});

	}

	@Test
	@Order(13)
	public void recuperarSubmetaPorPO() {

		SubmetaBD submeta = daoFactory.get(PoDAO.class).recuperarSubmetaPorPO(1L);

		assertNotNull(submeta);

	}

}
