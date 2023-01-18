package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;

@Provider
public class ErrorExceptionMapper extends GenericErrorResponse implements ExceptionMapper<Throwable> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(Throwable t) {
        if (t instanceof WebApplicationException) {
            return webErrorHandler((WebApplicationException) t);
        } else if ((t.getMessage() != null)
                && (t.getMessage().contains("No resource method found for options, return OK with Allow header"))) {
            return restEasyErrorHandler();
        } else {
			return genericErrorHandler(t,
					"Desculpe, mas o sistema não pode atender a sua solicitação de maneira apropriada.",
					HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private Response restEasyErrorHandler() {
        return Response.ok().build();
    }

    private Response webErrorHandler(WebApplicationException webError) {
        logger.warn(webError.getMessage());
        return webError.getResponse();
    }

}