package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
@Target({ TYPE })
@Inherited
public @interface QuadroResumoEventConfig {

	boolean historico() default false;
	
	boolean versionamentoComHistorico() default false;
	
	boolean versionamentoSemHistorico() default false;
	
	boolean email() default false;
	
	boolean servicos() default false;
}
