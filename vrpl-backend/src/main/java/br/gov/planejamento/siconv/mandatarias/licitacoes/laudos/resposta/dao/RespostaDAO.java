package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.dao;

import java.util.Collection;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;

public interface RespostaDAO {


	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_resposta (laudo_fk, pergunta_fk, lote_fk, resposta, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :laudoFk,  :perguntaFk,  :loteFk, :resposta,  :versaoId,  :versaoNmEvento,  1, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(RespostaBD.class)
    @GetGeneratedKeys
    RespostaBD inserirResposta(@BindBean RespostaBD resposta);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_resposta SET laudo_fk = :laudoFk, pergunta_fk = :perguntaFk, resposta = :resposta, versao_id = :versaoId, versao_nm_evento = :versaoNmEvento, versao_nr = :versaoNr, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
    @RegisterFieldMapper(RespostaBD.class)
    void alterarResposta(@BindBean RespostaBD resposta);

	@DefineUserInSession
    @SqlUpdate("DELETE FROM siconv.vrpl_resposta WHERE id = :id")
    void excluirResposta(@Bind ("id") Long id);
	
	@DefineUserInSession
    @SqlUpdate("DELETE FROM siconv.vrpl_resposta WHERE laudo_fk = :idLaudo")
    void excluirRespostasPorLaudo(@Bind ("idLaudo") Long idLaudo);

	@DefineUserInSession
	@SqlUpdate("delete from siconv.vrpl_resposta \n where lote_fk in (      								\n"
			+ "	select lote.id                                                                      		\n"
			+ "        from siconv.vrpl_lote_licitacao lote                                                	\n"
			+ "        left join siconv.vrpl_submeta submeta on submeta.vrpl_lote_licitacao_fk = lote.id   	\n"
			+ "        where submeta.id is null)")
	void excluirRespostasSemLote();

    @SqlQuery("SELECT * FROM siconv.vrpl_resposta WHERE id = :id")
    @RegisterFieldMapper(RespostaBD.class)
    RespostaBD recuperarRespostaPorId(@Bind ("id") Long id);

	@DefineUserInSession
	@SqlBatch("INSERT INTO siconv.vrpl_resposta (laudo_fk, pergunta_fk, lote_fk, resposta, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :laudoFk,  :perguntaFk, :loteFk, :resposta,  :versaoId,  :versaoNmEvento,  1, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(RespostaBD.class)
    void inserirRespostaBatch (@BindBean Collection<RespostaBD> respostas);

	@SqlQuery("SELECT * FROM siconv.vrpl_resposta WHERE laudo_fk = :idLaudo")
    @RegisterFieldMapper(RespostaBD.class)
	List<RespostaBD> recuperarRespostaPorLaudo(@Bind ("idLaudo") Long idLaudo);

}
