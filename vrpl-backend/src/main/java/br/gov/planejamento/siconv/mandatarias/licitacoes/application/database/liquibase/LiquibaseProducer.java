package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.liquibase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.Setter;

@ApplicationScoped
public class LiquibaseProducer {

    @Inject
    @LiquibaseType
    private DataSource dataSource;

	@Inject
	@Setter
	private ApplicationProperties applicationProperties;

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("liquibase/databasechangelog.yml");
        config.setDefaultSchema("public");
		config.setContexts(applicationProperties.getLiquibaseContext());

        return config;
    }

    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }

}