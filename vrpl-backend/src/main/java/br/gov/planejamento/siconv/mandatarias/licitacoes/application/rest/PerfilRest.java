package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;

@Path("/perfil")
@Produces(MediaType.APPLICATION_JSON)
public class PerfilRest {

	@GET
	@AccessAllowed({ Profile.GUEST })
	@Path("/guestExclusive")
	public Response getGuest() {
		return Response.ok().build();
	}

	@GET
	@AccessAllowed({ Profile.CONCEDENTE })
	@Path("/concedenteExclusive")
	public Response getConcedente() {
		return Response.ok().build();
	}

	@GET
	@AccessAllowed({ Profile.MANDATARIA })
	@Path("/mandatariaExclusive")
	public Response getMandataria() {
		return Response.ok().build();
	}

	@GET
	@AccessAllowed({ Profile.PROPONENTE })
	@Path("/proponenteExclusive")
	public Response getProponente() {
		return Response.ok().build();
	}

	@GET
	@Path("/withoutToken")
	public Response withoutToken() {
		/**
		 * Este método nunca vai ter cobertura de código
		 */
		throw new IllegalArgumentException(
				"Este método NUNCA poderá ser acionado por que não possui a anotação @AccessAllowed! "
						+ " O Interceptor de autenticação TEM que barrar o acesso a este método. "
						+ " Se essa exceção for lançada é um erro que introduzido no Interceptor.");
	}

}