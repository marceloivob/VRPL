package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.MetaQCIExternoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.RegimeExecucaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class QciDAOIT {

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

	private static List<MetaBD> metas;

	private Long submetaId, idPo;

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.openMocks(this);

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
	public void insertMetas() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			metas = daoFactory.get(QciDAO.class).insertMetas(criaMetas());

			assertNotNull(metas);
			assertTrue(metas.size() == 2);
		});
	}

	@Test
	@Order(2)
	public void insertSubmetas() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			Long id = novaLicitacao.getId();

			Collection<LoteBD> lotes = LoteLicitacaoDAOIT.criaLotes(id);
			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(lotes);

			List<SubmetaBD> submetas = handle.attach(QciDAO.class).insertSubmetas(criaSubmetas(metas.get(0)));
			submetaId = submetas.get(0).getId();

			assertNotNull(submetas);
			assertTrue(submetas.size() == 1);

			PoBD po = DataFactory.criarPO();
			po.setSubmetaId(submetaId);
			List<PoBD> list = new ArrayList<>();
			list.add(po);
			List<PoBD> insertPos = handle.attach(PoDAO.class).insertPos(list);
			idPo = insertPos.get(0).getId();
		});
	}

	@Test
	@Order(3)
	public void alterarDadosSubmeta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			SubmetaBD submeta = handle.attach(QciDAO.class).recuperarSubmetaVRPL(submetaId);
			assertTrue(BigDecimal.ONE.compareTo(submeta.getVlRepasse()) == 0);

			submeta.setVlRepasse(BigDecimal.TEN);

			SubmetaBD submetaAlterada = handle.attach(QciDAO.class).alterarDadosSubmeta(submeta);

			assertTrue(BigDecimal.TEN.compareTo(submetaAlterada.getVlRepasse()) == 0);
		});
	}

	@Test
	@Order(5)
	public void insertSubitensInvestimento() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<SubitemInvestimentoBD> subitens = handle.attach(QciDAO.class)
					.insertSubitensInvestimento(criaSubitens());

			assertNotNull(subitens);
			assertTrue(subitens.size() == 2);
		});
	}

	@Test
	@Order(6)
	public void recuperarSubitemPorFkAnalise() {
		List<Long> idsSubitemAnalise = new ArrayList<>();
		idsSubitemAnalise.add(2L);

		List<SubitemInvestimentoBD> subitens = daoFactory.get(QciDAO.class)
				.recuperarSubitemPorFkAnalise(idsSubitemAnalise);

		assertNotNull(subitens);
		assertTrue(subitens.size() == 1);
	}

	@Test
	@Order(7)
	public void recuperarSubmetaVRPL() {
		SubmetaBD submeta = daoFactory.get(QciDAO.class).recuperarSubmetaVRPL(submetaId);

		assertNotNull(submeta);
	}

	@Test
	@Order(9)
	public void recuperarIdsMetasNaoRelacionadas() {
		List<Long> ids = daoFactory.get(QciDAO.class).recuperarIdsMetasNaoRelacionadas();

		assertNotNull(ids);
	}

	@Test
	@Order(10)
	public void recuperarMetaPorFkAnalise() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<MetaBD> metas = daoFactory.get(QciDAO.class).recuperarMetaPorFkAnalise(ids);

		assertNotNull(metas);
		assertFalse(metas.isEmpty());
	}

	@Test
	@Order(11)
	public void recuperarQCIInternoByLicitacaoFk() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			List<MetaBD> metas = handle.attach(QciDAO.class).recuperarQCIInternoByIdLicitacao(licitacao.getId());

			assertNotNull(metas);
			assertFalse(metas.isEmpty());
		});
	}

	@Test
	@Order(12)
	public void recuperarQCIExternoByPropFk() {
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, 1L);

		List<MetaQCIExternoDTO> metas = daoFactory.get(QciDAO.class)
				.recuperarMetasAtivasParaQCIExternoByPropFk(usuarioLogado, 0L);

		assertNotNull(metas);
		assertFalse(metas.isEmpty());
	}

	@Test
	@Order(13)
	public void recuperarIdsSubmetasPorNumerosLotes() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);
			licitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.EM_ANALISE.getSigla());
			licitacao.setSituacaoDoProcessoLicitatorio(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			ids = handle.attach(QciDAO.class).recuperarIdsSubmetasPorNumerosLotes(LicitacaoDTO.from(licitacao), ids);

			assertNotNull(ids);
			assertFalse(ids.isEmpty());
		});
	}

	@Test
	@Order(14)
	public void excluirSubmetaPorIds() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			List<Long> ids = new ArrayList<>();
			ids.add(1L);

			handle.attach(QciDAO.class).excluirSubmetaPorIds(ids);

			SubmetaBD submeta = handle.attach(QciDAO.class).recuperarSubmetaVRPL(1L);
			assertNull(submeta);
		});
	}

	@Test
	@Order(15)
	public void removerMetasVRPLSemFilhos() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(QciDAO.class).insertMetas(criaMetas());
			List<Long> metas = handle.attach(QciDAO.class).recuperarIdsMetasNaoRelacionadas();

			assertNotNull(metas);
			assertFalse(metas.isEmpty());

			handle.attach(QciDAO.class).removerMetasVRPLSemFilhos();

			metas = handle.attach(QciDAO.class).recuperarIdsMetasNaoRelacionadas();

			assertNotNull(metas);
			assertTrue(metas.isEmpty());
		});
	}

	@Test
	@Order(16)
	public void recuperarSubmetasDaLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(LoteLicitacaoDAOIT.criaLotes(1L));
			metas = handle.attach(QciDAO.class).insertMetas(QciDAOIT.criaMetas());
			handle.attach(QciDAO.class).insertSubmetas(QciDAOIT.criaSubmetas(metas.get(0)));
			LicitacaoBD licitacao = handle.attach(LicitacaoDAO.class).findLicitacaoById(1L);
			List<SubmetaBD> submetas = handle.attach(QciDAO.class).recuperarSubmetasDaLicitacao(licitacao);

			assertNotNull(submetas);
			assertFalse(submetas.isEmpty());
		});
	}

	private Collection<SubitemInvestimentoBD> criaSubitens() {
		SubitemInvestimentoBD subitem = new SubitemInvestimentoBD();
		subitem.setCodigoUnd("UN");
		subitem.setDescricao("Subitem");
		subitem.setDescricaoUnd("Unidade");
		subitem.setIdSubitemAnalise(1L);
		subitem.setInProjetoSocial("S");

		SubitemInvestimentoBD subitem2 = new SubitemInvestimentoBD();
		subitem2.setCodigoUnd("UN");
		subitem2.setDescricao("Subitem 2");
		subitem2.setDescricaoUnd("Unidade 2");
		subitem2.setIdSubitemAnalise(2L);
		subitem2.setInProjetoSocial("N");

		Collection<SubitemInvestimentoBD> subitens = new ArrayList<>();
		subitens.add(subitem);
		subitens.add(subitem2);

		return subitens;
	}

	public static Collection<SubmetaBD> criaSubmetas(MetaBD meta) {
		SubmetaBD sub1 = new SubmetaBD();
		sub1.setDescricao("Desc 1");
		sub1.setIdSubmetaAnalise(1L);
		sub1.setItemPad(1L);
		sub1.setIdProposta(1L);
		sub1.setLote(1L);
		sub1.setNumero(1L);
		sub1.setRegimeExecucao(RegimeExecucaoEnum.EPG);
		sub1.setRegimeExecucaoAnalise("Fit");
		sub1.setSituacao("ELA");
		sub1.setSituacaoAnalise("ELA");
		sub1.setVlContrapartida(BigDecimal.ONE);
		sub1.setVlContrapartidaAnalise(BigDecimal.ONE);
		sub1.setVlOutros(BigDecimal.ONE);
		sub1.setVlOutrosAnalise(BigDecimal.ONE);
		sub1.setVlRepasse(BigDecimal.ONE);
		sub1.setVlRepasseAnalise(BigDecimal.ONE);
		sub1.setVlTotalAnalise(BigDecimal.ONE);
		sub1.setVlTotalLicitado(BigDecimal.ONE);
		sub1.setVrplLoteLicitacaoFk(1L);

		sub1.setIdMeta(meta.getId());
		sub1.setMetaBD(meta);

		Collection<SubmetaBD> submetas = new ArrayList<>();
		submetas.add(sub1);
		return submetas;
	}

	public static Collection<MetaBD> criaMetas() {
		MetaBD meta = new MetaBD();
		meta.setDescricao("Meta 1");
		meta.setIdMetaAnalise(1L);
		meta.setNumero(1L);
		meta.setQuantidade(BigDecimal.ONE);
		meta.setSocial(Boolean.TRUE);

		MetaBD meta2 = new MetaBD();
		meta2.setDescricao("Meta 2");
		meta2.setIdMetaAnalise(2L);
		meta2.setNumero(2L);
		meta2.setQuantidade(BigDecimal.ONE);
		meta2.setSocial(Boolean.TRUE);

		Collection<MetaBD> metas = new ArrayList<>();
		metas.add(meta);
		metas.add(meta2);
		return metas;
	}

}
