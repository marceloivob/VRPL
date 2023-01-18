package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.ANALISE;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jdbi.v3.core.Jdbi;

@ApplicationScoped
@DataSourceFor(ANALISE)
public class AnaliseDAOFactory implements DAOFactory {

    @Inject
	@DataSourceFor(ANALISE)
    private Jdbi jdbi;

    /* (non-Javadoc)
	 * @see br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DaoFactoryXXXX#get(java.lang.Class)
	 */
    @Override
	public <T> T get(Class<T> clazz) {
        return jdbi.onDemand(clazz);
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
