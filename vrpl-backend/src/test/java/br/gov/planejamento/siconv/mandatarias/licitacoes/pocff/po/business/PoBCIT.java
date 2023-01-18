package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.PoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@DisplayName(value = "PoBC - Exemplo sem uso de Mock (com acessando o banco de dados)")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PoBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private PoBC poBC;

	@Mock
	private UserCanEditVerifier checkPermission;

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		return new MockSiconvPrincipal(Profile.MANDATARIA, "1123213");
	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		poBC.setCheckPermission(checkPermission);
		poBC.setDao(daoFactory);
	}

	@Test
	@DisplayName(value = "Alterar PO inexistente")
	public void alterarPoInexistente() {
		PoDetalhadaVRPLDTO po = new PoDetalhadaVRPLDTO();
		po.setId(-1L);

		assertThrows(PoNaoEncontradaException.class, () -> poBC.alterar(po));
	}

}
