package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.apache.http.HttpStatus;

import io.grpc.StatusRuntimeException;

@Provider
public class GRPCExceptionMapper extends GenericErrorResponse implements ExceptionMapper<StatusRuntimeException> {

	@Context
	private Providers providers;

	@Override
	public Response toResponse(StatusRuntimeException exception) {
		return genericErrorHandler(
                exception,
                "Erro ao acionar serviço do sistema SICONV.",
                HttpStatus.SC_SERVICE_UNAVAILABLE);
	}

}
