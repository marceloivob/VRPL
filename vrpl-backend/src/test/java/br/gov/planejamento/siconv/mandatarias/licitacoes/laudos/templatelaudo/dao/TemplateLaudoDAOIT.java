package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.database.TemplateLaudoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TemplateLaudoDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

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
	public void recuperarTemplateLaudoPorTipo() {
		TemplateLaudoBD templateLAE = daoFactory.get(TemplateLaudoDAO.class).recuperarTemplateLaudoPorTipo("VRPL");

		assertNotNull(templateLAE);
	}

	@Test
	@Order(2)
	public void recuperarTemplateLaudoPorId() {
		TemplateLaudoBD template = daoFactory.get(TemplateLaudoDAO.class).recuperarTemplateLaudoPorId(1L);

		assertNotNull(template);
	}

}
