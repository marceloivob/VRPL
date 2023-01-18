package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;

public interface HistoricoLicitacaoDAO {

    @SqlUpdate
	@DefineUserInSession
    @UseStringTemplateSqlLocator
	void insertHistoricoLicitacao(@BindBean HistoricoLicitacaoBD historico);

    @SqlQuery("SELECT * from siconv.vrpl_historico_licitacao h where h.licitacao_fk = :identificadorDaLicitacao order by h.dt_registro desc")
    @RegisterFieldMapper(HistoricoLicitacaoBD.class)
    List<HistoricoLicitacaoBD> findHistoricoLicitacaoByIdLicitacao(
            @Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);
    
    @SqlQuery("SELECT * from siconv.vrpl_historico_licitacao h where h.licitacao_fk = :identificadorDaLicitacao and h.in_evento = :evento order by h.dt_registro desc")
    @RegisterFieldMapper(HistoricoLicitacaoBD.class)
    List<HistoricoLicitacaoBD> findHistoricoByIdLicitacaoEvento(
            @Bind("identificadorDaLicitacao") Long identificadorDaLicitacao, @Bind("evento") String evento);
    

    
    @SqlQuery("SELECT count(*) from siconv.vrpl_historico_licitacao h "
    		+ "WHERE h.licitacao_fk = :idLicitacao and h.in_evento = :eventoCancelamento")
    Long consultarSeHouveCancelamentos(@Bind("idLicitacao") Long idLicitacao, @Bind("eventoCancelamento") String eventoCancelamento);
    
    
    @SqlQuery("  select distinct h1.* from \n" + 
    		"     siconv.vrpl_historico_licitacao h1 \n" + 
    		"  where \n" + 
    		"     h1.licitacao_fk = :idLicitacao and \n" + 
    		"     h1.dt_registro >= (select max(h2.dt_registro) \n" + 
    		"           from siconv.vrpl_historico_licitacao h2 \n" + 
    		"           where h2.licitacao_fk = :idLicitacao and h2.in_evento = :eventoCancelamento) and\n" + 
    		"          \n" + 
    		"     h1.in_evento = :evento \n" + 
    		"     order by h1.dt_registro desc")
    @RegisterFieldMapper(HistoricoLicitacaoBD.class)
    List<HistoricoLicitacaoBD> findHistoricoByIdLicitacaoEventoConsiderandoCancelamento(
            @Bind("idLicitacao") Long idLicitacao, @Bind("eventoCancelamento") String eventoCancelamento, @Bind("evento") String evento);

	
    
    @SqlUpdate("delete from siconv.vrpl_historico_licitacao where licitacao_fk = :idLicitacao and versao = :versao")
	@DefineUserInSession
	@GetGeneratedKeys
    Long deleteHistorico(@Bind("idLicitacao") Long idLicitacao, @Bind("versao") Long versao);
    
    

}
