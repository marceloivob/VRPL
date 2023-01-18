package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.IgnorePrincipal;

@Path("/api/metrics")
@IgnorePrincipal
public class MetricsRest {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response recoveryInformationAboutBuildFromBackend()  {
    	String url = uriInfo.getRequestUri().toString();
    	URI	uri = URI.create(url.replace("api/metrics", "metrics"));

        return Response.seeOther(uri).build();
    }


}
