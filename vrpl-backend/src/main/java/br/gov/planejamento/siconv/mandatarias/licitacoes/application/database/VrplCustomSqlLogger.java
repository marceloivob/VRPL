package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import java.time.temporal.ChronoUnit;

import org.jdbi.v3.core.statement.ParsedParameters;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VrplCustomSqlLogger implements SqlLogger {

    @Override
	public void logAfterExecution(StatementContext context) {
		log.debug(context.getRenderedSql());
        ParsedParameters parameters = context.getParsedSql().getParameters();
		log.debug("{}", parameters);

		log.debug("{}", context.getBinding());
		log.debug("Tempo de execução da consulta:{}", context.getElapsedTime(ChronoUnit.MILLIS) + " ms");
    }

}
