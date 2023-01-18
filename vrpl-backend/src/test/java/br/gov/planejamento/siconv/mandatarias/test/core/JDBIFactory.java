package br.gov.planejamento.siconv.mandatarias.test.core;

import org.jdbi.v3.core.Jdbi;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.JDBIProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.VrplCustomSqlLogger;

public class JDBIFactory {

    private JDBIProducer jdbiProducer = new JDBIProducer();

    private VrplCustomSqlLogger vrplCustomSqlLogger = new VrplCustomSqlLogger();

    private StartupDatabase postgresqlInMemory = new StartupDatabase();

    public JDBIFactory() {
        this.setup();
    }

    private void setup() {

        jdbiProducer.setSqlLogger(vrplCustomSqlLogger);

        jdbiProducer.setDataSourceForVRPL(postgresqlInMemory.getDataSource());
    }

    public Jdbi createJDBI() {

        Jdbi jdbi = jdbiProducer.createJdbiForVRPL();

        return jdbi;
    }

}
