package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.BasicAuthenticatorHeaderGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Fonte:
 * http://www.adam-bien.com/roller/abien/entry/client_side_http_basic_access
 */
@Slf4j
public class BasicAuthenticatorFilter implements ClientRequestFilter {

	@Inject
	private BasicAuthenticatorHeaderGenerator basicAuthenticatorHeaderGenerator;

	@Inject
	private SiconvPrincipal usuarioLogado;

	/**
	 * Construtor Padrão para o CDI
	 */
	public BasicAuthenticatorFilter() {
		// noop
	}

	public BasicAuthenticatorFilter(SiconvPrincipal usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		MultivaluedMap<String, Object> headers = requestContext.getHeaders();
		final String basicAuthentication = basicAuthenticatorHeaderGenerator.create(usuarioLogado);

		headers.add(HttpHeaders.AUTHORIZATION, basicAuthentication);

		log.debug(headers.get(HttpHeaders.AUTHORIZATION).toString());
	}



}
