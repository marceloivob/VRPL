package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraDetalheBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.TotalizadorMesBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class EventoFrenteObraDAOIT {

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
	public void inserirEventoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			EventoBD evento = EventoDAOIT.criaEvento(idPo);
			evento = handle.attach(EventoDAO.class).inserirEvento(evento);
			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));
			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(evento.getId(), macro.getId()));
			FrenteObraBD frente = DataFactory.criaFrente(idPo);
			frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(frente);
			handle.attach(ServicoFrenteObraDAO.class).inserirServicoFrenteObra(
					DataFactory.criaServicoFrenteObra(frente.getId(), servicos.get(0).getId()));

			eventoFk = evento.getId();
			frenteObraFk = frente.getId();

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class)
					.inserirEventoFrenteObra(DataFactory.criaEventoFrenteObra(evento.getId(), frente.getId()));

			assertNotNull(efo);
		});
	}

	@Test
	@Order(2)
	public void recuperarEventoFrenteObraPorId() {
		EventoFrenteObraBD efo = daoFactory.get(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk,
				frenteObraFk);

		assertNotNull(efo);
	}

	@Test
	@Order(3)
	public void recuperarEventoFrenteObraPorIdEvento() {
		List<EventoFrenteObraBD> efo = daoFactory.get(EventoFrenteObraDAO.class)
				.recuperarEventoFrenteObraPorIdEvento(eventoFk);

		assertNotNull(efo);
		assertFalse(efo.isEmpty());
	}

	@Test
	@Order(5)
	public void recuperarEventosFrenteObraComDetalhe() {
		List<EventoFrenteObraDetalheBD> efo = daoFactory.get(EventoFrenteObraDAO.class)
				.recuperarEventosFrenteObraComDetalhe(idPo);
		assertNotNull(efo);
		assertFalse(efo.isEmpty());
	}

	@Test
	@Order(6)
	public void recuperarTotalizadorPorMes() {
		List<TotalizadorMesBD> tot = daoFactory.get(EventoFrenteObraDAO.class).recuperarTotalizadorPorMes(idPo);
		assertNotNull(tot);
		assertFalse(tot.isEmpty());
	}

	@Test
	@Order(7)
	public void recuperarTodasAsFrentesObraAssociadasAUmEvento() {
		List<EventoFrenteObraDetalheBD> efo = daoFactory.get(EventoFrenteObraDAO.class)
				.recuperarTodasAsFrentesObraAssociadasAUmEvento(eventoFk);

		assertNotNull(efo);
		assertFalse(efo.isEmpty());
	}

	@Test
	@Order(8)
	public void alterarEventoDaFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk,
					frenteObraFk);
			assertEquals(eventoFk, efo.getEventoFk());

			EventoBD evento = EventoDAOIT.criaEvento(idPo);
			evento.setNumeroEvento(2);
			evento = handle.attach(EventoDAO.class).inserirEvento(evento);

			efo.setEventoFk(evento.getId());
			eventoFk = evento.getId();

			handle.attach(EventoFrenteObraDAO.class).alterarEventoDaFrenteObra(efo);
			efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(evento.getId(), frenteObraFk);

			assertNotNull(efo);
		});
	}

	@Test
	@Order(9)
	public void alterarMesConclusaoEventoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk,
					frenteObraFk);
			assertEquals(1, efo.getNrMesConclusao());

			efo.setNrMesConclusao(2);

			handle.attach(EventoFrenteObraDAO.class).alterarMesConclusaoEventoFrenteObra(efo);

			efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk, frenteObraFk);

			assertEquals(2, efo.getNrMesConclusao());
		});
	}

	@Test
	@Order(10)
	public void excluirEventoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(EventoFrenteObraDAO.class).excluirEventoFrenteObra(eventoFk, frenteObraFk);
			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk,
					frenteObraFk);

			assertNull(efo);
		});
	}

	@Test
	@Order(11)
	public void excluirEventosFrenteObraPorIdsEventos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class)
					.inserirEventoFrenteObra(DataFactory.criaEventoFrenteObra(eventoFk, frenteObraFk));

			List<Long> ids = new ArrayList<>();
			ids.add(eventoFk);

			handle.attach(EventoFrenteObraDAO.class).excluirEventosFrenteObraPorIdsEventos(ids);

			efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk, frenteObraFk);

			assertNull(efo);
		});
	}

	@Test
	@Order(12)
	public void excluirEventosFrenteObraPorFrente() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			FrenteObraBD frente = new FrenteObraBD();
			frente.setId(frenteObraFk);

			EventoFrenteObraBD efo = handle.attach(EventoFrenteObraDAO.class)
					.inserirEventoFrenteObra(DataFactory.criaEventoFrenteObra(eventoFk, frenteObraFk));

			frente = handle.attach(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);

			handle.attach(EventoFrenteObraDAO.class).excluirEventoFrenteObraPorFrente(frente.getId(),
					frente.getVersao());

			efo = handle.attach(EventoFrenteObraDAO.class).recuperarEventoFrenteObraPorId(eventoFk, frenteObraFk);

			assertNull(efo);
		});
	}

}
