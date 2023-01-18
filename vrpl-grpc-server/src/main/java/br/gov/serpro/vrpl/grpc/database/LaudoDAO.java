package br.gov.serpro.vrpl.grpc.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

public interface LaudoDAO {
    
    //Exclusão das respostas dos laudos
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirRespostaLaudo(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    //Exclusão das pendências
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirPendenciasLaudo(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

    //Exclusão dos laudos
    @SqlUpdate
    @UseStringTemplateSqlLocator
    void excluirLaudos(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

}
