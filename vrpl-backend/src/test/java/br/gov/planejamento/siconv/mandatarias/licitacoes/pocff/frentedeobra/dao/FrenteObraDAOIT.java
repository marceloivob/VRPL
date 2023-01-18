package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class FrenteObraDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

	private Long idPo;
	private Long servicoId;
	private Long eventoFk;
	private Long macroServicoFk;
	private Long frenteObraFk;

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
	public void inserirFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			assertNotNull(idPo);

			EventoBD evento = handle.attach(EventoDAO.class).inserirEvento(EventoDAOIT.criaEvento(idPo));
			eventoFk = evento.getId();

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));
			macroServicoFk = macro.getId();

			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(eventoFk, macroServicoFk));
			servicoId = servicos.get(0).getId();

			FrenteObraBD frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(DataFactory.criaFrente(idPo));
			frenteObraFk = frente.getId();

			assertNotNull(frente);
		});

	}

	@Test
	@Order(2)
	public void recuperarFrenteObraPorId() {
		FrenteObraBD frente = new FrenteObraBD();
		frente.setId(1L);
		frente.setVersao(1L);

		FrenteObraBD frenteRecuperada = daoFactory.get(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);

		assertNotNull(frenteRecuperada);
	}

	@Test
	@Order(3)
	public void recuperarListaFrentesDeObraIdPo() {
		List<FrenteObraBD> frentes = daoFactory.get(FrenteObraDAO.class).recuperarListaFrentesDeObraIdPo(idPo);

		assertNotNull(frentes);
		assertTrue(frentes.size() > 0);
	}

	@Test
	@Order(5)
	public void recuperarListaFrentesDeObraPorPoIdProposta() {
		List<FrenteObraBD> frentes = daoFactory.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraPorPoIdProposta(idPo);

		assertNotNull(frentes);
		assertTrue(frentes.size() > 0);
	}

	@Test
	@Order(6)
	public void recuperarSequencialFrenteObraPorPo() {
		Long seq = daoFactory.get(FrenteObraDAO.class).recuperarSequencialFrenteObraPorPo(idPo);

		assertNotNull(seq);
	}

	@Test
	@Order(7)
	public void recuperarListaFrentesDeObraIdServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(ServicoFrenteObraDAO.class)
					.inserirServicoFrenteObra(DataFactory.criaServicoFrenteObra(frenteObraFk, servicoId));
			List<FrenteObraBD> frentes = handle.attach(FrenteObraDAO.class)
					.recuperarListaFrentesDeObraIdServico(servicoId);

			assertNotNull(frentes);
			assertTrue(frentes.size() > 0);
		});
	}

	@Test
	@Order(8)
	public void recuperarListaFrentesDeObraPoIdServico() {
		List<FrenteObraBD> frentes = daoFactory.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraPoIdServico(servicoId);

		assertNotNull(frentes);
		assertTrue(frentes.size() > 0);
	}

	@Test
	@Order(9)
	public void recuperarFrentesObraPorNumerosLotes() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);
			licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
			licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			ids = handle.attach(FrenteObraDAO.class).recuperarFrentesObraPorNumerosLotes(LicitacaoDTO.from(licitacao),
					ids);

			assertNotNull(ids);
			assertTrue(ids.size() > 0);
		});
	}

	@Test
	@Order(10)
	public void alterarFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			FrenteObraBD frente = new FrenteObraBD();
			frente.setId(1L);
			frente.setVersao(1L);

			FrenteObraBD frenteRecuperada = handle.attach(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);
			assertEquals("Frente", frenteRecuperada.getNmFrenteObra());
			assertEquals(1L, frenteRecuperada.getVersao());

			frenteRecuperada.setNmFrenteObra("Frente de Obra");
			FrenteObraBD frenteAlterada = handle.attach(FrenteObraDAO.class).alterarFrenteObra(frenteRecuperada);

			assertEquals("Frente de Obra", frenteAlterada.getNmFrenteObra());
			assertEquals(2L, frenteAlterada.getVersao());
		});
	}

	@Test
	@Order(11)
	public void excluirFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(ServicoFrenteObraDAO.class).excluirServicoFrenteObra(frenteObraFk, servicoId);

			FrenteObraBD frente = new FrenteObraBD();
			frente.setId(1L);
			frente.setVersao(2L);

			handle.attach(FrenteObraDAO.class).excluirFrenteObra(frente);

			FrenteObraBD frenteExcluida = handle.attach(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);
			assertNull(frenteExcluida);
		});
	}

	@Test
	@Order(12)
	public void insertFrentesDeObraBatch() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<FrenteObraBD> frentesObra = new ArrayList<>();
			frentesObra.add(DataFactory.criaFrente(idPo));
			Long[] ids = handle.attach(FrenteObraDAO.class).insertFrentesDeObraBatch(frentesObra);

			assertNotNull(ids);
			assertTrue(ids.length > 0);
		});
	}

	@Test
	@Order(13)
	public void excluirFrentesObraPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> ids = new ArrayList<>();
			ids.add(1L);
			handle.attach(FrenteObraDAO.class).excluirFrentesObraPorIds(ids);

			FrenteObraBD frente = new FrenteObraBD();
			frente.setId(1L);
			frente.setVersao(1L);

			FrenteObraBD frenteExcluida = handle.attach(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);
			assertNull(frenteExcluida);
		});
	}

}
