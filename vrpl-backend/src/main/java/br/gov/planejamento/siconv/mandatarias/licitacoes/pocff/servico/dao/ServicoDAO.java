package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;

public interface ServicoDAO {

	@DefineUserInSession
	@SqlBatch("INSERT INTO siconv.vrpl_servico (id_servico_analise, cd_servico, evento_fk, in_fonte, macro_servico_fk, nr_servico, pc_bdi_analise, pc_bdi_licitado, qt_total_itens_analise, sg_unidade, tx_descricao, tx_observacao, vl_custo_unitario_analise, vl_custo_unitario_ref_analise, vl_preco_total_analise, vl_preco_unitario_analise, vl_preco_unitario_licitado, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :idServicoAnalise, :cdServico, :eventoFk, :inFonte, :macroServicoFk, :nrServico, :pcBdi, :pcBdiLicitado, :qtTotalItensAnalise, :sgUnidade, :txDescricao, :txObservacao, :vlCustoUnitario, :vlCustoUnitarioRef, :vlPrecoTotal, :vlPrecoUnitario, :vlPrecoUnitarioLicitado, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(ServicoBD.class)
	@GetGeneratedKeys
	List<ServicoBD> inserirServicos(@BindBean Collection<ServicoBD> servicos);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_servico SET evento_fk = :eventoFk, pc_bdi_licitado = :pcBdiLicitado, vl_preco_unitario_licitado = :vlPrecoUnitarioLicitado, vl_custo_unitario_database = :vlCustoUnitario, tx_observacao = :txObservacao, versao = (:versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(ServicoBD.class)
	@GetGeneratedKeys
	ServicoBD alterarServico(@BindBean ServicoBD servico);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_servico SET evento_fk = null, versao = (:versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(ServicoBD.class)
	@GetGeneratedKeys
	ServicoBD removeAssociacaoComEvento(@BindBean ServicoBD servico);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico WHERE id = :id and versao = :versao")
	void excluirServico(@BindBean ServicoBD servico);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico evento WHERE id = :id")
	@RegisterFieldMapper(ServicoBD.class)
	ServicoBD recuperarServicoPorId(@Bind("id") Long idServico);

	@DefineUserInSession
	@SqlBatch("INSERT INTO siconv.vrpl_servico_frente_obra (servico_fk, frente_obra_fk, qt_itens, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :servicoFk, :frenteObraFk, :qtItens, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	void insertServicoFrenteObra(@BindBean Collection<ServicoFrenteObraBD> servicoFrenteObra);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra WHERE servico_fk = :idServico")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	List<ServicoFrenteObraBD> recuperaListaServicoFrenteObraPorIdServico(@Bind("idServico") Long idServico);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra WHERE servico_fk in (<idsServicos>)")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	List<ServicoFrenteObraBD> recuperaListaServicoFrenteObraPorListaIdsServicos(@BindList("idsServicos") List<Long> idsServicos);

	@DefineUserInSession
	@SqlBatch("UPDATE siconv.vrpl_servico_frente_obra SET qt_itens=:qtItens, versao = (:versao + 1), adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora= LOCALTIMESTAMP, adt_operacao='UPDATE' WHERE servico_fk=:servicoFk AND frente_obra_fk=:frenteObraFk")
	void alterarServicoFrenteObra(@BindBean Collection<ServicoFrenteObraBD> servicoFrenteObra);

	@DefineUserInSession
	@SqlBatch("DELETE FROM siconv.vrpl_servico_frente_obra WHERE servico_fk=:servicoFk AND frente_obra_fk=:frenteObraFk")
	void removerServicoFrenteObra(@BindBean Collection<ServicoFrenteObraBD> servicoFrenteObra);

	@DefineUserInSession
	@SqlUpdate(" DELETE FROM siconv.vrpl_evento_frente_obra efo 						\n"
			+ " 	USING siconv.vrpl_frente_obra fo 									\n"
			+ "  WHERE efo.frente_obra_fk = fo.id 										\n"
			+ " 	AND fo.po_fk = :poId 												\n"
			+ " 	AND efo.frente_obra_fk NOT IN ( 									\n"
			+ " 		SELECT sfo.frente_obra_fk 										\n"
			+ " 		FROM siconv.vrpl_servico_frente_obra sfo 						\n"
			+ " 		WHERE sfo.frente_obra_fk = efo.frente_obra_fk 					\n"
			+ " ) ")
	void removerEventosFrentesObraSemServicoFrenteObra(@Bind("poId") Long poId);

	@DefineUserInSession
	@SqlBatch(" DELETE FROM siconv.vrpl_evento_frente_obra efo                          \n"
			+ "    USING siconv.vrpl_servico s                                          \n"
			+ " WHERE s.evento_fk = efo.evento_fk                                       \n"
			+ "    AND s.id = :servicoFk                                                \n"
			+ "    AND efo.frente_obra_fk = :frenteObraFk ")
	void removerEventosFrentesObraDosServicos(@BindBean Collection<ServicoFrenteObraBD> servicoFrenteObra);

	@DefineUserInSession
	@SqlBatch(" DELETE FROM siconv.vrpl_evento_frente_obra efo                          \n"
			+ " WHERE efo.evento_fk = :eventoId   	                                    \n"
			+ "    AND efo.frente_obra_fk = :frenteObraFk ")
	void removerEventosFrentesObraDosServicosPorEvento(@Bind("eventoId") Long eventoId,
			@BindBean Collection<ServicoFrenteObraBD> servicoFrenteObra);

	@SqlQuery(" SELECT COUNT(*)                                                         \n"
			+ " FROM siconv.vrpl_servico_frente_obra sfo                                \n"
			+ "   INNER JOIN siconv.vrpl_servico s ON s.id = sfo.servico_fk             \n"
			+ " WHERE s.evento_fk = :eventoId                                           \n"
			+ "   AND sfo.frente_obra_fk = :frenteObraId ")
	int qntsServicosFrenteObraMesmoEvento(@Bind("eventoId") Long eventoId, @Bind("frenteObraId") Long frenteObraId);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico_frente_obra WHERE servico_fk = :servicoFk")
	void excluirServicoFrenteObraPorServico(@Bind("servicoFk") Long servicoFk);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico WHERE macro_servico_fk = :macroServicoId order by nr_servico")
	@RegisterFieldMapper(ServicoBD.class)
	List<ServicoBD> recuperarServicoPorMacroServico(@Bind("macroServicoId") Long macroServicoId);

	@SqlQuery(" SELECT distinct servico.id 												\n"
			+ "   from  																\n"
			+ "      siconv.vrpl_servico servico, 										\n"
			+ "      siconv.vrpl_macro_servico macro, 									\n"
			+ "      siconv.vrpl_po po,  												\n"
			+ "      siconv.vrpl_submeta submeta,  										\n"
			+ "      siconv.vrpl_lote_licitacao lote,  									\n"
			+ "      siconv.vrpl_licitacao 												\n"
			+ "   where  																\n"
			+ "    servico.macro_servico_fk = macro.id 					and 			\n"
			+ "    macro.po_fk = po.id 									and     		\n"
			+ "    po.submeta_fk = submeta.id 							and     		\n"
			+ "    submeta.vrpl_lote_licitacao_fk = lote.id 			and     		\n"
			+ "    lote.licitacao_fk = vrpl_licitacao.id				and 			\n"
			+ "    vrpl_licitacao.id = :id 								and 			\n"
			+ "    lote.numero_lote in (<numerosLotes>) ")
	List<Long> recuperarServicosPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico WHERE id in (<idsServicos>)")
	void excluirServicosPorIds(@BindList("idsServicos") List<Long> idsServicos);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico WHERE macro_servico_fk IN (SELECT id FROM siconv.vrpl_macro_servico WHERE po_fk = :po.id) order by nr_servico")
	@RegisterFieldMapper(ServicoBD.class)
	List<ServicoBD> recuperarServicosPorPo(@BindBean("po") PoVRPLDTO po);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico WHERE evento_fk = :eventoId")
	@RegisterFieldMapper(ServicoBD.class)
	List<ServicoBD> recuperarServicosPorEvento(@Bind("eventoId") Long eventoId);

	@SqlQuery(" SELECT po.in_acompanhamento_eventos \n" +
			" FROM siconv.vrpl_servico ser,\n" +
			"      siconv.vrpl_macro_servico macro,\n" +
			"      siconv.vrpl_po po\n" +
			" WHERE po.id = macro.po_fk and\n" +
			"       ser.macro_servico_fk = macro.id \n" +
			"       and ser.id = :servicoId")
	boolean servicoEhPorEvento(@Bind("servicoId") Long servicoId);

	@SqlQuery(" SELECT po.id \n" +
			" FROM siconv.vrpl_servico ser,\n" +
			"      siconv.vrpl_macro_servico macro,\n" +
			"      siconv.vrpl_po po\n" +
			" WHERE po.id = macro.po_fk and\n" +
			"       ser.macro_servico_fk = macro.id \n" +
			"       and ser.id = :servicoId")
	public Long recuperarIdPoPorServicoId(@Bind("servicoId") Long servicoId);




}
