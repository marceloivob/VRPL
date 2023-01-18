package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ConstraintBeanValidationTest {

	@Spy
	private ConstraintBeanValidation<ObjetoBD> constraintBeanValidationForObjetoBD;

	@Spy
	private ConstraintBeanValidation<Object> constraintBeanValidationForObject;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		Validator beanValidator = factory.getValidator();

		constraintBeanValidationForObjetoBD.setBeanValidator(beanValidator);
		constraintBeanValidationForObject.setBeanValidator(beanValidator);
	}

	@Test
	public void validarObjetoComAnotacao() {
		ObjetoBD objeto = new ObjetoBD();

		assertThrows(ConstraintBeanValidationException.class,
				() -> constraintBeanValidationForObjetoBD.validate(objeto));
	}

	@Test
	public void validarObjetoSemAnotacao() {
		Object objeto = new Object();

		assertThrows(IllegalArgumentException.class, () -> constraintBeanValidationForObject.validate(objeto));
	}

}

class ObjetoBD {

	@NotNull
	private Long id;

	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
