package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

public interface UpdatePropostaDAO {

	@SqlUpdate
	@GetGeneratedKeys
	@RegisterBeanMapper(PropostaBD.class)
	@UseStringTemplateSqlLocator
	@DefineUserInSession
	PropostaBD inserirProposta(@BindBean PropostaBD proposta);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	@DefineUserInSession
	@RegisterBeanMapper(PropostaBD.class)
	void atualizaDadosProposta(@BindBean PropostaBD proposta);
}
