package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@Inherited
@InterceptorBinding
@Target({ TYPE, METHOD})
@Retention(RUNTIME)
public @interface AccessAllowed {
    @Nonbinding
    Profile[] value() default Profile.GUEST;

    @Nonbinding
	Role[] roles() default { Role.CONSULTAS_BASICAS_CONCEDENTE, Role.CONSULTAS_BASICAS_PROPONENTE,
			Role.CONSULTAS_AGENTE_FINANCEIRO };
}