package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.ANALISE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.VRPL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.guava.GuavaPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@ApplicationScoped
public class JDBIProducer {

    @Inject
    @DataSourceFor(VRPL)
    private DataSource dataSourceForVRPL;

    @Inject
    @DataSourceFor(ANALISE)
    private DataSource dataSourceForAnalise;

    @Inject
    private SqlLogger sqlLogger;

	@Produces
	@DataSourceFor(VRPL)
	public Jdbi createJdbiForVRPL() {
		Jdbi jdbi = Jdbi.create(dataSourceForVRPL);
		jdbi.installPlugin(new SqlObjectPlugin());
		jdbi.installPlugin(new GuavaPlugin());
		jdbi.setSqlLogger(sqlLogger);

        return jdbi;
    }

    @Produces
    @DataSourceFor(ANALISE) // FIXME todo DS análise é temporário
    public Jdbi createJdbiForAnalise() {
        Jdbi jdbi = Jdbi.create(dataSourceForAnalise);
        jdbi.installPlugin(new SqlObjectPlugin());
		jdbi.installPlugin(new GuavaPlugin());
        jdbi.setSqlLogger(sqlLogger);

        return jdbi;
    }

    public DataSource getDataSourceForVRPL() {
        return dataSourceForVRPL;
    }

    public void setDataSourceForVRPL(DataSource dataSourceForVRPL) {
        this.dataSourceForVRPL = dataSourceForVRPL;
    }

    public SqlLogger getSqlLogger() {
        return sqlLogger;
    }

    public void setSqlLogger(SqlLogger sqlLogger) {
        this.sqlLogger = sqlLogger;
    }

}
