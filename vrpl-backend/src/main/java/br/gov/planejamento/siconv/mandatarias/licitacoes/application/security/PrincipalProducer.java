package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import com.auth0.jwt.interfaces.DecodedJWT;

@RequestScoped
public class PrincipalProducer {
	private SiconvPrincipal siconvPrincipal;

	@Produces
	public SiconvPrincipal getSiconvPrincipal() {
		return siconvPrincipal;
	}

	public void create(DecodedJWT jwt) {
		this.siconvPrincipal = new ConcreteSiconvPrincipal(jwt);
	}

}
