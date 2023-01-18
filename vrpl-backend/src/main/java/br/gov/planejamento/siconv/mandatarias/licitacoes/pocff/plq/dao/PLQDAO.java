package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.FrenteObraComDetalhesBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.ServicoComEventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;

public interface PLQDAO {
    
    
    @SqlQuery("SELECT * FROM siconv.vrpl_macro_servico macro WHERE macro.po_fk = :id order by macro.nr_macro_servico")
    @RegisterFieldMapper(MacroServicoBD.class)
    public List<MacroServicoBD> recuperarMacroServicoPorIdPO(@Bind("id") Long id);
    

    @SqlQuery("SELECT servico.* FROM siconv.vrpl_servico servico WHERE macro_servico_fk IN (SELECT id FROM siconv.vrpl_macro_servico WHERE po_fk = :poId) order by nr_servico")
    @RegisterFieldMapper(ServicoBD.class)
    List<ServicoBD> recuperarServicosPorPo(@Bind("poId") Long poId);
    
    @SqlQuery("SELECT servico.*, evento.nr_evento evento_numero, evento.nm_evento evento_nome "
            + " FROM siconv.vrpl_servico servico, "
            + "      siconv.vrpl_evento evento,"
            + "      siconv.vrpl_macro_servico macro "
            + " WHERE servico.macro_servico_fk = macro.id AND "
            + "       macro.po_fk = :poId AND "
            + "       servico.evento_fk is not null AND "
            + "       servico.evento_fk = evento.id "
            + " ORDER BY servico.nr_servico, evento_numero")
    @RegisterFieldMapper(ServicoComEventoBD.class)
    List<ServicoComEventoBD> recuperarServicosComEventoPorPo(@Bind("poId") Long poId);
    
    
    //recuperar frentes Obra de uma PO
    @SqlQuery("SELECT frente.id id_frente_obra, servico.id id_servico, frente.nr_frente_obra, frente.nm_frente_obra, servico_frente.qt_itens "
            + "FROM siconv.vrpl_servico servico, "
            + "     siconv.vrpl_frente_obra frente, "
            + "     siconv.vrpl_servico_frente_obra servico_frente,"
            + "     siconv.vrpl_macro_servico macro "
            + " WHERE servico_frente.frente_obra_fk = frente.id AND "
            + "       servico_frente.servico_fk = servico.id AND "
            + "       servico.macro_servico_fk = macro.id AND"
            + "       macro.po_fk = :poId order by id_servico, frente.nr_frente_obra")
    @RegisterFieldMapper(FrenteObraComDetalhesBD.class)
    List<FrenteObraComDetalhesBD> recuperarFrentesDeObraComDetalhesPorPo(@Bind("poId") Long poId);
    
    
}
