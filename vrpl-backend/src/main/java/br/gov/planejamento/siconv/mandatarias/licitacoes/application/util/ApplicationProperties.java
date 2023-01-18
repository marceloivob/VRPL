package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class ApplicationProperties {

	public static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";

	@NotNull
	@Inject
	@Getter
	@Setter
	@ConfigProperty(name = "liquibase.context")
	private String liquibaseContext;

	@NotNull
	@Inject
	@Getter
	@Setter
	@ConfigProperty(name = "publickey.jwt", defaultValue = "Valor Não Definido")
	private String publicKeyJWT;

	@NotNull
	@Inject
	@Setter
	@ConfigProperty(name = "thorntail.project.stage", defaultValue = "PRODUCAO")
	private String stage;
	
	@NotNull
	@Inject
	@Setter
	@ConfigProperty(name = "integrations.CONFIGURATIONS.STUB")
	private Boolean integrationStub;
	 
	
	public Stage getStage() {
		return Stage.fromSystemStage(stage);
	}
	
	public Boolean isStubActive() {
		return this.integrationStub;
	}	

}
