package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;

public interface MacroServicoParcelaDAO {

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_macro_servico_parcela (macro_servico_fk, nr_parcela, pc_parcela, versao, adt_login, adt_data_hora, adt_operacao) VALUES ( :macroServicoFk, :nrParcela, :pcParcela, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(MacroServicoParcelaBD.class)
	@GetGeneratedKeys
	MacroServicoParcelaBD inserirMacroServicoParcela(@BindBean MacroServicoParcelaBD macroServicoParcela);

	@DefineUserInSession
	@SqlBatch("INSERT INTO siconv.vrpl_macro_servico_parcela (macro_servico_fk, nr_parcela, pc_parcela, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :macroServicoFk,  :nrParcela,  :pcParcela, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(MacroServicoParcelaBD.class)
	@GetGeneratedKeys
	List<MacroServicoParcelaBD> inserirMacroServicoParcelaBatch(
			@BindBean List<MacroServicoParcelaBD> macroServicoParcela);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_macro_servico_parcela SET macro_servico_fk = :macroServicoFk, nr_parcela = :nrParcela, pc_parcela = :pcParcela, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(MacroServicoParcelaBD.class)
	void alterarMacroServicoParcela(@BindBean MacroServicoParcelaBD macroServicoParcela);

	@SqlQuery("SELECT * FROM siconv.vrpl_macro_servico_parcela evento WHERE id = :id")
	@RegisterFieldMapper(MacroServicoParcelaBD.class)
	MacroServicoParcelaBD recuperarMacroServicoParcelaPorId(@Bind("id") Long id);

	@SqlQuery("SELECT id, macro_servico_fk, nr_parcela, pc_parcela, versao FROM siconv.vrpl_macro_servico_parcela WHERE macro_servico_fk in (<idsMacroServicos>) order by macro_servico_fk")
	@RegisterFieldMapper(MacroServicoParcelaBD.class)
	List<MacroServicoParcelaBD> recuperarParcelasDoMacroServicoPorIdsMacroServico(
			@BindList("idsMacroServicos") List<Long> idsMacroServicos);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico_parcela WHERE macro_servico_fk in (select vrpl_macro_servico.id from siconv.vrpl_macro_servico join siconv.vrpl_po on vrpl_macro_servico.po_fk = siconv.vrpl_po.id where vrpl_po.id = :id and vrpl_po.versao = :versao);")
	void excluirParcelasDoMacroServico(@BindBean PoDetalhadaVRPLDTO po);
	
	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico_parcela WHERE macro_servico_fk = :macrosservicoFK;")
	void excluirParcelasPorMacroServicoFK(@Bind("macrosservicoFK") Long macrosservicoFK);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO - Remover métodos usados apenas nos Testes
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SqlQuery("select distinct cff.id 										\n"
			+ "    from 													\n"
			+ "        siconv.vrpl_macro_servico_parcela  cff, 				\n"
			+ "            siconv.vrpl_macro_servico macro,					\n"
			+ "            siconv.vrpl_po po, 								\n"
			+ "            siconv.vrpl_submeta submeta, 					\n"
			+ "            siconv.vrpl_lote_licitacao lote,					\n"
			+ "			   siconv.vrpl_licitacao							\n"
			+ "    where 													\n"
			+ "        cff.macro_servico_fk = macro.id 			and 		\n"
			+ "        macro.po_fk = po.id 						and 		\n"
			+ "        po.submeta_fk = submeta.id 				and			\n"
			+ "        submeta.vrpl_lote_licitacao_fk = lote.id and 		\n"
			+ " 	   lote.licitacao_fk = vrpl_licitacao.id	and			\n"
			+ "        vrpl_licitacao.id = :id 					and			\n"
			+ "        lote.numero_lote in (<numerosLotes>)")
	List<Long> recuperarParcelasDoMacroServicoPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico_parcela WHERE id in (<idsParcelas>)")
	void excluirMacroServicoParcelaPorIdsParcela(@BindList("idsParcelas") List<Long> idsParcelas);
	
	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico_parcela WHERE nr_parcela = :mes and macro_servico_fk in (select vrpl_macro_servico.id from siconv.vrpl_macro_servico join siconv.vrpl_po on vrpl_macro_servico.po_fk = siconv.vrpl_po.id where vrpl_po.id = :id and vrpl_po.versao = :versao);")
	void excluirParcelasDaPoPorMes(@BindBean PoDetalhadaVRPLDTO po, @Bind("mes") int mes);


}
