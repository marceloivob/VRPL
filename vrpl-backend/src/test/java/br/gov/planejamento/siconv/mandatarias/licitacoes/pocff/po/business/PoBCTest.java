package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business.MacroServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.PoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.MockDaoFactory;

@DisplayName(value = "PoBC - Exemplo usando Mock")
public class PoBCTest {

	@Spy
	private PoBC poBC;

	@Mock
	private MacroServicoBC macroServicoBC;

	@Mock
	private MacroServicoParcelaDAO macroServicoParcelaDAOMock;

	@Mock
	private MockDaoFactory<PoDAO> daoFactory;

	@Mock
	private PoDAO poDAO;

	@Mock
	private UserCanEditVerifier checkPermission;

	@Spy
	private ConstraintBeanValidation<PoBD> constraintBeanValidation;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		Validator beanValidator = factory.getValidator();

		constraintBeanValidation.setBeanValidator(beanValidator);

		poBC.setConstraintBeanValidation(constraintBeanValidation);
		poBC.setCheckPermission(checkPermission);
		poBC.setMacroServicoBC(macroServicoBC);
		
		poBC.setDao(daoFactory);

	}

	@Test
	@DisplayName(value = "Alterar PO inexistente")
	public void alterarPoInexistente() {

		Long idPoInexistente = -1L;

		PoDetalhadaVRPLDTO pp = new PoDetalhadaVRPLDTO();
		pp.setId(idPoInexistente);

		Mockito.doReturn(poDAO).when(daoFactory).get(PoDAO.class);
		Mockito.doReturn(null).when(poDAO).recuperarPoVRPL(idPoInexistente);

		assertThrows(PoNaoEncontradaException.class, () -> poBC.alterar(pp));
	}

// teste comentado depois que a rotina de alterar passou a usar transaction 	
	
//	@Test
//	@DisplayName(value = "Alterar PO Existente")
//	public void alterarPoExistente() {
//
//		Long idPo = 1L;
//
//		PoDetalhadaVRPLDTO poInformada = new PoDetalhadaVRPLDTO();
//		poInformada.setId(idPo);
//		poInformada.setPrevisaoAnalise(LocalDate.now());
//		poInformada.setPrevisaoInicioVRPL(LocalDate.now());
//		poInformada.setQtMesesDuracaoObra(1L);
//		poInformada.setQtMesesDuracaoObraValorOriginal(1L);
//
//		PoBD poMockada = new PoBD();
//		poMockada.setId(1L);
//		poMockada.setIdPoAnalise(1L);
//		poMockada.setSubmetaId(1L);
//		poMockada.setInAcompanhamentoEventos(false);
//		poMockada.setDtBaseAnalise(LocalDate.now());
//		poMockada.setVersaoNr(1L);
//
//		Mockito.doReturn(poDAO).when(daoFactory).get(PoDAO.class);
//		Mockito.doReturn(poMockada).when(poDAO).recuperarPoVRPL(idPo);
//
//		Mockito.doReturn(poMockada).when(poDAO).recuperarPoVRPL(idPo);
//		Mockito.doReturn(poMockada).when(poDAO).recuperarPoPorId(idPo);
//		Mockito.when(poBC.getMacroServicoParcelaDAO()).thenReturn(macroServicoParcelaDAOMock);
//
//		euComo(Profile.GUEST);
//
//		poBC.alterar(poInformada);
//	}

	private PoBCTest euComo(Profile perfilDoUsuario) {
		return this;
	}

}
