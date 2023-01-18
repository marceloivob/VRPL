package br.gov.planejamento.siconv.mandatarias.licitacoes.application.database;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.VRPL;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.jdbi.v3.core.Jdbi;

@RequestScoped
@Default
@DataSourceFor(VRPL)
public class ConcreteDAOFactory implements DAOFactory {

    @Inject
    @DataSourceFor(VRPL)
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
