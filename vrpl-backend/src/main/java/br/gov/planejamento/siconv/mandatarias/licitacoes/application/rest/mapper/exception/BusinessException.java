package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;

public class BusinessException extends MandatariasException {
	private static final long serialVersionUID = -8080210994849015208L;
	
	private final ErrorInfo errorInfo;
	private final String detailMessage;
	private List<BusinessException> lista = new ArrayList<>();  

	BusinessException() {
		this.errorInfo = ErrorInfo.ERRO_GENERICO_NEGOCIO;
		this.detailMessage = ErrorInfo.ERRO_GENERICO_NEGOCIO.getDetail();
	}
	
	public BusinessException(ErrorInfo errorInfo) {
		super(errorInfo.getMensagem());
		this.errorInfo = errorInfo;
		this.detailMessage = null;
	}

    public BusinessException(String mensagem) {
    	super(mensagem);
        this.errorInfo = ErrorInfo.ERRO_GENERICO_NEGOCIO;
		this.detailMessage = null;
    }
	
	public BusinessException(ErrorInfo errorInfo, Object... param) {
		super(MessageFormat.format(errorInfo.getMensagem(), param));
		this.errorInfo = errorInfo;

		if (errorInfo.getDetail() == null) {
			this.detailMessage = null;
		} else {
			this.detailMessage = MessageFormat.format(errorInfo.getDetail(), param);
		}
	}

	public ErrorInfo getErrorInfo() {
		return errorInfo;
	}

	public String getDetailMessage() {
		return detailMessage;
	}
	
	void add (BusinessException exception) {
		this.lista.add(exception);
	}
	
	Boolean hasException () {
		return !lista.isEmpty();
	}

	List<BusinessException> getExceptionList() {
    	if (this.hasException()) {
    		return new ArrayList<BusinessException>(lista);
    	} else {
    		return Collections.emptyList();
    	}
	}

}
