package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business.ServicoBC;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@TestMethodOrder(OrderAnnotation.class)
@DisplayName(value = "MacroServicoBC - Teste integrado BC")
@TestInstance(Lifecycle.PER_CLASS)
class MacroServicoBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private MacroServicoBC macroServicoBC;

	@Mock
	private ServicoBC servicoBC;

	@Spy
	private List<MacroServicoDTO> lServicoBC;

	@Spy
	private ConstraintBeanValidation<FrenteObraBD> constraintBeanValidation;

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
			handle.execute(DataFactory.INSERT_LICITACAO);
			handle.execute(DataFactory.INSERT_LOTE_LICITACAO);
			handle.execute(DataFactory.INSERT_SUBTIEM_LICITACAO);
			handle.execute(DataFactory.INSERT_META);
			handle.execute(DataFactory.INSERT_SUBMETA);
			handle.execute(DataFactory.INSERT_PO);

			handle.execute(DataFactory.INSERT_MACRO_SERVICO);

			handle.execute(DataFactory.INSERT_SERVICO);

		});
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator beanValidator = factory.getValidator();

		constraintBeanValidation.setBeanValidator(beanValidator);

		macroServicoBC.setDao(daoFactory);
		macroServicoBC.setServicoBC(servicoBC);
	}

	@Test
	@DisplayName(value = "Listar Macroservicos de PO")
	void recuperarMacroServicoServicosPorPo() {
		daoFactory.getJdbi().useTransaction(transaction -> {
			List<MacroServicoDTO> listaMacroServicoDTO = macroServicoBC.recuperarMacroServicoPorPo(transaction, 1L);
			assertEquals(2, listaMacroServicoDTO.size());
		});
	}

	@AfterAll
	@DisplayName(value = "Listar Macroservicos Serviço de PO sem haver Macrosservico")
	void recuperarMacroServicoServicosPorPoSemRegistro() {
		limparMacrosservico();
		daoFactory.getJdbi().useTransaction(transaction -> {
			List<MacroServicoDTO> listaMacroServicoDTO = macroServicoBC.recuperarMacroServicoPorPo(transaction, 1L);

		assertTrue(listaMacroServicoDTO.isEmpty());
		});

	}

	/** Consulta os Macrosserviços da idPo=1 e os serviços vinculados */
	private void limparMacrosservico() {
		daoFactory.getJdbi().useTransaction(transaction -> {
			List<MacroServicoDTO> listaMacroServicoDTO = macroServicoBC.recuperarMacroServicoPorPo(transaction, 1L);
		for (MacroServicoDTO macroServicoDTO : listaMacroServicoDTO) {
			excluirMacrosservico(macroServicoDTO.getId());
		}
		});
	}

	/** Exclui o macrosservico e respectivos serviços */
	private void excluirMacrosservico(long idMacrosservico) {
		daoFactory.getJdbi().useHandle(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.execute("DELETE FROM siconv.vrpl_servico " + "WHERE macro_servico_fk= " + idMacrosservico);
			handle.execute("DELETE FROM siconv.vrpl_macro_servico " + "WHERE id= " + idMacrosservico);
		});
	}

}
