package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao;

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

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;

public interface EventoDAO {

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_evento (nm_evento, po_fk, nr_evento, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :nomeEvento, :idPo, :numeroEvento, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(EventoBD.class)
    @GetGeneratedKeys
	EventoBD inserirEvento(@BindBean EventoBD evento);

	@SqlQuery("SELECT evento.* FROM siconv.vrpl_evento evento WHERE evento.po_fk = :idPO order by evento.nr_evento")
	@RegisterFieldMapper(EventoBD.class)
	List<EventoBD> recuperarListaEventosVRPLIdPo(@Bind("idPO") Long idPO);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_evento SET nm_evento = :nomeEvento, nr_evento = :numeroEvento, versao = (:versao + 1), adt_data_hora = LOCALTIMESTAMP, adt_login = current_setting('vrpl.cpf_usuario'), adt_operacao = 'UPDATE' WHERE id = :id and versao = :versao")
    @RegisterFieldMapper(EventoBD.class)
	void alterarEvento(@BindBean EventoBD evento);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_evento WHERE id = :idEvento and versao = :versao")
	void excluirEvento(@Bind("idEvento") Long idEvento, @Bind("versao") Long versao);

	@SqlQuery("SELECT evento.* FROM siconv.vrpl_evento evento WHERE evento.id = :idEvento")
    @RegisterFieldMapper(EventoBD.class)
	EventoBD recuperarEventoPorId(@Bind("idEvento") Long idEvento);

    @SqlQuery(" SELECT licitacao.proposta_fk                                " +
            " FROM                                " +
            "    siconv.vrpl_evento evento,   " +
            "    siconv.vrpl_po po,   " +
            "    siconv.vrpl_submeta submeta,   " +
            "    siconv.vrpl_lote_licitacao lote,   " +
            "    siconv.vrpl_licitacao licitacao  " +
            " WHERE                         " +
            "     evento.id = :idEvento AND  " +
            "     evento.po_fk = po.id AND  " +
            "     po.submeta_fk = submeta.id AND   " +
            "     submeta.vrpl_lote_licitacao_fk = lote.id AND   " +
            "     lote.licitacao_fk = licitacao.id  ")
    Long recuperarIdPropostaAssociadaAoEvento(@Bind("idEvento") Long idEvento);

    @SqlQuery(" SELECT MAX(nr_evento) FROM siconv.vrpl_evento where po_fk = :idPO ")
    Long recuperarSequencialEventoPorPo(@Bind("idPO") Long idPO);
    
    @SqlQuery(" SELECT * FROM siconv.vrpl_evento where po_fk = :idPO ")
    @RegisterFieldMapper(EventoBD.class)
    List<EventoBD> recuperarEventosPorPo(@Bind("idPO") Long idPO);

    @DefineUserInSession
    @SqlBatch("INSERT INTO siconv.vrpl_evento (nm_evento, po_fk, nr_evento, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :nomeEvento, :idPo, :numeroEvento, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @GetGeneratedKeys("id")
    Long[] insertEventosBatch(@BindBean Collection<EventoBD> eventos);

    // FIXME mover essa rotina para EventoFrenteObraDAO
    @DefineUserInSession
    @SqlBatch("INSERT INTO siconv.vrpl_evento_frente_obra (evento_fk, frente_obra_fk, nr_mes_conclusao, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :eventoFk, :frenteObraFk, :nrMesConclusao, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    void insertEventosFrenteObra(@BindBean Collection<EventoFrenteObraBD> eventosFrenteObra);

	@SqlQuery("  SELECT distinct evento.id     											\n"
			+ "  from     																\n"
			+ "		siconv.vrpl_evento evento,    										\n"
			+ "     siconv.vrpl_po po,     												\n"
			+ "     siconv.vrpl_submeta submeta,     									\n"
			+ "     siconv.vrpl_lote_licitacao lote,     								\n"
			+ "     siconv.vrpl_licitacao 												\n"
			+ "	where     																\n"
			+ "		evento.po_fk = po.id 								and     		\n"
			+ "		po.submeta_fk = submeta.id 							and     		\n"
			+ "		submeta.vrpl_lote_licitacao_fk = lote.id 			and     		\n"
			+ "     lote.licitacao_fk = vrpl_licitacao.id				and 			\n"
			+ "     vrpl_licitacao.id = :id 							and 			\n"
			+ "		lote.numero_lote in (<numerosLotes>)  ")
	List<Long> recuperarEventosPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_evento WHERE id in (<idsEventos>)")
    void excluirEventosPorIds(@BindList("idsEventos") List<Long> idsEventos);

}
