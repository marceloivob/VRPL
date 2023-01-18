package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.PermissaoUsuario;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@EnableWeld
class PermissaoValidatorTest {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator
    	.from(PermissaoValidatorTest.class, SiconvPrincipal.class).build();
	
    ExecutableValidator validator;
    
	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		return new MockSiconvPrincipal(Profile.PROPONENTE, "1123213");
	}

	@Inject
	SiconvPrincipal principal;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator().forExecutables();
    }
	
	@Test
	void testIsNotValidSingle() throws Exception {
		TestCases test1 = new TestCases();
		Method method = TestCases.class.getMethod("test1");
		QuadroResumoValidator rule = mock(QuadroResumoValidator.class);
		Set<ConstraintViolation<TestCases>> constraintViolations = validator.
				validateReturnValue(test1, method, rule);
		assertEquals(1, constraintViolations.size());
	}

	@Test
	void testIsValidSingle() throws Exception {
		TestCases test1 = new TestCases();
		Method method = TestCases.class.getMethod("test2");
		QuadroResumoValidator rule = mock(QuadroResumoValidator.class);
		Set<ConstraintViolation<TestCases>> constraintViolations = validator.
				validateReturnValue(test1, method, rule);
		assertEquals(0, constraintViolations.size());
	}

	@Test
	void testIsValidArrayAnd() throws Exception {
		principal.getProfiles().add(Profile.MANDATARIA);
		TestCases test1 = new TestCases();
		Method method = TestCases.class.getMethod("test3");
		QuadroResumoValidator rule = mock(QuadroResumoValidator.class);
		Set<ConstraintViolation<TestCases>> constraintViolations = validator.
				validateReturnValue(test1, method, rule);
		assertEquals(0, constraintViolations.size());
	}

	@Test
	void testIsNotValidArrayAnd() throws Exception {
		TestCases test1 = new TestCases();
		Method method = TestCases.class.getMethod("test4");
		QuadroResumoValidator rule = mock(QuadroResumoValidator.class);
		Set<ConstraintViolation<TestCases>> constraintViolations = validator.
				validateReturnValue(test1, method, rule);
		assertEquals(1, constraintViolations.size());
	}

	@Test
	void testIsValidArrayOr() throws Exception {
		TestCases test1 = new TestCases();
		Method method = TestCases.class.getMethod("test5");
		QuadroResumoValidator rule = mock(QuadroResumoValidator.class);
		Set<ConstraintViolation<TestCases>> constraintViolations = validator.
				validateReturnValue(test1, method, rule);
		assertEquals(0, constraintViolations.size());
	}
	
}

class TestCases {
	
	@PermissaoUsuario(Profile.MANDATARIA)
	public QuadroResumoValidator test1() {
		return mock(QuadroResumoValidator.class);
	}

	@PermissaoUsuario(Profile.PROPONENTE)
	public QuadroResumoValidator test2() {
		return mock(QuadroResumoValidator.class);
	}
	
	@PermissaoUsuario(value = {Profile.MANDATARIA, Profile.PROPONENTE}, andConnector = true)
	public QuadroResumoValidator test3() {
		return mock(QuadroResumoValidator.class);
	}

	@PermissaoUsuario(value = {Profile.MANDATARIA, Profile.CONCEDENTE}, andConnector = true)
	public QuadroResumoValidator test4() {
		return mock(QuadroResumoValidator.class);
	}

	@PermissaoUsuario(value = {Profile.MANDATARIA, Profile.PROPONENTE})
	public QuadroResumoValidator test5() {
		return mock(QuadroResumoValidator.class);
	}

}
