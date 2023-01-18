package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao;

import java.util.Collection;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;

public interface FrenteObraDAO {


	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_frente_obra (nm_frente_obra, nr_frente_obra, po_fk, versao, adt_login, adt_data_hora, adt_operacao) VALUES (:nmFrenteObra,  :nrFrenteObra,  :poFk, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(FrenteObraBD.class)
    @GetGeneratedKeys
	FrenteObraBD inserirFrenteObra(@BindBean FrenteObraBD frenteObra);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_frente_obra SET nm_frente_obra = :nmFrenteObra, nr_frente_obra = :nrFrenteObra, po_fk = :poFk, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
    @RegisterFieldMapper(FrenteObraBD.class)
	@GetGeneratedKeys
	FrenteObraBD alterarFrenteObra(@BindBean FrenteObraBD frenteObra);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_frente_obra WHERE id = :id and versao = :versao")
	void excluirFrenteObra(@BindBean FrenteObraBD frenteObra);

	@SqlQuery("SELECT * FROM siconv.vrpl_frente_obra frente WHERE id = :id")
    @RegisterFieldMapper(FrenteObraBD.class)
	FrenteObraBD recuperarFrenteObraPorId(@BindBean FrenteObraBD frenteObra);

    @SqlQuery("SELECT frente.* FROM siconv.vrpl_frente_obra frente WHERE frente.po_fk = :idPO order by frente.nr_frente_obra")
    @RegisterFieldMapper(FrenteObraBD.class)
    List<FrenteObraBD> recuperarListaFrentesDeObraPorPoIdProposta(@Bind ("idPO") Long idPO);

    @DefineUserInSession
    @SqlBatch("INSERT INTO siconv.vrpl_frente_obra (nm_frente_obra, nr_frente_obra, po_fk, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :nmFrenteObra,  :nrFrenteObra,  :poFk, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(FrenteObraBD.class)
    @GetGeneratedKeys("id")
    Long[] insertFrentesDeObraBatch(@BindBean Collection<FrenteObraBD> frentesObra);

	@SqlQuery("SELECT MAX(nr_frente_obra) from siconv.vrpl_frente_obra where po_fk = :idPO")
    Long recuperarSequencialFrenteObraPorPo(@Bind("idPO") Long idPO);

    @SqlQuery("SELECT frente.* FROM siconv.vrpl_frente_obra frente WHERE frente.po_fk = :idPO order by frente.id")
    @RegisterFieldMapper(FrenteObraBD.class)
    List<FrenteObraBD> recuperarListaFrentesDeObraIdPo(@Bind("idPO") Long idPO);

    @SqlQuery
    @RegisterFieldMapper(FrenteObraBD.class)
    @UseStringTemplateSqlLocator
    List<FrenteObraBD> recuperarListaFrentesDeObraIdServico(@Bind("idServico") Long idServico);

    @SqlQuery
    @RegisterFieldMapper(FrenteObraBD.class)
    @UseStringTemplateSqlLocator
    List<FrenteObraBD> recuperarListaFrentesDeObraPoIdServico(@Bind("idServico") Long idServico);

	@SqlQuery("select distinct frente_obra.id 										\n"
			+ " from 																\n"
			+ "    siconv.vrpl_frente_obra frente_obra, 							\n"
			+ "    siconv.vrpl_po po, 												\n"
			+ "    siconv.vrpl_submeta submeta, 									\n"
			+ "    siconv.vrpl_lote_licitacao lote, 								\n"
			+ "    siconv.vrpl_licitacao 											\n"
            + " where 																\n"
			+ "    frente_obra.po_fk = po.id 					and 				\n"
			+ "    po.submeta_fk = submeta.id 					and 				\n"
			+ "    submeta.vrpl_lote_licitacao_fk = lote.id 	and 				\n"
			+ "    lote.licitacao_fk = vrpl_licitacao.id		and 				\n"
			+ "    vrpl_licitacao.id = :id 						and 				\n"
			+ "    lote.numero_lote in (<numerosLotes>)")
	List<Long> recuperarFrentesObraPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_frente_obra WHERE id in (<idsFrenteObra>)")
    void excluirFrentesObraPorIds(@BindList("idsFrenteObra") List<Long> idsFrenteObra);

}
