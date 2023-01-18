package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.SecurityAccessException;

@AccessAllowed
@Interceptor
public class AccessAllowedInterceptor {

    @Inject
    private SiconvPrincipal principal;

    @AroundInvoke
    public Object manageTransaction(InvocationContext ctx) throws Exception {

        AccessAllowed annotation = getAnnotation(ctx);

        Profile[] allowedProfiles = annotation.value();
        if(allowedProfiles.length > 0 && !principal.hasProfile(allowedProfiles)) {
			throw new SecurityAccessException(principal, allowedProfiles);
        }

        Role[] allowedRoles = annotation.roles();
        if(allowedRoles.length > 0 && !principal.hasRole(allowedRoles)) {
			throw new SecurityAccessException(principal, allowedRoles);
        }

        return ctx.proceed();
    }

    private AccessAllowed getAnnotation(InvocationContext ic) {
        AccessAllowed annotation = ic.getMethod().getAnnotation(AccessAllowed.class);

        if ( annotation == null) {
            annotation = ic.getTarget().getClass().getAnnotation(AccessAllowed.class);
        }

        return annotation;
    }
}
