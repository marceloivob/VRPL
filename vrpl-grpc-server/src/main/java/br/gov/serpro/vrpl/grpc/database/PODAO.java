package br.gov.serpro.vrpl.grpc.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

public interface PODAO {
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirEventoFrenteObras(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirFrentesObra(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirEventos(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirPO(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);
}
