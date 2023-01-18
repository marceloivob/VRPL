package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collection;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAOIT;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class RespostaDAOIT {

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

	@Test
	@Order(1)
	public void inserirResposta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			Long idLicitacao = novaLicitacao.getId();

			LaudoBD laudo = daoFactory.get(LaudoDAO.class).inserirLaudo(LaudoDAOIT.criaLaudoEmitidoViavel(idLicitacao));

			RespostaBD resposta = daoFactory.get(RespostaDAO.class).inserirResposta(criaResposta(laudo.getId(), 1L));

			assertNotNull(resposta);
		});
	}

	@Test
	@Order(2)
	public void recuperarRespostaPorId() {
		RespostaBD resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(1L);

		assertNotNull(resposta);
	}

	@Test
	@Order(3)
	public void alterarResposta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			RespostaBD resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(1L);

			assertEquals("resposta teste", resposta.getResposta());

			resposta.setResposta("resposta alterada");

			daoFactory.get(RespostaDAO.class).alterarResposta(resposta);

			resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(1L);
			assertEquals("resposta alterada", resposta.getResposta());
		});
	}

	@Test
	@Order(4)
	public void excluirResposta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			daoFactory.get(RespostaDAO.class).excluirResposta(1L);

			RespostaBD resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(1L);

			assertNull(resposta);
		});
	}

	@Test
	@Order(5)
	public void inserirRespostaBatch() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			LicitacaoBD licitacao = DataFactory.criaLicitacao();
			LicitacaoBD novaLicitacao = daoFactory.get(LicitacaoDAO.class).insertLicitacao(licitacao);
			Long idLicitacao = novaLicitacao.getId();

			LaudoBD laudo = daoFactory.get(LaudoDAO.class).inserirLaudo(LaudoDAOIT.criaLaudoEmitidoViavel(idLicitacao));

			daoFactory.get(RespostaDAO.class).inserirRespostaBatch(criaRespostas(laudo.getId()));

			RespostaBD resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(2L);
			assertNotNull(resposta);
			resposta = daoFactory.get(RespostaDAO.class).recuperarRespostaPorId(3L);
			assertNotNull(resposta);
		});

	}

	public static RespostaBD criaResposta(Long idLaudo, Long idPergunta) {
		RespostaBD resposta = new RespostaBD();

		resposta.setLaudoFk(idLaudo);
		resposta.setPerguntaFk(idPergunta);
		resposta.setResposta("resposta teste");
		resposta.setVersaoId("nome_evento");
		resposta.setVersaoNr(1L);

		return resposta;
	}

	public static Collection<RespostaBD> criaRespostas(Long idLaudo) {
		Collection<RespostaBD> respostas = new ArrayList<RespostaBD>();

		respostas.add(criaResposta(idLaudo, 1L));
		respostas.add(criaResposta(idLaudo, 2L));

		return respostas;
	}

}
