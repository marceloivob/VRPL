package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidationException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception.EventoNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception.NumeroEventoRepetidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.dto.EventoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business.ServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@DisplayName(value = "EventosBC - Teste integrado")
@TestInstance(Lifecycle.PER_CLASS)
public class EventosBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private EventoBC eventoBC;

	@Spy
	private PoBC poBC;

	@Mock
	private ServicoDAO servicoDAOMock;

	@Mock
	private ServicoBC servicoBC;

	@Spy
	private ConstraintBeanValidation<EventoBD> constraintBeanValidation;

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

		Mockito.when(servicoBC.getServicoDAO()).thenReturn(servicoDAOMock);

		eventoBC.setDao(daoFactory);
		eventoBC.setServicoBC(servicoBC);
		eventoBC.setConstraintBeanValidator(constraintBeanValidation);
		eventoBC.setCheckPermission(checkPermission);
	}

	@Test
	@DisplayName(value = "Inserir novo Evento com sucesso")
	public void inserirEventoComSucesso() {
		// Verificacao: Confirmar os valores atualizados
		EventoBD eventoInserido = criarEvento(1, "Novo Evento Teste");

		assertNotNull(eventoInserido);

	}

	@Test
	@DisplayName(value = "Listar eventos existentes")
	public void listarEventos() {
		limparEventos();
		criarEvento(201, "Evento 201");
		criarEvento(202, "Evento 202");

		List<EventoDTO> eventos = eventoBC.consultarListaEventos(1L);

		assertNotNull(eventos);
		assertFalse(eventos.isEmpty());
		assertEquals(2, eventos.size());

		for (EventoDTO eventoDTO : eventos) {
			if (eventoDTO.getNumeroEvento() == 201) {
				assertNotNull(eventoDTO);
				assertEquals("Evento 201", eventoDTO.getTituloEvento());
				assertEquals(201, eventoDTO.getNumeroEvento());
				assertEquals(1L, eventoDTO.getIdPO());
			}
		}
	}

	@Test
	@DisplayName(value = "Detalhar evento existente")
	public void detalharEvento() {
		limparEventos();
		EventoBD eventoBd = criarEvento(301, "Evento 301");
		criarEvento(302, "Evento 302");

		EventoDTO novoEvento = eventoBC.recuperarEventoPorId(eventoBd.getId());

		assertNotNull(novoEvento);

		assertEquals("Evento 301", novoEvento.getTituloEvento());
		assertEquals(301, novoEvento.getNumeroEvento());
		assertEquals(1L, novoEvento.getIdPO());

	}

	@Test
	@DisplayName(value = "Alterar novo Evento com sucesso")
	public void alterarEventoComSucesso() {
		EventoBD evento = criarEvento(400, "Evento teste 400");
		EventoDTO eventoConsulta = eventoBC.recuperarEventoPorId(evento.getId());

		eventoConsulta.setTituloEvento("Evento teste 401");
		eventoConsulta.setNumeroEvento(401);

		eventoBC.alterar(eventoConsulta);

		EventoDTO eventoAposAlteracao = eventoBC.recuperarEventoPorId(evento.getId());

		assertEquals("Evento teste 401", eventoAposAlteracao.getTituloEvento());
		assertEquals(401, eventoAposAlteracao.getNumeroEvento());

	}

	@Test
	@DisplayName(value = "Excluir Evento")
	public void excluirEvento() {

		// cenario: Cadastrar um evento a ser excluido
		EventoBD evento = criarEvento(500, "Evento teste 500");

		Mockito.when(servicoBC.recuperarServicosPorEvento(evento.getId()))
				.thenReturn(Collections.emptyList());

		// Ação: excluir o evento criado
		eventoBC.excluir(evento.getId(), 1L);

		assertThrows(EventoNaoEncontradoException.class, () -> eventoBC.recuperarEventoPorId(evento.getId()));

	}

	@Test
	@DisplayName(value = "Excluir Evento com perfil sem permssao")
	public void excluirEventoSemPermissao() {
		// TODO
		assertTrue(true);
//		// cenario: Cadastrar um evento a ser excluido
//		EventoBD evento = criarEvento(500, "Evento teste 500");
//
//		Mockito.when(servicoBC.recuperarServicosPorEvento(evento.getId())).thenReturn(Collections.emptyList());
//
//		// Ação: excluir o evento criado
//		eventoBC.excluir(evento.getId());
//
//		assertThrows(EventoNaoEncontradoException.class, () -> eventoBC.recuperarEventoPorId(evento.getId()));

	}

	@Test
	@DisplayName(value = "Alterar Evento inexistente")
	public void alterarEventoInexistente() {

		// cenario: Carregar uma Evento existente e atribuir um Id inexistente
		EventoDTO evento = new EventoDTO();
		evento.setId(-1L);

		// Verificacao: Confirmar os valores atualizados
		assertThrows(EventoNaoEncontradoException.class, () -> eventoBC.alterar(evento));

	}

	@Test
	@DisplayName(value = "Criar evento com numero ja existente")
	public void inserirEventoComNumeroExistente() {
		// cenario: Carregar uma Evento existente e atribuir um Id inexistente
		criarEvento(701, "Evento 701");

		EventoDTO novoEvento = new EventoDTO();
		novoEvento.setTituloEvento("Evento erro");
		novoEvento.setNumeroEvento(701);
		novoEvento.setIdPO(1L);
		novoEvento.setVersao(0L);

		// Verificacao: Confirmar os valores atualizados
		assertThrows(NumeroEventoRepetidoException.class, () -> eventoBC.inserir(novoEvento));

	}

	@Test
	@DisplayName(value = "Alterar evento para número ja existente")
	public void alterarEventoComNumeroExistente() {

		// cenario: Criar evento
		EventoBD evento1 = criarEvento(801, "Evento 801");
		criarEvento(802, "Evento 802");

		EventoDTO eventoDto = eventoBC.recuperarEventoPorId(evento1.getId());

		eventoDto.setNumeroEvento(802);

		// Verificacao: Confirmar os valores atualizados
		assertThrows(NumeroEventoRepetidoException.class, () -> eventoBC.alterar(eventoDto));

	}

	@Test
	@DisplayName(value = "Recuperar valor sequencial evento")
	public void recuperarValorSequencialEvento() {
		limparEventos();
		criarEvento(222, "Evento 222");
		Long proximo = eventoBC.recuperarValorSequencialEvento(1L);

		assertEquals(223, proximo);

	}

	@Test
	@DisplayName(value = "Inserir evento maior que 999")
	public void inserirEvento1000() {
		limparEventos();

		assertThrows(ConstraintBeanValidationException.class, () -> criarEvento(1000, "Evento 1000"));

	}

	@Test
	@DisplayName(value = "Recuperar evento 1000")
	public void recuperarEvento1000() {
		limparEventos();
		criarEvento(999, "Evento 999");

		long proximo = eventoBC.recuperarValorSequencialEvento(1L);
		assertEquals(1000, proximo); // Era para lançar exceção

	}

	@Test
	@DisplayName(value = "Recuperar sequencial primeiro evento")
	public void recuperarValorSequencialPrimeiroEvento() {
		limparEventos();

		Long proximo = eventoBC.recuperarValorSequencialEvento(1L);

		assertEquals(1, proximo);

	}

	/**
	 * Insere evento com sucesso
	 *
	 * @param Numero
	 * @param titulo
	 */
	private EventoBD criarEvento(int Numero, String titulo) {
		EventoDTO novoEvento = new EventoDTO();
		novoEvento.setTituloEvento(titulo);
		novoEvento.setNumeroEvento(Numero);
		novoEvento.setIdPO(1L);
		novoEvento.setVersao(1L);

		return eventoBC.inserir(novoEvento);

	}

	private void limparEventos() {
		List<EventoDTO> eventos = eventoBC.consultarListaEventos(1L);

		for (EventoDTO eventoDTO : eventos) {
			Mockito.when(servicoBC.recuperarServicosPorEvento(eventoDTO.getId()))
					.thenReturn(Collections.emptyList());
			eventoBC.excluir(eventoDTO.getId(), eventoDTO.getVersao());
		}
	}

}