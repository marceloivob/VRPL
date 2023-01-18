package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.stringtemplate4.UseStringTemplateEngine;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LicitacaoLoteReducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LicitacaoReducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;

public interface LicitacaoDAO {

	@SqlQuery
	@RegisterFieldMapper(value = LicitacaoBD.class, prefix = "lic")
	@RegisterFieldMapper(value = LoteBD.class, prefix = "lote")
	@RegisterFieldMapper(value = SubmetaBD.class, prefix = "submeta")
	@RegisterFieldMapper(value = MetaBD.class, prefix = "meta")
	@RegisterFieldMapper(value = FornecedorBD.class, prefix = "for")
	@UseRowReducer(LicitacaoLoteReducer.class)
	@UseStringTemplateEngine
	@UseStringTemplateSqlLocator
	List<LicitacaoBD> recuperarLicitacoesAssociadasDaProposta(@BindBean("proposta") PropostaBD proposta,
			@Define("licitacoesAtivas") Boolean licitacoesAtivas);

	@Log
	@SqlQuery
	@RegisterFieldMapper(value = LicitacaoBD.class, prefix = "lic")
	@RegisterFieldMapper(value = LoteBD.class, prefix = "lote")
	@RegisterFieldMapper(value = SubmetaBD.class, prefix = "submeta")
	@RegisterFieldMapper(value = MetaBD.class, prefix = "meta")
	@RegisterFieldMapper(value = FornecedorBD.class, prefix = "for")
	@UseRowReducer(LicitacaoLoteReducer.class)
	@UseStringTemplateSqlLocator
	LicitacaoBD findLicitacaoByIdLicitacao(@Bind("id") Long idLicitacao);

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterFieldMapper(LicitacaoBD.class)
	List<LicitacaoBD> recuperarLicitacoesAtuaisDaProposta(@BindBean SiconvPrincipal usuarioLogado);
	
	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterFieldMapper(LicitacaoBD.class)
	List<LicitacaoBD> recuperarLicitacoesDaPropostaPorVersao(@BindBean SiconvPrincipal usuarioLogado, @Bind("versao") Long versao);

	@SqlQuery("SELECT * FROM SICONV.VRPL_LICITACAO WHERE VRPL_LICITACAO.ID = :id")
	@RegisterFieldMapper(LicitacaoBD.class)
	LicitacaoBD findLicitacaoById(@Bind("id") Long idLicitacao);

	@SqlUpdate
	@UseStringTemplateSqlLocator
	@DefineUserInSession
	@RegisterFieldMapper(LicitacaoBD.class)
	@GetGeneratedKeys
	LicitacaoBD insertLicitacao(@BindBean LicitacaoBD licitacao);

	@SqlBatch
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	@DefineUserInSession
	@RegisterFieldMapper(LicitacaoBD.class)
	List<LicitacaoBD> insertLicitacao(@BindBean Collection<LicitacaoBD> licitacoes);

	@SqlBatch
	@UseStringTemplateSqlLocator
	@DefineUserInSession
	@RegisterBeanMapper(LicitacaoBD.class)
	boolean[] atualizaLicitacao(@BindBean List<LicitacaoBD> licitacoesASeremAtualizadas);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_licitacao set in_situacao = :situacaoDaLicitacao, versao = (:versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora= LOCALTIMESTAMP, adt_operacao='UPDATE'  where id = :id")
	boolean updateSituacaoDaLicitacao(@BindBean LicitacaoBD licitacao);

	@SqlQuery
	@UseStringTemplateSqlLocator
	BigDecimal recuperarValorRepassePorListaLicitacoes(@BindList("idsLicitacoes") List<Long> idsLicitacoes);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_submeta set in_situacao = :novoHistorico.situacaoDaLicitacao, versao = (versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora= LOCALTIMESTAMP, adt_operacao='UPDATE' where vrpl_lote_licitacao_FK IN (select id from siconv.vrpl_lote_licitacao where licitacao_fk = :novoHistorico.identificadorDaLicitacao)")
	boolean updateSituacaoSubmetasAssociadasDaLicitacao(@BindBean("novoHistorico") HistoricoLicitacaoBD novoHistorico);
	
	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_submeta set in_situacao = :situacao, versao = (versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora= LOCALTIMESTAMP, adt_operacao='UPDATE' where vrpl_lote_licitacao_FK IN (select id from siconv.vrpl_lote_licitacao where licitacao_fk = :idLicitacao)")
	boolean updateSituacaoSubmetasAssociadasDaLicitacaoPorLicitacao(@Bind("situacao") String situacao, @Bind("idLicitacao") Long idLicitacao);

	@SqlBatch
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	@DefineUserInSession
	@RegisterBeanMapper(FornecedorBD.class)
	List<FornecedorBD> insertFornecedores(@BindBean Collection<FornecedorBD> fornecedor);

	@SqlBatch
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	@DefineUserInSession
	@RegisterBeanMapper(FornecedorBD.class)
	List<FornecedorBD> atualizaFornecedores(@BindBean Collection<FornecedorBD> fornecedor);

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(FornecedorBD.class)
	List<FornecedorBD> recuperaFornecedoresDasLicitacoes(@BindList("idsLicitacoes") List<Long> licitacoes);

	@SqlQuery
	@UseStringTemplateSqlLocator
	BigDecimal recuperarSomatorioDosValoresDasPOsLicitadas(@Bind("idLicitacao") Long idLicitacao,
			@Bind("versao") Long versao);
	
	@SqlQuery
	@UseStringTemplateSqlLocator
	Boolean recuperarIndicadorSocialLicitacao(@Bind("id") Long idLicitacao);

	@SqlQuery("select * from siconv.vrpl_fornecedor where id = :id")
	@RegisterBeanMapper(FornecedorBD.class)
	FornecedorBD findFornecedorPorId(@Bind("id") Long idFornecedor);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO - Remover métodos usados apenas nos Testes
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_licitacao where ID = :id")
	void delete(@Bind("id") Long idLicitacao);
	
	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_fornecedor where licitacao_fk = :id")
	void deleteFornecedorPorIdLicitacao(@Bind("id") Long idLicitacao);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_fornecedor where ID in (<idsFornecedores>)")
	void deleteFornecedores(@BindList("idsFornecedores") List<Long> idsFornecedores);
	
	@SqlQuery
	@RegisterFieldMapper(value = LicitacaoBD.class, prefix = "lic")
	@RegisterFieldMapper(value = LoteBD.class, prefix = "lote")
	@UseRowReducer(LicitacaoReducer.class)
	@UseStringTemplateSqlLocator
	List<LicitacaoBD> recuperarOutrasLicitacoesRejeitadasComMesmosNumerosLotes(@BindBean LicitacaoBD licitacao, @BindList("nrLotes") List<Long> nrLotes);
	
	@DefineUserInSession
	@SqlUpdate("update siconv.vrpl_fornecedor set obsoleto = true, versao = (versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora= LOCALTIMESTAMP, adt_operacao='UPDATE' where id = :idFornecedor")
	void fornecedorObsoleto(@Bind("idFornecedor") Long idFornecedor);
}