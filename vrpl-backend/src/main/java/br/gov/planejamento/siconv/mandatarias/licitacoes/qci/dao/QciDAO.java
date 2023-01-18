package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business.MetaBDReducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.MetaQCIExternoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaQCIExternoDTO;

public interface QciDAO {

    @SqlQuery("SELECT * from siconv.vrpl_submeta where siconv.vrpl_submeta.id = :idSubmeta")
    @RegisterFieldMapper(SubmetaBD.class)
    SubmetaBD recuperarSubmetaVRPL(@Bind("idSubmeta") Long idSubmeta);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_submeta SET vl_repasse = :vlRepasse, vl_contrapartida = vl_total_licitado - :vlRepasse - :vlOutros, vl_outros = :vlOutros, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(SubmetaBD.class)
	@GetGeneratedKeys
	SubmetaBD alterarDadosSubmeta(@BindBean SubmetaBD submeta);

	@DefineUserInSession
    @SqlUpdate
    @UseStringTemplateSqlLocator
	void atualizaValorTotalLicitadoDaSubmetaByID(@BindBean SubmetaBD submeta);

    @SqlQuery
    @RegisterFieldMapper(value = SubmetaBD.class, prefix = "submeta")
    @RegisterFieldMapper(value = MetaBD.class, prefix = "meta")
    @RegisterFieldMapper(value = SubitemInvestimentoBD.class, prefix = "item")
    @UseRowReducer(MetaBDReducer.class)
    @UseStringTemplateSqlLocator
	List<MetaBD> recuperarQCIInternoByIdLicitacao(@Bind("idLicitacao") Long idLicitacao);

    @SqlQuery
	@RegisterFieldMapper(MetaQCIExternoDTO.class)
    @UseStringTemplateSqlLocator
	List<MetaQCIExternoDTO> recuperarMetasAtivasParaQCIExternoByPropFk(@BindBean SiconvPrincipal usuarioLogado,
			@Bind("versaoDaProposta") Long versaoDaProposta);

	@SqlQuery
	@RegisterFieldMapper(SubmetaQCIExternoDTO.class)
	@UseStringTemplateSqlLocator
	List<SubmetaQCIExternoDTO> recuperarSubMetasParaQCIExternoByMetaId(@BindBean SiconvPrincipal usuarioLogado,
			@Bind("versaoDaProposta") Long versaoDaProposta, @BindList("idsMetas") List<Long> idsMetas);

    @SqlBatch
    @UseStringTemplateSqlLocator
    @GetGeneratedKeys
    @RegisterBeanMapper(SubmetaBD.class)
    List<SubmetaBD> insertSubmetas(@BindBean Collection<SubmetaBD> lote);

    @SqlBatch
    @UseStringTemplateSqlLocator
    @GetGeneratedKeys
    @RegisterBeanMapper(MetaBD.class)
    List<MetaBD> insertMetas(@BindBean Collection<MetaBD> metas);

    @SqlQuery
    @RegisterFieldMapper(MetaBD.class)
    @UseStringTemplateSqlLocator
    List<MetaBD> recuperarMetaPorFkAnalise(@BindList("idsMetaAnalise") List<Long> idsMetaAnalise);

    @SqlQuery("  SELECT distinct submeta.id                                          \n"
            + "    from                                                              \n"
            + "      siconv.vrpl_submeta submeta,                                    \n"
            + "      siconv.vrpl_lote_licitacao lote,                                \n"
            + "      siconv.vrpl_licitacao                                           \n"
            + "    where                                                             \n"
            + "      submeta.vrpl_lote_licitacao_fk = lote.id             and        \n"
            + "      lote.licitacao_fk = vrpl_licitacao.id                 and       \n"
            + "      vrpl_licitacao.id = :id                             and         \n"
            + "      lote.numero_lote in (<numerosLotes>) ")
    List<Long> recuperarIdsSubmetasPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
            @BindList("numerosLotes") List<Long> numerosLotes);

	@DefineUserInSession
    @SqlUpdate("DELETE from siconv.vrpl_submeta where siconv.vrpl_submeta.id in (<idsSubmetas>)")
    void excluirSubmetaPorIds(@BindList("idsSubmetas") List<Long> idsSubmetas);

	@DefineUserInSession
    @SqlUpdate("delete from siconv.vrpl_meta where siconv.vrpl_meta.id in (select meta.id from siconv.vrpl_meta meta where meta.id not in (select siconv.vrpl_submeta.meta_fk from siconv.vrpl_submeta))")
    void removerMetasVRPLSemFilhos();

    @SqlQuery("select id from siconv.vrpl_meta where id not in (select meta_fk from siconv.vrpl_submeta)")
    List<Long> recuperarIdsMetasNaoRelacionadas();

    @SqlBatch
    @UseStringTemplateSqlLocator
    @GetGeneratedKeys
    @RegisterBeanMapper(SubitemInvestimentoBD.class)
	@DefineUserInSession
    List<SubitemInvestimentoBD> insertSubitensInvestimento(@BindBean Collection<SubitemInvestimentoBD> subItens);

    @SqlQuery
    @UseStringTemplateSqlLocator
    @RegisterBeanMapper(SubitemInvestimentoBD.class)
    List<SubitemInvestimentoBD> recuperarSubitemPorFkAnalise(
            @BindList("idsSubitemAnalise") List<Long> idsSubitemAnalise);

	@SqlQuery("select sub.* from siconv.vrpl_submeta sub, siconv.vrpl_lote_licitacao lote where sub.vrpl_lote_licitacao_fk = lote.id and lote.licitacao_fk = :licitacao.id")
	@RegisterBeanMapper(SubmetaBD.class)
	List<SubmetaBD> recuperarSubmetasDaLicitacao(@BindBean("licitacao") LicitacaoBD licitacao);

    @SqlBatch
	@DefineUserInSession
    @UseStringTemplateSqlLocator
    void atualizaLoteDaSubmeta(@BindBean Collection<SubmetaBD> submetas);

    @SqlQuery
    @UseStringTemplateSqlLocator
    Boolean verificaSubmetasMesmoTipoPorLote(@BindList("idsLotes") List<Long> idsLotes);

    @SqlQuery
    @UseStringTemplateSqlLocator
    Boolean verificaAcompanhamentoPoMesmoTipoPorLote(@BindList("idsLotes") List<Long> idsLotes);

    @SqlQuery
    @UseStringTemplateSqlLocator
    Boolean verificaSubmetaMesmoRegimePorLote(@BindList("idsLotes") List<Long> idsLotes);

}
