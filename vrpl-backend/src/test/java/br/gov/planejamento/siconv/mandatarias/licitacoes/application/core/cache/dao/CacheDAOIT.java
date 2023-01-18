package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CacheDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

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
			handle.execute(DataFactory.INSERT_ANEXO);
		});

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void recuperarRegistrosPermitidos() {
		Long idProposta = 1L;
		SiconvPrincipal usuarioMockado = new MockSiconvPrincipal(Profile.MANDATARIA, idProposta);

		List<Resultado> resultado = daoFactory.get(CacheDAO.class).recuperarRegistrosPermitidos(usuarioMockado);

		assertFalse(resultado.isEmpty());
	}

	@Test
	public void definirUsuarioLogado() {

		daoFactory.getJdbi().useTransaction(handle -> {

			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			String usuarioLogadoRecuperado = handle.attach(CacheDAO.class).consultarUsuarioLogado();

			assertTrue(usuarioLogado != usuarioLogadoRecuperado,
					"Garante que são 2 objetos diferentes de Usuários Logados");
			assertTrue(usuarioLogado.equals(usuarioLogadoRecuperado));
		});

	}

}
