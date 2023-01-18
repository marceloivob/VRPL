package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;

public interface CacheDAO {

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(Resultado.class)
	List<Resultado> recuperarRegistrosPermitidos(@BindBean SiconvPrincipal usuarioLogado);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	void definirUsuarioLogado(@Define("cpf") String cpf);

	@SqlQuery
	@UseStringTemplateSqlLocator
	String consultarUsuarioLogado();

}
