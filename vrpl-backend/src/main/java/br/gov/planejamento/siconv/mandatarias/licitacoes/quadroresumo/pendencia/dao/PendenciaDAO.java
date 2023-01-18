package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.dao;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;

public interface PendenciaDAO {

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_pendencia (descricao, in_resolvida, laudo_fk, submeta_fk, prazo, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :descricao,  :inResolvida,  :laudoFk,  :submetaFk, :prazo,  :versaoId,  :versaoNmEvento,  1, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(PendenciaBD.class)
	@GetGeneratedKeys
	PendenciaBD inserirPendencia(@BindBean PendenciaBD pendencia);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_pendencia SET descricao = :descricao, in_resolvida = :inResolvida, laudo_fk = :laudoFk, submeta_fk = :submetaFk, prazo = :prazo, versao_id = :versaoId, versao_nm_evento = :versaoNmEvento, versao_nr = 1, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(PendenciaBD.class)
	void alterarPendencia(@BindBean PendenciaBD pendencia);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_pendencia WHERE id = :id")
	void excluirPendencia(@Bind("id") Long id);

	@SqlQuery("SELECT * FROM siconv.vrpl_pendencia WHERE id = :id")
	@RegisterFieldMapper(PendenciaBD.class)
	PendenciaBD recuperarPendenciaPorId(@Bind("id") Long id);

	@DefineUserInSession
	@SqlBatch("INSERT INTO siconv.vrpl_pendencia (descricao, in_resolvida, laudo_fk, submeta_fk, prazo, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :descricao,  :inResolvida,  :laudoFk,  :submetaFk, :prazo,  :versaoId,  :versaoNmEvento,  :versaoNr, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(PendenciaBD.class)
	void inserirPendenciaBatch(@BindBean Collection<PendenciaBD> pendencias);
	
	@SqlQuery("SELECT * FROM siconv.vrpl_pendencia WHERE laudo_fk = :laudoFk")
	@RegisterFieldMapper(PendenciaBD.class)
	List<PendenciaBD> recuperarPendenciaPorLaudo(@Bind("laudoFk") Long laudoFk);

}
