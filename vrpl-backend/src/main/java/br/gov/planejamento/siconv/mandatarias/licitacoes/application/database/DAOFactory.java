package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import org.jdbi.v3.core.Jdbi;

public interface DAOFactory {

	<T> T get(Class<T> clazz);

	Jdbi getJdbi();

	void setJdbi(Jdbi jdbi);

}