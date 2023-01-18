package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

public interface LaudoDAO {

	@SqlQuery("SELECT * FROM siconv.vrpl_laudo WHERE id = :id")
	@RegisterFieldMapper(LaudoBD.class)
	LaudoBD recuperarLaudoPorId(@Bind("id") Long id);

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_laudo (licitacao_fk, versao_nr, versao_nm_evento, adt_login, adt_operacao, template_fk, in_resultado, versao_id, in_status, versao, adt_data_hora) VALUES (  :licitacaoFk, :versaoNr, :versaoNmEvento, current_setting('vrpl.cpf_usuario'), 'INSERT', :templateFk, :inResultado, :versaoId, :inStatus, 1, LOCALTIMESTAMP )")
    @RegisterFieldMapper(LaudoBD.class)
    @GetGeneratedKeys
	LaudoBD inserirLaudo(@BindBean LaudoBD laudo);

	@SqlQuery("SELECT laudo.id, laudo.licitacao_fk, laudo.template_fk, laudo.in_status, laudo.in_resultado, laudo.versao, laudo.adt_login FROM siconv.vrpl_laudo laudo inner join siconv.vrpl_template_laudo templ on templ.id = laudo.template_fk WHERE licitacao_fk = :idLicitacao and templ.tipo = :tipoLaudo ")
	@RegisterFieldMapper(LaudoBD.class)
	LaudoBD recuperarLaudoPorLicitacao(@Bind("idLicitacao") Long idLicitacao, @Bind("tipoLaudo") String tipoLaudo);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_laudo SET licitacao_fk = :licitacaoFk, versao_nr = 1, versao_nm_evento = :versaoNmEvento, adt_login = current_setting('vrpl.cpf_usuario'), adt_operacao = 'UPDATE', template_fk = :templateFk, in_resultado = :inResultado, versao_id = :versaoId, in_status = :inStatus, versao = (:versao + 1), adt_data_hora = LOCALTIMESTAMP WHERE id = :id ")
    @RegisterFieldMapper(LaudoBD.class)
    @GetGeneratedKeys
	LaudoBD atualizarLaudo(@BindBean LaudoBD laudo);

	@SqlQuery
	@RegisterBeanMapper(PropostaBD.class)
	@RegisterBeanMapper(LaudoBD.class)
	@UseStringTemplateSqlLocator
	List<LaudoBD> existeParecerEmitidoViavelParaALicitacao(@BindBean LicitacaoBD proposta);

	@SqlQuery
	@RegisterBeanMapper(PropostaBD.class)
	@RegisterBeanMapper(LaudoBD.class)
	@UseStringTemplateSqlLocator
	List<LaudoBD> existeParecerEmitidoParaALicitacao(@BindBean LicitacaoBD proposta);

	@SqlQuery
	@UseStringTemplateSqlLocator
	Boolean existeAlgumParecerEmitidoParaALicitacao(@Bind("idLicitacao") Long idLicitacao);

	@DefineUserInSession
	@SqlUpdate("delete from siconv.vrpl_laudo WHERE id = :id ")
    @GetGeneratedKeys
	Long excluirLaudo(@Bind("id") Long id);
}
