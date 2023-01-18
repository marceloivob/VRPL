package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;

public interface ServicoFrenteObraDAO {

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico_frente_obra WHERE frente_obra_fk = (SELECT frente.id FROM siconv.vrpl_frente_obra frente WHERE frente.id = :frenteObraFk AND frente.versao = :versao)")
	void excluirServicoFrenteObraPorIdFrente(@Bind("frenteObraFk") Long frenteObraFk, @Bind("versao") Long versao);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra sfo WHERE servico_fk = :servicoFk")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	List<ServicoFrenteObraBD> recuperarServicoFrenteObraPorIdServico(@Bind("servicoFk") Long servicoFk);
	
	


	@SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra sfo WHERE frente_obra_fk = :frenteObraFk")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	List<ServicoFrenteObraBD> recuperarServicoFrenteObraPorIdFrenteObra(@Bind("frenteObraFk") Long frenteObraFk);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO Remover todos os métodos abaixo. Eles só são referenciados nos Testes de
	// DAO
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@DefineUserInSession
	@SqlUpdate("INSERT INTO siconv.vrpl_servico_frente_obra (frente_obra_fk, qt_itens, servico_fk, versao, adt_login, adt_data_hora, adt_operacao) VALUES (  :frenteObraFk,  :qtItens,  :servicoFk, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT')")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	@GetGeneratedKeys
	ServicoFrenteObraBD inserirServicoFrenteObra(@BindBean ServicoFrenteObraBD servicoFrenteObra);

	@SqlQuery("SELECT * FROM siconv.vrpl_servico_frente_obra evento WHERE frente_obra_fk = :frenteObraFk AND servico_fk = :servicoFk")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	ServicoFrenteObraBD recuperarServicoFrenteObraPorId(@Bind("frenteObraFk") Long frenteObraFk,
			@Bind("servicoFk") Long servicoFk);

	@DefineUserInSession
	@SqlUpdate("UPDATE siconv.vrpl_servico_frente_obra SET frente_obra_fk = :frenteObraFk, qt_itens = :qtItens, servico_fk = :servicoFk, versao = (:versao + 1),  adt_login = current_setting('vrpl.cpf_usuario'), adt_data_hora = LOCALTIMESTAMP, adt_operacao = 'UPDATE' WHERE frente_obra_fk = :frenteObraFk AND servico_fk = :servicoFk")
	@RegisterFieldMapper(ServicoFrenteObraBD.class)
	void alterarServicoFrenteObra(@BindBean ServicoFrenteObraBD servicoFrenteObra);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico_frente_obra WHERE frente_obra_fk = :frenteObraFk AND servico_fk = :servicoFk")
	void excluirServicoFrenteObra(@Bind("frenteObraFk") Long frenteObraFk, @Bind("servicoFk") Long servicoFk);

	@DefineUserInSession
	@SqlUpdate("DELETE FROM siconv.vrpl_servico_frente_obra WHERE servico_fk in (<idsServicos>)")
	void excluirServicosFrenteObraPorIds(@BindList("idsServicos") List<Long> idsServicos);
	
	
	

	@SqlQuery(" SELECT distinct servico_frente_obra.servico_fk 											\n"
			+ " FROM 																					\n"
			+ "    siconv.vrpl_servico_frente_obra servico_frente_obra, 								\n"
			+ "    siconv.vrpl_servico servico, 														\n"
			+ "        siconv.vrpl_macro_servico macro, 												\n"
			+ "        siconv.vrpl_po po, 																\n"
			+ "        siconv.vrpl_submeta submeta, 													\n"
			+ "        siconv.vrpl_lote_licitacao lote,													\n"
			+ "		   siconv.vrpl_licitacao															\n"
			+ " WHERE 																					\n"
			+ "    servico_frente_obra.servico_fk = servico.id 	and										\n"
			+ "    servico.macro_servico_fk = macro.id 			and										\n"
			+ "    macro.po_fk = po.id 							and										\n"
			+ "    po.submeta_fk = submeta.id 					and										\n"
			+ "    submeta.vrpl_lote_licitacao_fk = lote.id 	and										\n"
			+ "    lote.licitacao_fk = vrpl_licitacao.id	  	and										\n"
			+ "    vrpl_licitacao.id = :id 						and										\n"
			+ "    lote.numero_lote in (<numerosLotes>) ")
	List<Long> recuperarServicosFrenteObraPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

}
