package br.gov.serpro.vrpl.grpc.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

public interface LoteDAO {
   
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirMeta(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirSubmetas(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirLotesLicitacao(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

}
