package br.gov.planejamento.siconv.mandatarias.licitacoes.application.interceptor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;


public class TracerProducer {

    @ApplicationScoped @Produces 
    public Tracer produces() {
    	
    	Configuration c = Configuration.fromEnv("vrpl/backend");
    	
        return c.getTracer();
    }

}
