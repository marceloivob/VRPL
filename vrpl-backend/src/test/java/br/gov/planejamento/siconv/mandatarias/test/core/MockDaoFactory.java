package br.gov.planejamento.siconv.mandatarias.test.core;

import org.jdbi.v3.core.Jdbi;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;

public class MockDaoFactory<T> implements DAOFactory {

	private Jdbi jdbi;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		return (T) clazz;
	}

	@Override
	public Jdbi getJdbi() {
		return jdbi;
	}

	@Override
	public void setJdbi(Jdbi jdbi) {
		this.jdbi = jdbi;
	}

}
