package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.ANALISE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.VRPL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;
import liquibase.integration.cdi.annotations.LiquibaseType;

@ApplicationScoped
public class DataSourceProducer {

    private static final String CRIANDO_DATA_SOURCE = "Criando DataSource: {}";

    @Inject
    private InitialContext context;

    @Inject
    private Logger logger;

    @Produces
    @LiquibaseType
    public DataSource createDataSourceForLiquibase() {
        try {
            final String DATASOURCE_NAME = "java:/Mandatarias_VRPL_DS";

            DataSource dataSource = (DataSource) context.lookup(DATASOURCE_NAME);

            logger.debug(CRIANDO_DATA_SOURCE, DATASOURCE_NAME);

            return dataSource;
        } catch (NamingException e) {
            throw new MandatariasException(e);
        }
    }

    @Produces
    @DataSourceFor(VRPL)
    public DataSource createDataSourceForVRPL() {
        try {
            final String DATASOURCE_NAME = "java:/Mandatarias_VRPL_DS";

            DataSource dataSource = (DataSource) context.lookup(DATASOURCE_NAME);

            logger.debug(CRIANDO_DATA_SOURCE, DATASOURCE_NAME);

            return dataSource;
        } catch (NamingException e) {
            throw new MandatariasException(e);
        }
    }

    @Produces
    @DataSourceFor(ANALISE)
    public DataSource createDataSourceForAnalise() {
        try {
			final String DATASOURCE_NAME = "java:/Mandatarias_Analise_DS";

            DataSource dataSource = (DataSource) context.lookup(DATASOURCE_NAME);

            logger.debug(CRIANDO_DATA_SOURCE, DATASOURCE_NAME);

            return dataSource;
        } catch (NamingException e) {
            throw new MandatariasException(e);
        }
    }

    @Produces
    public InitialContext createContext() {
        try {
            InitialContext initialContext = new InitialContext();

            return initialContext;
        } catch (NamingException e) {
            throw new MandatariasException(e);
        }
    }

    public void setContext(InitialContext context) {
        this.context = context;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
