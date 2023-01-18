package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper  extends GenericErrorResponse implements ExceptionMapper<BusinessException> {

	
	@Inject
	private BusinessExceptionContext businessExceptionContext;
	
    @Override
    public Response toResponse(BusinessException exception) {
        HashMap<String, Object> retorno = new HashMap<>();
        retorno.put("status", "fail");
        retorno.put("data", failData(exception));

        if (exception.getErrorInfo().getCodigoHttp() == null) {
            return Response.status(422).entity(retorno).build();
        } else if (BAD_REQUEST.equals(exception.getErrorInfo().getCodigoHttp())) {
            return genericErrorHandler(
                    exception,
                    exception.getErrorInfo().getMensagem(),
                    exception.getErrorInfo().getCodigoHttp().getStatusCode()
            );
        } else {
            return Response.status(exception.getErrorInfo().getCodigoHttp()).entity(retorno).build();
        }
    }

    private HashMap<String, Object> failData(BusinessException exception) {
        HashMap<String, Object> data = new HashMap<>();
        
        if (businessExceptionContext.hasException()) {
        	adicionarErrorListMap(data);
        } else {
	        adicionaErroMap("codigo", "message", "detail", exception, data);
        }
        data.put("severity", exception.getErrorInfo().getSeverity());
        
        return data;
    }

	private void adicionarErrorListMap(HashMap<String, Object> data) {
		data.put ("errorListSize", businessExceptionContext.getExceptionList().size());
		
		AtomicInteger ai  = new AtomicInteger();
		ai.set(0);
		businessExceptionContext.getExceptionList().forEach( bex -> {
			adicionaErroMap("codigo_"+ai.get(), "message_"+ai.get(), "detail_"+ai.get(), bex, data);
			ai.getAndAdd(1);
		});
	}

	private void adicionaErroMap(String codeAlias, String messageAlias, String detailAlias, BusinessException exception, HashMap<String, Object> data) {
		data.put(codeAlias, exception.getErrorInfo().getCodigo());
		data.put(messageAlias, exception.getMessage());

		if (exception.getDetailMessage() != null) {
			data.put(detailAlias, exception.getDetailMessage());
		}
	}

}
