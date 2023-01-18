package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao;

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
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.PrecoTotalMacroServicoBD;

public interface MacroServicoDAO {

	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_macro_servico (id_macro_servico_analise, nr_macro_servico, po_fk, tx_descricao, versao, adt_login, adt_data_hora, adt_operacao) VALUES (:idMacroServicoAnalise,  :nrMacroServico,  :poFk,  :txDescricao, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(MacroServicoBD.class)
	@GetGeneratedKeys
	MacroServicoBD inserirMacroServico(@BindBean MacroServicoBD macroServico);

	@SqlBatch("INSERT INTO siconv.vrpl_macro_servico (id_macro_servico_analise, nr_macro_servico, po_fk, tx_descricao, versao, adt_login, adt_data_hora, adt_operacao) VALUES (:idMacroServicoAnalise, :nrMacroServico,  :poFk,  :txDescricao, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@GetGeneratedKeys
	@DefineUserInSession
	@RegisterFieldMapper(MacroServicoBD.class)
	List<MacroServicoBD> inserirMacrosServicos(@BindBean Collection<MacroServicoBD> lote);

	@SqlQuery("SELECT * FROM siconv.vrpl_macro_servico WHERE po_fk = :idPO order by nr_macro_servico")
	@RegisterFieldMapper(MacroServicoBD.class)
	List<MacroServicoBD> recuperarMacroServicosPorIdPo(@Bind("idPO") Long idPO);

	@SqlQuery("SELECT * FROM siconv.vrpl_macro_servico WHERE po_fk IN (<listaIdPo>)")
	@RegisterFieldMapper(MacroServicoBD.class)
	List<MacroServicoBD> recuperarMacroServicosPorListaIdPo(@BindList("listaIdPo") List<Long> listaIdPo);

	
	/*@SqlQuery("select u.id u_id, u.name u_name, p.id p_id, p.phone p_phone "
		    + "from user u left join phone p on u.id = p.user_id")
		@RegisterConstructorMapper(value = User.class, prefix = "u")
		@RegisterConstructorMapper(value = Phone.class, prefix = "p")*/
	
	
	
	@SqlQuery(" select macro.id, sum(servico.vl_preco_unitario_licitado * servico.qt_total_itens_analise) preco_total "
			+ " from siconv.vrpl_macro_servico macro, " + "      siconv.vrpl_servico servico "
			+ " where servico.macro_servico_fk = macro.id " + "    and macro.id in (<idsMacroservico>) "
			+ " group by macro.id ")
	@RegisterFieldMapper(PrecoTotalMacroServicoBD.class)
	List<PrecoTotalMacroServicoBD> recuperarPrecoTotalMacroServicos(
			@BindList("idsMacroservico") List<Long> idsMacroservico);
	
	@SqlQuery(" select macro.id, sum(round(servico.vl_preco_unitario_licitado * sfo.qt_itens, 2)) preco_total "
			+ " from siconv.vrpl_macro_servico macro, " + "      siconv.vrpl_servico servico, " + "		siconv.vrpl_servico_frente_obra sfo "
			+ " where sfo.servico_fk = servico.id " + "		and servico.macro_servico_fk = macro.id " + "    and macro.id in (<idsMacroservico>) "
			+ " group by macro.id ")
	@RegisterFieldMapper(PrecoTotalMacroServicoBD.class)
	List<PrecoTotalMacroServicoBD> recuperarPrecoTotalMacroServicosPorFrenteObra(
			@BindList("idsMacroservico") List<Long> idsMacroservico);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO - Remover métodos usados apenas nos Testes
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_macro_servico SET id = :id, nr_macro_servico = :nrMacroServico, po_fk = :poFk, tx_descricao = :txDescricao, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE id = :id")
	@RegisterFieldMapper(MacroServicoBD.class)
	void alterarMacroServico(@BindBean MacroServicoBD macroServico);

	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico WHERE id = :id")
	void excluirMacroServico(@Bind("id") Long id);

	@SqlQuery("SELECT * FROM siconv.vrpl_macro_servico evento WHERE id = :id")
	@RegisterFieldMapper(MacroServicoBD.class)
	MacroServicoBD recuperarMacroServicoPorId(@Bind("id") Long id);

	@SqlQuery("  SELECT distinct macro.id 												\n"
			+ "    from 																\n"
			+ "      siconv.vrpl_macro_servico macro, 									\n"
			+ "      siconv.vrpl_po po, 												\n"
			+ "      siconv.vrpl_submeta submeta, 										\n"
			+ "      siconv.vrpl_lote_licitacao lote, 									\n"
			+ "      siconv.vrpl_licitacao 												\n"
			+ "    where    															\n"
			+ "      macro.po_fk = po.id 								and 			\n"
			+ "      po.submeta_fk = submeta.id							and 			\n"
			+ "      submeta.vrpl_lote_licitacao_fk = lote.id 			and 			\n"
			+ "      lote.licitacao_fk = vrpl_licitacao.id 				and 			\n"
			+ "      vrpl_licitacao.id = :id 							and 			\n"
			+ "      lote.numero_lote in (<numerosLotes>)")
	List<Long> recuperarMacroServicosPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@SqlUpdate("DELETE FROM siconv.vrpl_macro_servico WHERE id in (<idsMacroServicos>)")
	void excluirMacroServicosPorIds(@BindList("idsMacroServicos") List<Long> idsMacroServicos);

}
