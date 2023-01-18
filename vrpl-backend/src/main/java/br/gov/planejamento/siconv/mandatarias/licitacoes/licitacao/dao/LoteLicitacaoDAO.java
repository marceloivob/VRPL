package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao;

import java.util.Collection;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LoteReducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

public interface LoteLicitacaoDAO {

	@SqlQuery
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	List<LoteBD> findLotesByIdLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);

	@SqlQuery
	@RegisterBeanMapper(value = LoteBD.class, prefix = "lote")
	@RegisterBeanMapper(value = SubmetaBD.class, prefix = "submeta")
	@RegisterBeanMapper(value = MetaBD.class, prefix = "meta")
	@UseRowReducer(LoteReducer.class)
	@UseStringTemplateSqlLocator
	List<LoteBD> findLotesComAssociacoesByIdLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);
	
	@SqlQuery
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	List<LoteBD> findLotesSemSubmeta(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);

	@SqlBatch
	@GetGeneratedKeys
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	@DefineUserInSession
	List<LoteBD> insertLotesLicitacao(@BindBean Collection<LoteBD> lote);

	@SqlBatch
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void updateLoteLicitacao(@Bind("idLicitacao") Long idLicitacao, @Bind("idFornecedor") Long idFornecedor,
			@BindBean("lote") Collection<LoteBD> lote);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void removeLoteNaoSelecionadoLicitacao(@Bind("idLicitacao") Long idLicitacao,
			@Bind("idFornecedor") Long idFornecedor, @BindList("idsLoteSelecionados") List<Long> idsLoteSelecionados);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void removeTodosLoteLicitacaoFornecedor(@Bind("idLicitacao") Long idLicitacao,
			@Bind("idFornecedor") Long idFornecedor);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void removeTodosLoteLicitacao(@Bind("idLicitacao") Long idLicitacao);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void deleteLoteLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@SqlQuery
	@RegisterBeanMapper(value = LoteBD.class, prefix = "lote")
	@RegisterBeanMapper(value = SubmetaBD.class, prefix = "submeta")
	@RegisterBeanMapper(value = MetaBD.class, prefix = "meta")
	@UseRowReducer(LoteReducer.class)
	@UseStringTemplateSqlLocator
	List<LoteBD> findLotesAtivosByIdPropostaSiconv(@BindBean SiconvPrincipal usuarioLogado,
			@Bind("versaoDaProposta") Long versaoDaProposta);

	@SqlQuery
	@RegisterBeanMapper(value = LoteBD.class, prefix = "lote")
	@RegisterBeanMapper(value = SubmetaBD.class, prefix = "submeta")
	@RegisterBeanMapper(value = MetaBD.class, prefix = "meta")
	@UseRowReducer(LoteReducer.class)
	@UseStringTemplateSqlLocator
	List<LoteBD> findLotesRejeitadosByIdPropostaSiconv(@BindBean SiconvPrincipal usuarioLogado,
			@Bind("versaoDaProposta") Long versaoDaProposta);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void deleteLoteSemSubmeta();

	@SqlQuery
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	LoteBD recuperarLotePorPo(@Bind("idPO") Long idPO);
	
	@SqlQuery
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	LoteBD recuperarLotePorId(@Bind("id") Long id);
	
	@SqlQuery
	@UseStringTemplateSqlLocator
	boolean existeLoteAssociadoAoFornecedor(@Bind("idFornecedor") Long idFornecedor);

}
