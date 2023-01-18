package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

public interface PropostaDAO {

	@SqlQuery
	@RegisterBeanMapper(value = LicitacaoBD.class)
	@UseStringTemplateSqlLocator
	Boolean existePropostaVrpl(@BindBean SiconvPrincipal usuarioLogado);
	
	@SqlQuery
	@UseStringTemplateSqlLocator
	Boolean existePropostaVrplAceita(@Bind("idProposta") Long idProposta);

	
	@SqlQuery
	@RegisterBeanMapper(PropostaBD.class)
	@UseStringTemplateSqlLocator
	PropostaBD recuperaUltimaVersaoDaProposta(@BindBean SiconvPrincipal usuarioLogado);

	@SqlQuery
	@RegisterBeanMapper(PropostaBD.class)
	@UseStringTemplateSqlLocator
	PropostaBD recuperaVersaoDaProposta(@BindBean SiconvPrincipal usuarioLogado, @Bind("versao") Long versao);

	@SqlQuery
	@UseStringTemplateSqlLocator
	List<Long> recuperaVersoesDaProposta(@BindBean SiconvPrincipal usuarioLogado);

	@SqlQuery
	@RegisterBeanMapper(PropostaBD.class)
	@UseStringTemplateSqlLocator
	PropostaBD loadById(@Bind("identificadorDaProposta") Long identificadorDaProposta);

}
