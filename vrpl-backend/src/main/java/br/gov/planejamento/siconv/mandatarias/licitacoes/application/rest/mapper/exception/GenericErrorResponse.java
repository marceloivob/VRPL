package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.PrincipalProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.GeradorDeTicket;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.Stage;
import io.sentry.Sentry;
import io.sentry.event.UserBuilder;

public abstract class GenericErrorResponse {

	@Inject
	private Logger logger;

	@Inject
	private GeradorDeTicket geradorDeTicket;

	
	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private PrincipalProducer principalProducer;
	
	@Context
	private HttpServletRequest request;

	@Inject
	private ApplicationProperties applicationProperties;

	protected Response genericErrorHandler(Throwable t, String message, Integer status) {
		String cpf = "NA";
		Object roles = "NA";
		Object idProposta = "NA";

		if (usuarioLogado == null) {
			usuarioLogado = principalProducer.getSiconvPrincipal();
		}
		
		if (usuarioLogado != null) {
			cpf = usuarioLogado.getCpf();
			roles = usuarioLogado.getRoles();
			idProposta = usuarioLogado.getIdProposta();
		}

		Sentry.getContext().setUser(new UserBuilder().setUsername(cpf).setIpAddress(request.getRemoteAddr())
				.setData(Collections.singletonMap("info", roles)).build());

		String ticket = geradorDeTicket.generateNewTicket();
		String rootMessage = ExceptionUtils.getRootCauseMessage(t);
		String trace = ExceptionUtils.getStackTrace(t);

		Sentry.getContext().addExtra("ticket", ticket);
		Sentry.getContext().addExtra("proposta", idProposta);

		if (Stage.LOCAL.equals(applicationProperties.getStage())) {
            logger.error("[VRPL:Error] [Ticket:{}] [{}] - {}", ticket, rootMessage, trace);
        } else {
			logger.error(String.format("[VRPL:Error] [Ticket:%s] [%s]", ticket, rootMessage), t);
        }

		Sentry.clearContext();

		if (status == null) {
			status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		}

		return getResponse(status, getResponseBody(ticket, rootMessage, trace, message));
	}

	private Response getResponse(int status, Object body) {
		return Response.status(status).header("Content-type", APPLICATION_JSON_UTF8).entity(body).build();
	}

	private Object getResponseBody(String ticket, String rootMessage, String trace, String message) {
		HashMap<String, Object> retorno = new HashMap<>();
		retorno.put("status", "error");
		retorno.put("message", message);
		retorno.put("ticket", ticket);

		List<Stage> ambientesDesenvolvimento = Arrays.asList(Stage.LOCAL, Stage.DEVELOPMENT);

		if (ambientesDesenvolvimento.contains(applicationProperties.getStage())) {
			retorno.put("environment", applicationProperties.getStage());
			retorno.put("rootMessage", rootMessage);

			if (trace != null) {
				retorno.put("trace", trace);
			}
		}

		return retorno;
	}
}
