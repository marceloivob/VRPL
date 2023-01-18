package br.gov.planejamento.siconv.mandatarias.test.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.liquibase.LiquibaseProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.cdi.CDILiquibaseConfig;

public class StartupDatabase {

	private Logger logger = LoggerFactory.getLogger(StartupDatabase.class);

	private Connection connection;

	private DataSource dataSource;

	private ProjectConfiguration configuration = new ProjectConfiguration();

	public StartupDatabase() {

		if (configuration.getUseDatabaseInMemory()) {
			logger.info("Usando banco de dados em memória");
			this.initDatabaseInMemory();
		} else {
			logger.info("Usando configurações do banco de dados definido no arquivo: {}",
					configuration.getFileConfigPath());
			this.useDatabasePersistent();
		}

		logger.info("Aplicar scripts do Liquibase: {}", configuration.applyScriptsFromLiquibase());
		if (configuration.applyScriptsFromLiquibase()) {
			this.applyScriptsFromLiquibase(configuration.getContext());
		}
	}

	private void useDatabasePersistent() {
		try {
			this.dataSource = configuration.getDatasource();
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa o banco de dados em memória
	 */
	void initDatabaseInMemory() {
		try {
			EmbeddedPostgres pg = EmbeddedPostgres.start();
			this.dataSource = pg.getPostgresDatabase();
			this.connection = dataSource.getConnection();

			logger.info("URL de conexão com o banco: {}. Usar o usuário: {} e senha: vazia",
					this.connection.getMetaData().getURL(), this.connection.getMetaData().getUserName());
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aplica os scripts do Liquibase
	 */
	void applyScriptsFromLiquibase(String context) {
		try {

			Database database = DatabaseFactory.getInstance()
					.findCorrectDatabaseImplementation(new JdbcConnection(this.connection));

			ApplicationProperties applicationProperties = new ApplicationProperties();
			applicationProperties.setLiquibaseContext(configuration.getContext());

			LiquibaseProducer liquibaseProducer = new LiquibaseProducer();
			liquibaseProducer.setApplicationProperties(applicationProperties);

			CDILiquibaseConfig d = liquibaseProducer.createConfig();

			database.setDefaultSchemaName(d.getDefaultSchema());
			Liquibase liquibase = new Liquibase(d.getChangeLog(), liquibaseProducer.create(), database);

			liquibase.update(context);
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (LiquibaseException e) {
			e.printStackTrace();
		}

	}

	public DataSource getDataSource() {
		return dataSource;
	}

}