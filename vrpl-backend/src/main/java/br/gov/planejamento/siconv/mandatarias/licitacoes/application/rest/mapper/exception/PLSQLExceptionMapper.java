package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import org.apache.http.HttpStatus;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import com.google.common.base.Throwables;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.Severity;

@Provider
public class PLSQLExceptionMapper implements ExceptionMapper<UnableToExecuteStatementException> {

	@Context
	private Providers providers;

	@Override
	public Response toResponse(UnableToExecuteStatementException exception) {

		ExceptionMapper<Throwable> globalHandler = providers.getExceptionMapper(Throwable.class);

		final String CODIGO_ERRO_CONCORRENCIA = "23501";
		// Existe mais de uma versão da exceção PSQLException no Classloader
		// Não foi identificado a origem dessa outra versão da classe
		// Isso trouxe como consequência não poder simplesmente usar o instanceof
		// PSQLException
		// Isso sempre retornava false.
		// Foi tentado o uso do cast (PSQLException) exception.getCause().
		// Neste cenário, era apresentado o erro de ClassCastException: PSQLException
		// não é uma classe do tipo PSQLException
		// ¯\_(ツ)_/¯
		// A solução aqui usada foi encontrada no seguinte link:
		// https://stackoverflow.com/questions/40301779/how-to-handle-a-psqlexception-in-java
		Throwable rootCause = Throwables.getRootCause(exception.getCause());

		if (rootCause instanceof SQLException) {
			if (CODIGO_ERRO_CONCORRENCIA.equals(((SQLException) rootCause).getSQLState())) {
				HashMap<String, Object> retorno = new HashMap<>();
				retorno.put("status", "fail");
				retorno.put("data", failData(exception));

				return Response.status(HttpStatus.SC_CONFLICT).entity(retorno).build();
			} else {
				return globalHandler.toResponse(exception);
			}
		} else {
			return globalHandler.toResponse(exception);
		}
	}

	private HashMap<String, Object> failData(Exception exception) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("message", exception.getMessage());
		data.put("severity", Severity.ERROR);

		return data;
	}
}
