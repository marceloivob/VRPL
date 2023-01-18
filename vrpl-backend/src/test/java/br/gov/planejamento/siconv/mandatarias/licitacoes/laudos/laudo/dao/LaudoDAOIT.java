package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.ResultadoParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.StatusParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LaudoDAOIT {

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

			handle.execute(DataFactory.INSERT_PROPOSTA);
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Order(1)
	public void inserirLaudo() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			Long idLicitacao = novaLicitacao.getId();
			LaudoBD parecerEmitidoViavel = daoFactory.get(LaudoDAO.class).inserirLaudo(criaLaudoEmitidoViavel(idLicitacao));
			LaudoBD parecerEmitidoInviavel = daoFactory.get(LaudoDAO.class).inserirLaudo(criaLaudoEmitido(idLicitacao));

			assertNotNull(parecerEmitidoViavel);
			assertNotNull(parecerEmitidoInviavel);
		});
	}

	@Test
	@Order(2)
	public void recuperarLaudoPorId() {
		LaudoBD laudo = daoFactory.get(LaudoDAO.class).recuperarLaudoPorId(1L);

		assertNotNull(laudo);
	}

	@Test
	@Order(3)
	public void existeParecerEmitidoViavelParaALicitacao() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);

		List<LaudoBD> relacaoDePareceres = daoFactory.get(LaudoDAO.class)
				.existeParecerEmitidoViavelParaALicitacao(licitacao);

		assertFalse(relacaoDePareceres.isEmpty());
		assertEquals(relacaoDePareceres.size(), 1);

		LaudoBD parecer = relacaoDePareceres.get(0);
		assertNotNull(parecer);
		assertEquals(parecer.getInStatus(), StatusParecerEnum.EMITIDO.getCodigo());
		assertEquals(parecer.getInResultado(), ResultadoParecerEnum.VIAVEL.getSigla());

	}

	@Test
	@Order(4)
	public void existeParecerEmitidoParaALicitacao() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);

		List<LaudoBD> relacaoDePareceres = daoFactory.get(LaudoDAO.class).existeParecerEmitidoParaALicitacao(licitacao);

		assertFalse(relacaoDePareceres.isEmpty());

		assertEquals(relacaoDePareceres.size(), 2);

		LaudoBD parecer = relacaoDePareceres.get(1);
		assertNotNull(parecer);
		assertEquals(parecer.getInStatus(), StatusParecerEnum.EMITIDO.getCodigo());
		assertEquals(parecer.getInResultado(), ResultadoParecerEnum.INVIAVEL.getSigla());

	}

	public static LaudoBD criaLaudoEmitidoViavel(Long idLicitacao) {
		LaudoBD laudo = new LaudoBD();
		laudo.setInResultado(ResultadoParecerEnum.VIAVEL.getSigla());
		laudo.setInStatus(StatusParecerEnum.EMITIDO.getCodigo());
		laudo.setVersaoNr(1L);
		laudo.setLicitacaoFk(idLicitacao);
		laudo.setTemplateFk(1L);
		laudo.setVersao(0L);

		return laudo;
	}

	public static LaudoBD criaLaudoEmitido(Long idLicitacao) {
		LaudoBD laudo = new LaudoBD();
		laudo.setInResultado(ResultadoParecerEnum.INVIAVEL.getSigla());
		laudo.setInStatus(StatusParecerEnum.EMITIDO.getCodigo());
		laudo.setVersaoNr(1L);
		laudo.setLicitacaoFk(idLicitacao);
		laudo.setTemplateFk(1L);
		laudo.setVersao(0L);

		return laudo;
	}

}
