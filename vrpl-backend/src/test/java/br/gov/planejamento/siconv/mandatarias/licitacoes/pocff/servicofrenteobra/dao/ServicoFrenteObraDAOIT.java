package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ServicoFrenteObraDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

	private static Long frenteObraFk;
	private static Long servicoFk;
	private static Long idPo;
	private static Long eventoFk;
	private static Long macroServicoFk;

	private LicitacaoDTO licDto = new LicitacaoDTO();

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
	public void inserirServicoFrenteObra() {
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

			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(eventoFk, macroServicoFk));
			servicoFk = servicos.get(0).getId();

			FrenteObraBD frente = handle.attach(FrenteObraDAO.class).inserirFrenteObra(DataFactory.criaFrente(idPo));
			frenteObraFk = frente.getId();

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);
			licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
			licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());
			LicitacaoDTO.from(licitacao);

			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.inserirServicoFrenteObra(DataFactory.criaServicoFrenteObra(frenteObraFk, servicoFk));

			assertNotNull(sfo);
		});
	}

	@Test
	@Order(2)
	public void recuperarServicoFrenteObraPorId() {
		ServicoFrenteObraBD sfo = daoFactory.get(ServicoFrenteObraDAO.class)
				.recuperarServicoFrenteObraPorId(frenteObraFk, macroServicoFk);

		assertNotNull(sfo);
	}

	@Test
	@Order(3)
	public void recuperarServicoFrenteObraPorIdServico() {
		List<ServicoFrenteObraBD> sfos = daoFactory.get(ServicoFrenteObraDAO.class)
				.recuperarServicoFrenteObraPorIdServico(servicoFk);

		assertNotNull(sfos);
		assertTrue(sfos.size() > 0);
	}

	@Test
	@Order(4)
	public void alterarServicoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.recuperarServicoFrenteObraPorId(frenteObraFk, macroServicoFk);
			assertEquals(BigDecimal.ONE.doubleValue(), sfo.getQtItens().doubleValue());

			sfo.setQtItens(BigDecimal.TEN);

			handle.attach(ServicoFrenteObraDAO.class).alterarServicoFrenteObra(sfo);

			sfo = handle.attach(ServicoFrenteObraDAO.class).recuperarServicoFrenteObraPorId(frenteObraFk,
					macroServicoFk);
			assertEquals(BigDecimal.TEN.doubleValue(), sfo.getQtItens().doubleValue());
		});
	}

	@Test
	@Order(5)
	public void excluirServicoFrenteObra() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(ServicoFrenteObraDAO.class).excluirServicoFrenteObra(frenteObraFk, macroServicoFk);
			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.recuperarServicoFrenteObraPorId(frenteObraFk, macroServicoFk);

			assertNull(sfo);
		});
	}

	@Test
	@Order(6)
	public void excluirServicoFrenteObraPorIdFrente() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			FrenteObraBD frente = new FrenteObraBD();
			frente.setId(frenteObraFk);
			frente = handle.attach(FrenteObraDAO.class).recuperarFrenteObraPorId(frente);

			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.inserirServicoFrenteObra(DataFactory.criaServicoFrenteObra(frenteObraFk, servicoFk));
			handle.attach(ServicoFrenteObraDAO.class).excluirServicoFrenteObraPorIdFrente(frenteObraFk,
					frente.getVersao());
			sfo = handle.attach(ServicoFrenteObraDAO.class).recuperarServicoFrenteObraPorId(frenteObraFk,
					macroServicoFk);

			assertNull(sfo);
		});
	}

	@Test
	@Order(7)
	public void excluirServicosFrenteObraPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			ServicoFrenteObraBD sfo = handle.attach(ServicoFrenteObraDAO.class)
					.inserirServicoFrenteObra(DataFactory.criaServicoFrenteObra(frenteObraFk, servicoFk));

			List<Long> idsServicos = new ArrayList<>();
			idsServicos.add(servicoFk);

			handle.attach(ServicoFrenteObraDAO.class).excluirServicosFrenteObraPorIds(idsServicos);
			sfo = handle.attach(ServicoFrenteObraDAO.class).recuperarServicoFrenteObraPorId(frenteObraFk,
					macroServicoFk);

			assertNull(sfo);
		});
	}

	@Test
	@Order(8)
	public void recuperarServicosFrenteObraPorNumerosLotes() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		ids = daoFactory.get(ServicoFrenteObraDAO.class).recuperarServicosFrenteObraPorNumerosLotes(licDto, ids);

		assertNotNull(ids);
	}

}
