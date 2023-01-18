package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.slf4j.Logger;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceFor;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType;

@Readiness
//localhost:8080/health/ready
public class ReadinessHealthCheckResource implements HealthCheck {

	@Inject
	private Logger logger;

	@Inject
	@DataSourceFor(DataSourceType.VRPL)
	private DataSource datasource;

	@Override
	public HealthCheckResponse call() {
		Instant start = Instant.now();
		logger.trace("Início do Readiness - VRPL");

		HealthCheckResponseBuilder builder = HealthCheckResponse.named("readiness"); //

		Instant finish = Instant.now();
		Duration timeElapsed = Duration.between(start, finish);

		logger.trace("Fim do Readiness - VRPL, em {} ms", timeElapsed.toMillis());

		if (databaseIsOk()) {
		return builder.up().build();
		} else {
			return builder.down().build();
		}
	}

	private boolean databaseIsOk() {
		try (Connection conn = datasource.getConnection()) {

			final int TIME_OUT_EM_SEGUNDOS = 15;

			return conn.isValid(TIME_OUT_EM_SEGUNDOS);
		} catch (SQLException sqlException) {
			logger.error("Não foi possível obter uma conexão com o banco!", sqlException);
			return false;
		}
	}

}
