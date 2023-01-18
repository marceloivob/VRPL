package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MacroServicoParcelaDAOIT {

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

	private Long macroId, idPo;

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
	public void inserirMacroServicoParcela() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));
			macroId = macro.getId();

			MacroServicoParcelaBD msp = handle.attach(MacroServicoParcelaDAO.class)
					.inserirMacroServicoParcela(criaMacroservicoParcela(macroId));

			assertNotNull(msp);
		});

	}

	@Test
	@Order(2)
	public void recuperarMacroServicoParcelaPorId() {
		MacroServicoParcelaBD msp = daoFactory.get(MacroServicoParcelaDAO.class).recuperarMacroServicoParcelaPorId(1L);

		assertNotNull(msp);

	}

	@Test
	@Order(3)
	public void recuperarMacroSearvicoParcelaPorId() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<MacroServicoParcelaBD> msp = daoFactory.get(MacroServicoParcelaDAO.class)
				.recuperarParcelasDoMacroServicoPorIdsMacroServico(ids);

		assertNotNull(msp);
		assertTrue(msp.size() > 0);
	}

	@Test
	@Order(4)
	public void recuperarParcelasDoMacroServicoPorNumerosLotes() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		LicitacaoBD licitacao = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
		licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());
		LicitacaoDTO licDto = new LicitacaoDTO();
		licDto = LicitacaoDTO.from(licitacao);

		List<Long> msp = daoFactory.get(MacroServicoParcelaDAO.class)
				.recuperarParcelasDoMacroServicoPorNumerosLotes(licDto, ids);

		assertNotNull(msp);
		assertTrue(msp.size() > 0);
	}

	@Test
	@Order(5)
	public void alterarMacroServicoParcela() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			MacroServicoParcelaBD msp = handle.attach(MacroServicoParcelaDAO.class)
					.recuperarMacroServicoParcelaPorId(1L);
			assertEquals(BigDecimal.ONE.doubleValue(), msp.getPcParcela().doubleValue());

			msp.setPcParcela(BigDecimal.TEN);
			handle.attach(MacroServicoParcelaDAO.class).alterarMacroServicoParcela(msp);

			msp = handle.attach(MacroServicoParcelaDAO.class).recuperarMacroServicoParcelaPorId(1L);
			assertEquals(BigDecimal.TEN.doubleValue(), msp.getPcParcela().doubleValue());
		});
	}

	@Test
	@Order(6)
	public void excluirMacroServicoParcela() {

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoDetalhadaVRPLDTO po = new PoDetalhadaVRPLDTO();
			po.setId(1L);
			po.setVersao(1L);

			handle.attach(MacroServicoParcelaDAO.class).excluirParcelasDoMacroServico(po);
			MacroServicoParcelaBD msp = handle.attach(MacroServicoParcelaDAO.class)
					.recuperarMacroServicoParcelaPorId(1L);

			assertNull(msp);
		});
	}

	@Test
	@Order(7)
	public void excluirMacroServicoParcelaPorIdsParcela() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			MacroServicoParcelaBD msp = handle.attach(MacroServicoParcelaDAO.class)
					.inserirMacroServicoParcela(criaMacroservicoParcela(macroId));

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			handle.attach(MacroServicoParcelaDAO.class).excluirMacroServicoParcelaPorIdsParcela(ids);

			msp = handle.attach(MacroServicoParcelaDAO.class).recuperarMacroServicoParcelaPorId(1L);

			assertNull(msp);
		});
	}

	@Test
	@Order(8)
	public void inserirMacroServicoParcelaBatch() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));
			macroId = macro.getId();

			List<MacroServicoParcelaBD> lista = new ArrayList<>();
			lista.add(criaMacroservicoParcela(macroId));

			lista = handle.attach(MacroServicoParcelaDAO.class).inserirMacroServicoParcelaBatch(lista);

			assertNotNull(lista);
			assertFalse(lista.isEmpty());
		});
	}

	private MacroServicoParcelaBD criaMacroservicoParcela(Long macroServicoFk) {
		MacroServicoParcelaBD msp = new MacroServicoParcelaBD();
		msp.setMacroServicoFk(macroServicoFk);
		msp.setNrParcela(1);
		msp.setPcParcela(BigDecimal.ONE);
		msp.setVersao(1L);

		return msp;
	}

}
