package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database.GrupoPerguntaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GrupoPerguntaDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;
	
	private static List<GrupoPerguntaBD> grupoPergunta;

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	@Test
	@Order(1)
	public void recuperarGruposPerguntaPorTemplate() {
		grupoPergunta = daoFactory.get(GrupoPerguntaDAO.class).recuperarGruposPerguntaPorTemplate(1L);

		assertTrue(grupoPergunta.size() > 0);
	}

	@Test
	@Order(2)
	public void recuperarGrupoPerguntaPorId() {
		Long id = grupoPergunta.get(0).getId();
		GrupoPerguntaBD grupo = daoFactory.get(GrupoPerguntaDAO.class).recuperarGrupoPerguntaPorId(id);

		assertNotNull(grupo);
	}

}
