package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;

public interface ServicoFrenteObraAnaliseDAO {


    @SqlUpdate("INSERT INTO siconv.vrpl_servico_frente_obra_analise (nm_frente_obra, nr_frente_obra, qt_itens, servico_fk, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :nmFrenteObra,  :nrFrenteObra,  :qtItens,  :servicoFk,  :versaoId,  :versaoNmEvento,  1, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    @DefineUserInSession
    @GetGeneratedKeys
    ServicoFrenteObraAnaliseBD inserirServicoFrenteObraAnalise(@BindBean ServicoFrenteObraAnaliseBD servicoFrenteObraAnalise);

    @SqlUpdate("UPDATE siconv.vrpl_servico_frente_obra_analise SET nm_frente_obra = :nmFrenteObra, nr_frente_obra = :nrFrenteObra, qt_itens = :qtItens, servico_fk = :servicoFk, versao_id = :versaoId, versao_nm_evento = :versaoNmEvento, versao_nr = :versaoNr, versao = (:versao + 1),  adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
    @DefineUserInSession
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    void alterarServicoFrenteObraAnalise(@BindBean ServicoFrenteObraAnaliseBD servicoFrenteObraAnalise);

    @SqlUpdate("DELETE FROM siconv.vrpl_servico_frente_obra_analise WHERE id = :id")
    @DefineUserInSession
    void excluirServicoFrenteObraAnalise(@Bind ("id") Long id);

    @SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra_analise WHERE id = :id")
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    Optional<ServicoFrenteObraAnaliseBD> recuperarServicoFrenteObraAnalisePorId(@Bind ("id") Long id);


    @SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra_analise WHERE servico_fk = :servicoFK")
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    List<ServicoFrenteObraAnaliseBD> recuperarTodosServicoFrenteObraAnalisePorChaveServico(@Bind ("servicoFK") Long servicoFK);

    @SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra_analise WHERE servico_fk in (<idsServicos>)")
	@RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
	List<ServicoFrenteObraAnaliseBD> recuperarTodosServicoFrenteObraAnalisePorListaIdsServicos(@BindList("idsServicos") List<Long> idsServicos);

    @SqlBatch("INSERT INTO siconv.vrpl_servico_frente_obra_analise (nm_frente_obra, nr_frente_obra, qt_itens, servico_fk, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :nmFrenteObra,  :nrFrenteObra,  :qtItens,  :servicoFk,  :versaoId,  :versaoNmEvento,  1, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
    @DefineUserInSession
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    void inserirServicoFrenteObraAnaliseBatch (@BindBean Collection<ServicoFrenteObraAnaliseBD> servicoFrenteObraAnalises);

    @SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra_analise")
    @RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
    List<ServicoFrenteObraAnaliseBD> recuperarTodosServicoFrenteObraAnalise();


}
