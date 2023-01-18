package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;


@RequestScoped
public class BusinessExceptionProducer {

    @Produces @BusinessExceptionQualifier
    public BusinessException produces() {
        return new BusinessException();
    }
	
}
