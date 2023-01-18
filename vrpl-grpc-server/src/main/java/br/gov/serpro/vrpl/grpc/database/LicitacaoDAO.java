package br.gov.serpro.vrpl.grpc.database;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.serpro.vrpl.grpc.PropostaRequest;
import br.gov.serpro.vrpl.grpc.database.bean.LicitacaoBD;

public interface LicitacaoDAO {

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(LicitacaoBD.class)
	List<LicitacaoBD> consultarPermissoesDasLicitacoesDaProposta(@BindBean PropostaRequest proposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void excluirFornecedorLicitacao(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void excluirAnexosLicitacao(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void excluirHistoricoLicitacao(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void excluirLicitacoes(@Bind("numeroProposta") long numeroProposta, @Bind("anoProposta") long anoProposta);

}
