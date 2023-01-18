package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraDetalheBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.TotalizadorMesBD;

public interface EventoFrenteObraDAO {

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_evento_frente_obra (evento_fk, frente_obra_fk, nr_mes_conclusao, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :eventoFk,  :frenteObraFk,  :nrMesConclusao,  1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(EventoFrenteObraBD.class)
    @GetGeneratedKeys
	EventoFrenteObraBD inserirEventoFrenteObra(@BindBean EventoFrenteObraBD eventoFrenteObra);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_evento_frente_obra SET nr_mes_conclusao = :nrMesConclusao, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE evento_fk = :eventoFk AND frente_obra_fk = :frenteObraFk")
	void alterarMesConclusaoEventoFrenteObra(@BindBean EventoFrenteObraBD eventoFrenteObra);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_evento_frente_obra SET evento_fk = :eventoFk, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE frente_obra_fk = :frenteObraFk")
	void alterarEventoDaFrenteObra(@BindBean EventoFrenteObraBD eventoFrenteObra);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_evento_frente_obra WHERE evento_fk = (SELECT evento.id FROM siconv.vrpl_evento evento WHERE evento.id = :idEvento AND evento.versao = :versao)")
	void excluirEventoFrenteObraPorEvento(@Bind("idEvento") Long idEvento, @Bind("versao") Long versao);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_evento_frente_obra WHERE evento_fk = :eventoFk AND frente_obra_fk = :frenteObraFk")
    void excluirEventoFrenteObra(@Bind("eventoFk") Long eventoFk, @Bind("frenteObraFk") Long frenteObraFk);

    @SqlQuery("SELECT * FROM siconv.vrpl_evento_frente_obra evento WHERE evento_fk = :eventoFk AND frente_obra_fk = :frenteObraFk")
    @RegisterFieldMapper(EventoFrenteObraBD.class)
    EventoFrenteObraBD recuperarEventoFrenteObraPorId(@Bind("eventoFk") Long eventoFk, @Bind("frenteObraFk") Long frenteObraFk);

    @DefineUserInSession
    @SqlUpdate("DELETE FROM siconv.vrpl_evento_frente_obra WHERE evento_fk  in (<idsEventos>)")
    void excluirEventosFrenteObraPorIdsEventos(@BindList("idsEventos") List<Long> idsEventos);


    @SqlQuery("  SELECT sum(round((servico.vl_preco_unitario_licitado * srv_ftr.qt_itens)::numeric, 2)) total, evt_frt.nr_mes_conclusao mes\n" +
            "         from   \n" +
            "         siconv.vrpl_evento_frente_obra evt_frt,   \n" +
            "         siconv.vrpl_evento evento,   \n" +
            "         siconv.vrpl_frente_obra frente,   \n" +
            "         siconv.vrpl_servico_frente_obra srv_ftr,   \n" +
            "         siconv.vrpl_servico servico,   \n" +
            "         siconv.vrpl_macro_servico macro,   \n" +
            "         siconv.vrpl_po po\n" +
            "         where   \n" +
            "         po.id = :idPO and\n" +
            "         evento.po_fk = po.id and   \n" +
            "         frente.po_fk = po.id and   \n" +
            "         macro.po_fk = po.id and   \n" +
            "         servico.macro_servico_fk = macro.id and\n" +
            "         srv_ftr.servico_fk = servico.id and   \n" +
            "         srv_ftr.frente_obra_fk = frente.id and   \n" +
            "         srv_ftr.frente_obra_fk = evt_frt.frente_obra_fk and\n" +
            "         evt_frt.evento_fk = evento.id and   \n" +
            "         evt_frt.frente_obra_fk = frente.id and\n" +
            "         servico.evento_fk = evento.id\n" +
            "         group by evt_frt.nr_mes_conclusao order by evt_frt.nr_mes_conclusao ")
    @RegisterFieldMapper(TotalizadorMesBD.class)
    List<TotalizadorMesBD> recuperarTotalizadorPorMes (@Bind("idPO") Long idPO);

    @SqlQuery(" select distinct evento.nm_evento, \n" +
    "       evento.id id_evento, \n" +
    "       evento.nr_evento, \n" +
    "       frente_obra.id id_frente_obra, \n" +
    "       frente_obra.nm_frente_obra, \n" +
    "       frente_obra.nr_frente_obra, \n" +
    "       frente_obra.versao versao_frente_obra, \n" +
    "       evt_frt.nr_mes_conclusao, \n" +
    "       evt_frt.versao \n" +
    " from \n" +
    "   siconv.vrpl_evento_frente_obra evt_frt, \n" +
    "   siconv.vrpl_evento evento, \n" +
    "   siconv.vrpl_frente_obra frente_obra, \n" +
    "   siconv.vrpl_servico servico   \n" +
    " where \n" +
    "   evento.id = evt_frt.evento_fk and \n" +
    "   servico.evento_fk = evento.id and \n" +
    "   frente_obra.id = evt_frt.frente_obra_fk and \n" +
    "   evento.po_fk = :idPO and \n" +
    "   frente_obra.po_fk = :idPO order by evento.nr_evento, frente_obra.nr_frente_obra")
    @RegisterFieldMapper(EventoFrenteObraDetalheBD.class)
    List<EventoFrenteObraDetalheBD> recuperarEventosFrenteObraComDetalhe(@Bind("idPO") Long idPO);

    @SqlQuery("SELECT * FROM siconv.vrpl_evento_frente_obra evento WHERE evento_fk = :eventoFk")
    @RegisterFieldMapper(EventoFrenteObraBD.class)
    List<EventoFrenteObraBD> recuperarEventoFrenteObraPorIdEvento(@Bind("eventoFk") Long eventoFk);
    
    @SqlQuery("SELECT * FROM siconv.vrpl_evento_frente_obra WHERE frente_obra_fk = :frenteObraFk")
    @RegisterFieldMapper(EventoFrenteObraBD.class)
    List<EventoFrenteObraBD> recuperarEventoFrenteObraPorIdFrenteObra(@Bind("frenteObraFk") Long frenteObraFK);

    @SqlQuery(" select evento.nm_evento, \n" +
            "       evento.id id_evento, \n" +
            "       evento.nr_evento, \n" +
            "       frente_obra.id id_frente_obra, \n" +
            "       frente_obra.nm_frente_obra, \n" +
            "       frente_obra.nr_frente_obra, \n" +
            "       evt_frt.nr_mes_conclusao, \n" +
            "       evt_frt.versao \n" +
            " from \n" +
            "   siconv.vrpl_evento_frente_obra evt_frt, \n" +
            "   siconv.vrpl_evento evento, \n" +
            "   siconv.vrpl_frente_obra frente_obra \n" +
            " where \n" +
            "   evento.id = :idEvento and \n" +
            "   evento.id = evt_frt.evento_fk and \n" +
            "   frente_obra.id = evt_frt.frente_obra_fk \n" +
            "   order by frente_obra.nr_frente_obra")
    @RegisterFieldMapper(EventoFrenteObraDetalheBD.class)
    List<EventoFrenteObraDetalheBD> recuperarTodasAsFrentesObraAssociadasAUmEvento(@Bind("idEvento")Long idEvento);
    
	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_evento_frente_obra WHERE frente_obra_fk = (SELECT frente.id FROM siconv.vrpl_frente_obra frente WHERE frente.id = :idFrente AND frente.versao = :versao)")
    void excluirEventoFrenteObraPorFrente(@Bind("idFrente") Long idFrente, @Bind("versao") Long versao);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_evento_frente_obra vefo SET nr_mes_conclusao = 0, versao = (versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE vefo.nr_mes_conclusao = :nrMesConclusao and vefo.frente_obra_fk in (select fo.id from siconv.vrpl_frente_obra fo where fo.po_fk = :idPO)")
    void apagarNumeroMesConclusaoDoEventoFrenteObraDaPO(@Bind("nrMesConclusao") Long nrMesConclusao, @Bind("idPO") Long idPO);

}
