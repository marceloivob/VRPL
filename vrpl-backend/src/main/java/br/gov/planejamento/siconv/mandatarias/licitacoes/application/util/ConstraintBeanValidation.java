package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class ConstraintBeanValidation<T> {

	@Inject
	private Validator beanValidator;

	public void validate(T object) {

		if (existeAnotacaoNoObjeto(object)) {
			Set<ConstraintViolation<T>> violacoes = beanValidator.validate(object);

			if (!violacoes.isEmpty()) {
				ConstraintViolation<T> violacao = violacoes.iterator().next();

				throw new ConstraintBeanValidationException(violacao);
			}
		} else {
			throw new IllegalArgumentException("O objeto não tem nenhuma anotação a ser verificada.");
		}

	}

	private boolean existeAnotacaoNoObjeto(T object) {
		Field[] fields = object.getClass().getDeclaredFields();

		for (Field f : fields) {
			Annotation[] annotations = f.getAnnotations();

			for (Annotation annotation : annotations) {
				if (annotation.toString().contains("javax.validation.constraints")) {
					return true;
				}
			}
		}

		return false;

	}

	public void setBeanValidator(Validator beanValidator) {
		this.beanValidator = beanValidator;
	}
}
