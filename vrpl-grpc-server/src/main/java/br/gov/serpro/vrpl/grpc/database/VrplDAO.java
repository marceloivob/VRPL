package br.gov.serpro.vrpl.grpc.database;

import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.serpro.vrpl.grpc.database.bean.PropostaLotesBD;

public interface VrplDAO {
    
    @SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(PropostaLotesBD.class)
	Optional<PropostaLotesBD> verificarExclusaoVRPL(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);
    
    
    @SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(PropostaLotesBD.class)
	Optional<Long> verificarSeHouveRejeicaoLicitacao(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);
}
