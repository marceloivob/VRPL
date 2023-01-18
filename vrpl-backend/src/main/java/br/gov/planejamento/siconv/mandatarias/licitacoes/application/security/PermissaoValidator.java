package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import javax.enterprise.inject.spi.CDI;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class PermissaoValidator implements ConstraintValidator<PermissaoUsuario, Object>{

	private boolean andConnector;
	private Profile[] profiles;
	
	private final SiconvPrincipal usuarioLogado;
	
	public PermissaoValidator() {
		this.usuarioLogado = CDI.current().select(SiconvPrincipal.class).get();
	}
	
	@Override
	public void initialize(PermissaoUsuario constraintAnnotation) {
		this.andConnector = constraintAnnotation.andConnector();
		this.profiles = constraintAnnotation.value();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		boolean valid = usuarioLogado.hasProfile(profiles[0]);
		if (profiles.length == 1) {
			return valid;
		}
		// Mais de um profile definido
		for(int i = 1; i < profiles.length; i++) {
			if (andConnector) {
				valid = valid && usuarioLogado.hasProfile(profiles[i]);
			} else {
				valid = valid || usuarioLogado.hasProfile(profiles[i]);
			}
		}
		return valid;	
	}

}
