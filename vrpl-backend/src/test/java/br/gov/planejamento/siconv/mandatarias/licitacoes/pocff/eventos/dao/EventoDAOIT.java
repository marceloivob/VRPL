package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class EventoDAOIT {

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

	private Long idPo;
	private Long idEvento;
	private Long idFrente;

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
	public void inserirEvento() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = daoFactory.get(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			EventoBD evento = daoFactory.get(EventoDAO.class).inserirEvento(criaEvento(idPo));

			assertNotNull(evento);
		});

	}

	@Test
	@Order(2)
	public void insertEventosFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			FrenteObraBD frente = DataFactory.criaFrente(idPo);
			frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(frente);
			idFrente = frente.getId();
			idEvento = 1L;

			List<EventoFrenteObraBD> eventosFrenteObra = new ArrayList<>();
			eventosFrenteObra.add(DataFactory.criaEventoFrenteObra(idEvento, idFrente));
			handle.attach(EventoDAO.class).insertEventosFrenteObra(eventosFrenteObra);

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(idEvento,
					idFrente);

			assertNotNull(efo);
		});
	}

	@Test
	@Order(3)
	public void recuperarEventoPorId() {
		EventoBD evento = daoFactory.get(EventoDAO.class).recuperarEventoPorId(1L);

		assertNotNull(evento);
	}

	@Test
	@Order(4)
	public void recuperarListaEventosVRPLIdPo() {
		List<EventoBD> eventos = daoFactory.get(EventoDAO.class).recuperarListaEventosVRPLIdPo(idPo);

		assertNotNull(eventos);
		assertTrue(eventos.size() == 1);
	}

	@Test
	@Order(5)
	public void recuperarSequencialEventoPorPo() {
		Long seq = daoFactory.get(EventoDAO.class).recuperarSequencialEventoPorPo(idPo);

		assertNotNull(seq);
	}

	@Test
	@Order(6)
	public void recuperarIdPropostaAssociadaAoEvento() {
		Long seq = daoFactory.get(EventoDAO.class).recuperarIdPropostaAssociadaAoEvento(idEvento);

		assertNotNull(seq);
	}

	@Test
	@Order(7)
	public void recuperarEventosPorNumerosLotes() {
		daoFactory.get(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
		LicitacaoBD licitacao = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
		licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		ids = daoFactory.get(EventoDAO.class).recuperarEventosPorNumerosLotes(LicitacaoDTO.from(licitacao), ids);

		assertNotNull(ids);
		assertFalse(ids.isEmpty());
	}

	@Test
	@Order(8)
	public void alterarEvento() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			EventoBD evento = handle.attach(EventoDAO.class).recuperarEventoPorId(1L);

			assertEquals("Evento", evento.getNomeEvento());

			evento.setNomeEvento("Evento 1");

			handle.attach(EventoDAO.class).alterarEvento(evento);
			EventoBD eventoAlterado = handle.attach(EventoDAO.class).recuperarEventoPorId(1L);

			assertNotNull(eventoAlterado);
			assertEquals("Evento 1", eventoAlterado.getNomeEvento());
			assertEquals(2L, eventoAlterado.getVersao());
			assertNotSame(evento, eventoAlterado);
		});
	}

	@Test
	@Order(9)
	public void excluirEventoFrenteObraPorEvento() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			EventoBD evento = handle.attach(EventoDAO.class).recuperarEventoPorId(idEvento);

			handle.attach(EventoFrenteObraDAO.class).excluirEventoFrenteObraPorEvento(idEvento, evento.getVersao());
			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(idEvento,
					idFrente);

			assertNull(efo);
		});
	}

	@Test
	@Order(10)
	public void excluirEvento() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(EventoDAO.class).excluirEvento(1L, 2L);
			EventoBD evento = handle.attach(EventoDAO.class).recuperarEventoPorId(1L);

			assertNull(evento);
		});
	}

	@Test
	@Order(11)
	public void insertEventosBatch() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<EventoBD> eventos = new ArrayList<>();
			eventos.add(criaEvento(idPo));

			Long[] ids = handle.attach(EventoDAO.class).insertEventosBatch(eventos);
			idEvento = ids[0];

			assertNotNull(ids);
			assertTrue(ids.length > 0);
		});
	}

	@Test
	@Order(12)
	public void excluirEventosPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> ids = new ArrayList<>();
			ids.add(idEvento);

			handle.attach(EventoDAO.class).excluirEventosPorIds(ids);

			EventoBD evento = handle.attach(EventoDAO.class).recuperarEventoPorId(idEvento);

			assertNull(evento);
		});
	}

	public static EventoBD criaEvento(Long idPo) {
		EventoBD evento = new EventoBD();
		evento.setIdAnalise(1L);
		evento.setIdPo(idPo);
		evento.setNomeEvento("Evento");
		evento.setNumeroEvento(1);

		return evento;
	}

}
