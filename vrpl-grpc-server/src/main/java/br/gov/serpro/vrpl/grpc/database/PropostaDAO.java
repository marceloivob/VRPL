package br.gov.serpro.vrpl.grpc.database;

import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.serpro.vrpl.grpc.database.bean.VrplAceitaDTO;

public interface PropostaDAO {

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(VrplAceitaDTO.class)
	Optional<VrplAceitaDTO> existePropostaVrplAceita(@Bind("idProposta") Long idProposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void excluirProposta(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

}
