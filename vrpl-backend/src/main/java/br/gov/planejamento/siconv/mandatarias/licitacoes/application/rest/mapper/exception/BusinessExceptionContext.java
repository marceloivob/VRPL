package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class BusinessExceptionContext {

    @Inject
    @BusinessExceptionQualifier
    private BusinessException bex;
    
    private boolean activated = false; 
    
    public void add (BusinessException exception) {
    	
    	bex.add(exception);
    }
    
    public void throwException () {
    	this.activated = true;
    	if (hasException()) {
    		throw bex;
    	}
    }
    
    Boolean hasException() {
    	return activated? bex.hasException(): activated;
    }
    
    List<BusinessException> getExceptionList(){
  		return bex.getExceptionList();
    }

	public List<BusinessException> getException(Class<? extends BusinessException> clazz) {
		
		if (hasException()) {
			
			return this.bex.getExceptionList().stream().filter((ex) -> ex.getClass().equals(clazz) ).collect (Collectors.toList());
		} else {
			return Collections.emptyList();
		}
		
		
	}
	
}
