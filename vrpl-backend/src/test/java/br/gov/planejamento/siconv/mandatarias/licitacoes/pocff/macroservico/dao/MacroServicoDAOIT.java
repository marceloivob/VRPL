package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.PrecoTotalMacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MacroServicoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

	private Long idPo;

	private Long idMacro;

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
	public void inserirMacroServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			PoBD po = DataFactory.criarPO();
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class)
					.inserirMacroServico(DataFactory.criaMacroServico(idPo));

			assertNotNull(macro);
		});
	}

	@Test
	@Order(2)
	public void recuperarMacroServicoPorId() {
		MacroServicoBD macro = daoFactory.get(MacroServicoDAO.class).recuperarMacroServicoPorId(1L);
		idMacro = macro.getId();
		assertNotNull(macro);
	}

	@Test
	@Order(3)
	public void recuperarMacroServicosPorIdPo() {
		List<MacroServicoBD> macros = daoFactory.get(MacroServicoDAO.class).recuperarMacroServicosPorIdPo(idPo);

		assertNotNull(macros);
		assertFalse(macros.isEmpty());
	}

	@Test
	@Order(5)
	public void recuperarPrecoTotalMacroServicos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<ServicoBD> servicos = handle.attach(ServicoDAO.class)
					.inserirServicos(DataFactory.criaServicos(null, idMacro));

			List<Long> ids = new ArrayList<>();
			ids.add(idMacro);

			List<PrecoTotalMacroServicoBD> precos = handle.attach(MacroServicoDAO.class)
					.recuperarPrecoTotalMacroServicos(ids);

			assertNotNull(precos);
			assertFalse(precos.isEmpty());

			ServicoBD servico = servicos.get(0);

			handle.attach(ServicoDAO.class).excluirServico(servico);
		});

	}

	@Test
	@Order(6)
	public void recuperarMacroServicosPorNumerosLotes() {
		daoFactory.get(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
		LicitacaoBD licitacao = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
		licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		ids = daoFactory.get(MacroServicoDAO.class).recuperarMacroServicosPorNumerosLotes(LicitacaoDTO.from(licitacao),
				ids);

		assertNotNull(ids);
		assertFalse(ids.isEmpty());
	}

	@Test
	@Order(7)
	public void alterarMacroServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			MacroServicoBD macro = handle.attach(MacroServicoDAO.class).recuperarMacroServicoPorId(1L);
			assertEquals("Macroservico", macro.getTxDescricao());

			macro.setTxDescricao("Macro");

			handle.attach(MacroServicoDAO.class).alterarMacroServico(macro);

			macro = handle.attach(MacroServicoDAO.class).recuperarMacroServicoPorId(1L);
			assertEquals("Macro", macro.getTxDescricao());
		});
	}

	@Test
	@Order(8)
	public void excluirMacroServico() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(MacroServicoDAO.class).excluirMacroServico(1L);
			MacroServicoBD macro = handle.attach(MacroServicoDAO.class).recuperarMacroServicoPorId(1L);

			assertNull(macro);
		});
	}

	@Test
	@Order(9)
	public void inserirMacrosServicos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<MacroServicoBD> lote = new ArrayList<>();
			lote.add(DataFactory.criaMacroServico(idPo));

			lote = handle.attach(MacroServicoDAO.class).inserirMacrosServicos(lote);
			idMacro = lote.get(0).getId();
			MacroServicoBD macro = handle.attach(MacroServicoDAO.class).recuperarMacroServicoPorId(idMacro);

			assertNotNull(macro);
		});
	}

	@Test
	@Order(10)
	public void excluirMacroServicosPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> ids = new ArrayList<>();
			ids.add(idMacro);

			handle.attach(MacroServicoDAO.class).excluirMacroServicosPorIds(ids);
			MacroServicoBD macro = handle.attach(MacroServicoDAO.class).recuperarMacroServicoPorId(idMacro);

			assertNull(macro);
		});
	}

}
