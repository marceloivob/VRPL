package br.gov.serpro.vrpl.grpc.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

public interface ServicoDAO {
    
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirServicoFrenteObra(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirServicoFrenteObraAnalise(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirServicos(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirMacroServicoParcela(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirMacroServicos(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);
}
