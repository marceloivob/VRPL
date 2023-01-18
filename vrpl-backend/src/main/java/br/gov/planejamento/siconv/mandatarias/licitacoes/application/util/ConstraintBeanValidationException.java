package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;

public class ConstraintBeanValidationException extends MandatariasException {

	private static final long serialVersionUID = 3935663790028403472L;

	private static final Logger logger = LoggerFactory.getLogger(ConstraintBeanValidationException.class);

	public ConstraintBeanValidationException(ConstraintViolation<?> violacao) {
		super(String.format("O campo %s %s %s %s", violacao.getPropertyPath().iterator().next().getName(),
				violacao.getMessage(), violacao.getRootBeanClass(), violacao.getRootBean()));
		logger.debug(this.getMessage());

	}

}
