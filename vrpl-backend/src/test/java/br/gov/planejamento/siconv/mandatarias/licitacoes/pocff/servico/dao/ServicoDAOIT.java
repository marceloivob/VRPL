package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ServicoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

	private Long idPo;

	private Long eventoFk;

	private Long macroServicoFk;

	private Long frenteObraFk;

	private ServicoBD servico;

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

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Order(1)
	public void inserirServicos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			EventoBD evento = handle.attach(EventoDAO.class).inserirEvento(EventoDAOIT.criaEvento(idPo));
			eventoFk = evento.getId();

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));
			macroServicoFk = macro.getId();

			FrenteObraBD frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(DataFactory.criaFrente(idPo));
			frenteObraFk = frente.getId();

			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(eventoFk, macroServicoFk));
			servico = servicos.get(0);

			assertNotNull(servicos);
			assertTrue(servicos.size() > 0);
		});
	}

	@Test
	@Order(2)
	public void recuperarServicoPorId() {

		ServicoBD servicoRecuperado = daoFactory.get(ServicoDAO.class).recuperarServicoPorId(servico.getId());

		assertNotNull(servicoRecuperado);
	}

	@Test
	@Order(3)
	public void recuperarServicoPorMacroServico() {
		List<ServicoBD> servicos = daoFactory.get(ServicoDAO.class).recuperarServicoPorMacroServico(macroServicoFk);

		assertNotNull(servicos);
		assertTrue(servicos.size() > 0);
	}

	@Test
	@Order(4)
	public void recuperarServicosPorEvento() {
		List<ServicoBD> servicos = daoFactory.get(ServicoDAO.class).recuperarServicosPorEvento(eventoFk);

		assertNotNull(servicos);
		assertTrue(servicos.size() > 0);
	}

	@Test
	@Order(5)
	public void recuperarServicosPorPo() {
		PoVRPLDTO po = new PoVRPLDTO();
		po.setId(idPo);

		List<ServicoBD> servicos = daoFactory.get(ServicoDAO.class).recuperarServicosPorPo(po);

		assertNotNull(servicos);
		assertTrue(servicos.size() > 0);
	}

	@Test
	@Order(6)
	public void alterarServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoBD servicoRecuperado = handle.attach(ServicoDAO.class).recuperarServicoPorId(servico.getId());
			assertEquals(BigDecimal.ONE.doubleValue(), servico.getPcBdiLicitado().doubleValue());

			servicoRecuperado.setPcBdiLicitado(BigDecimal.TEN);

			ServicoBD servicoAlterado = handle.attach(ServicoDAO.class).alterarServico(servicoRecuperado);

			assertNotNull(servicoAlterado);
			assertTrue(BigDecimal.TEN.compareTo(servicoAlterado.getPcBdiLicitado()) == 0);
			assertEquals(2L, servicoAlterado.getVersao());
		});
	}

	@Test
	@Order(7)
	public void insertServicoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoFrenteObraBD sfo = DataFactory.criaServicoFrenteObra(frenteObraFk, servico.getId());

			List<ServicoFrenteObraBD> lis = new ArrayList<>();
			lis.add(sfo);

			handle.attach(ServicoDAO.class).insertServicoFrenteObra(lis);
			lis = handle.attach(ServicoDAO.class).recuperaListaServicoFrenteObraPorIdServico(servico.getId());

			assertNotNull(lis);
			assertFalse(lis.isEmpty());
		});
	}

	@Test
	@Order(8)
	public void recuperaListaServicoFrenteObraPorIdServico() {
		List<ServicoFrenteObraBD> lis = daoFactory.get(ServicoDAO.class)
				.recuperaListaServicoFrenteObraPorIdServico(servico.getId());

		assertNotNull(lis);
		assertFalse(lis.isEmpty());
	}

	@Test
	@Order(9)
	public void recuperarServicosPorNumerosLotes() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);
			licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
			licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			List<Long> lis = handle.attach(ServicoDAO.class)
					.recuperarServicosPorNumerosLotes(LicitacaoDTO.from(licitacao), ids);

			assertNotNull(lis);
			assertFalse(lis.isEmpty());
		});
	}

	@Test
	@Order(10)
	public void alterarServicoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.recuperarServicoFrenteObraPorId(frenteObraFk, servico.getId());
			assertEquals(BigDecimal.ONE.doubleValue(), sfo.getQtItens().doubleValue());

			sfo.setQtItens(BigDecimal.TEN);
			List<ServicoFrenteObraBD> col = new ArrayList<>();
			col.add(sfo);

			handle.attach(ServicoDAO.class).alterarServicoFrenteObra(col);
			col = handle.attach(ServicoDAO.class).recuperaListaServicoFrenteObraPorIdServico(servico.getId());

			assertNotNull(col);
			assertEquals(BigDecimal.TEN.doubleValue(), col.get(0).getQtItens().doubleValue());
		});
	}

	@Test
	@Order(11)
	public void excluirServicoFrenteObraPorServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(ServicoDAO.class).excluirServicoFrenteObraPorServico(servico.getId());
			List<ServicoFrenteObraBD> col = handle.attach(ServicoDAO.class)
					.recuperaListaServicoFrenteObraPorIdServico(servico.getId());

			assertTrue(col.isEmpty());
		});
	}

	@Test
	@Order(12)
	public void excluirServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoBD servico = new ServicoBD();
			servico.setId(1L);
			servico.setVersao(2L);

			handle.attach(ServicoDAO.class).excluirServico(servico);

			ServicoBD servicoRecuperado = handle.attach(ServicoDAO.class).recuperarServicoPorId(servico.getId());

			assertNull(servicoRecuperado);
		});
	}

	@Test
	@Order(13)
	public void excluirServicosPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(ServicoDAO.class).inserirServicos(DataFactory.criaServicos(eventoFk, macroServicoFk));

			List<Long> ids = new ArrayList<>();
			ids.add(servico.getId());

			handle.attach(ServicoDAO.class).excluirServicosPorIds(ids);
			ServicoBD servicoRecuperado = handle.attach(ServicoDAO.class).recuperarServicoPorId(servico.getId());

			assertNull(servicoRecuperado);
		});
	}

}
