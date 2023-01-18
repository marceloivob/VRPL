package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.filter;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.IgnorePrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.PrincipalProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.ValidateJWT;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

	@Inject
	private Logger logger;

	@Inject
	private PrincipalProducer principalProducer;

	@Inject
	private ValidateJWT validateJWT;

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null && this.ignoreToken()) {
			return;
		}

		String token = authorizationHeader != null ? authorizationHeader.substring("Bearer".length()).trim() : "";

		try {
			DecodedJWT jwt = validateJWT.validaToken(token);

			principalProducer.create(jwt);

		} catch (TokenExpiredException tee) {
			logger.info("[JWTExpirado]: '{}'", token);

			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Expired token").build());
		} catch (JWTVerificationException e) {
			logger.info("[JWTInvalido]: '{}'. Motivo: {}", token, e.getLocalizedMessage());

			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

	private boolean ignoreToken() {
		IgnorePrincipal ignorePrincipal = resourceInfo.getResourceClass().getAnnotation(IgnorePrincipal.class);
		if (ignorePrincipal == null) {
			ignorePrincipal = resourceInfo.getResourceMethod().getAnnotation(IgnorePrincipal.class);
		}

		return ignorePrincipal != null;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
