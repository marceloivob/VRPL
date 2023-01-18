package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

public interface PoDAO {

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(value = PoBD.class)
	PoBD recuperarPoPorId(@Bind("idPo") Long idPo);

	@DefineUserInSession
	@SqlUpdate
	@UseStringTemplateSqlLocator
	void alterarDadosPo(@BindBean PoBD po);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	void alterarReferenciaPo(@BindBean PoBD po);

	@SqlQuery
	@RegisterFieldMapper(PoBD.class)
	@UseStringTemplateSqlLocator
	PoBD recuperarPoVRPL(@Bind("idPo") Long idPo);

	@SqlQuery
	@RegisterBeanMapper(value = PoVRPLDTO.class)
	@UseStringTemplateSqlLocator
	List<PoVRPLDTO> recuperarPosPorLicitacao(@BindBean LicitacaoBD licitacao);

	@SqlQuery
	@RegisterBeanMapper(value = PoBD.class)
	@UseStringTemplateSqlLocator
	List<PoBD> recuperarPosPorLicitacaoELote(@Bind("idLicitacao") Long idLicitacao, @Bind("idLote") Long idLote);

	@SqlBatch
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	@RegisterBeanMapper(value = PoBD.class)
	List<PoBD> insertPos(@BindBean Collection<PoBD> pos);

	@SqlQuery
	@UseStringTemplateSqlLocator
	BigDecimal recuperarValorTotalAnalise(@Bind("submetaId") Long submetaId);

	@SqlQuery
	@UseStringTemplateSqlLocator
	BigDecimal recuperarValorTotalVRPL(@Bind("idPO") Long idPO);

	@SqlQuery
	@UseStringTemplateSqlLocator
	BigDecimal recuperarValorTotalAnalisePorPO(@Bind("idPO") Long idPO);

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(value = SubmetaBD.class)
	SubmetaBD recuperarSubmetaPorPO(@Bind("idPO") Long idPO);

	@SqlQuery("select po.* from siconv.vrpl_po po where po.id = :idPo")
	@RegisterBeanMapper(value = PoBD.class)
	PoBD findById(@Bind("idPo") Long idPo);


	@SqlQuery("select 	po.id po_id,																\n" +
			"			po.id_po_analise po_idPoAnalise,											\n" +
			"			po.submeta_fk po_submetaId,													\n" +
			"			po.dt_previsao_inicio_obra po_dtPrevisaoInicioObra,							\n" +
			"			po.qt_meses_duracao_obra po_qtMesesDuracaoObra,								\n" +
			"			po.in_acompanhamento_eventos po_inAcompanhamentoEventos,					\n" +
			"			po.in_desonerado po_inDesonerado,											\n" +
			"			po.sg_localidade po_sgLocalidade,											\n" +
			"			po.dt_base_analise po_dtBaseAnalise,										\n" +
			"			po.versao_nr po_versaoNr,													\n" +
			"			po.versao_id po_versaoId,													\n" +
			"			po.versao_nm_evento po_versaoNmEvento,										\n" +
			"			po.versao po_versao,														\n" +
			"			po.referencia po_referencia,												\n" +
			"			po.dt_base_vrpl po_dtBaseVrpl,												\n" +
			"			po.dt_previsao_inicio_obra_analise po_dtPrevisaoInicioObraAnalise,			\n" +
			"			mc.id mc_id,																\n" +
			"			mc.tx_descricao	mc_txDescricao,												\n" +
			"	 		mc.po_fk mc_poFk,															\n" +
			"	 		mc.nr_macro_servico mc_nrMacroServico,										\n" +
			"	 		mc.versao_nr mc_versaoNr,													\n" +
			"	 		mc.versao mc_versao,														\n" +
			"			srv.id srv_id,																\n" +
			"	  		srv.macro_servico_fk srv_macroServicoFk,									\n" +
			"	  		srv.vl_preco_total_analise	srv_vlPrecoTotal,								\n" +
			"	  		srv.vl_preco_unitario_licitado	srv_vlPrecoUnitarioLicitado,				\n" +
			"	  		srv.evento_fk srv_eventoFk,													\n" +
			"	  		srv.pc_bdi_analise srv_pcBdi,												\n" +
			"	  		srv.pc_bdi_licitado	srv_pcBdiLicitado,										\n" +
			"	  		srv.vl_preco_unitario_analise srv_vlPrecoUnitario,							\n" +
			"	  		srv.tx_observacao srv_txObservacao,											\n" +
			"	  		srv.in_fonte srv_inFonte,													\n" +
			"	  		srv.vl_custo_unitario_ref_analise srv_vlCustoUnitarioRef,					\n" +
			"	  		srv.vl_custo_unitario_analise srv_vlCustoUnitario,							\n" +
			"	  		srv.vl_custo_unitario_database srv_vlCustoUnitarioDatabase,					\n" +
			"	  		srv.qt_total_itens_analise srv_qtTotalItensAnalise,							\n" +
			"	  		srv.cd_servico srv_cdServico,												\n" +
			"	  		srv.tx_descricao srv_txDescricao,											\n" +
			"	  		srv.sg_unidade srv_sgUnidade,												\n" +
			"	  		srv.id_servico_analise srv_idServicoAnalise,								\n" +
			"	  		srv.nr_servico srv_nrServico,												\n" +
			"	  		srv.versao_nr srv_versaoNr,													\n" +
			"	  		srv.versao srv_versao,														\n" +
			"	  		srv.versao_id srv_versaoId,													\n" +
			"	  		srv.versao_nm_evento srv_versaoNmEvento,									\n" +
			"	        vsfo.frente_obra_fk				vsfo_frente_obra_fk, 												\n" +
			"	        vsfo.servico_fk					vsfo_servico_fk,													\n" +
			"			vsfo.frente_obra_fk::text || '_' || vsfo.servico_fk::text as vsfo_id,       						\n" +
			"	        vsfo.qt_itens					vsfo_qt_itens,														\n" +
			"	        vsfo.versao						vsfo_versao,														\n" +
			"			vsroa.id						vsroa_id,															\n" +
			"			vsroa.servico_fk				vsroa_servico_fk,													\n" +
			"			vsroa.qt_itens					vsroa_qt_itens,														\n" +
			"			vsroa.nr_frente_obra			vsroa_nr_frente_obra,												\n" +
			"			vsroa.nm_frente_obra			vsroa_nm_frente_obra,												\n" +
			"			vsroa.versao					vsroa_versao														\n" +
			" from siconv.vrpl_po 											po 												\n" +
			"			inner join siconv.vrpl_macro_servico 				mc 		on po.id 				= mc.po_fk 		\n" +
			"			inner join siconv.vrpl_servico 						srv  	on srv.macro_servico_fk = mc.id 		\n" +
			"			left  join siconv.vrpl_servico_frente_obra 			vsfo 	on vsfo.servico_fk 		= srv.id 		\n" +
			"			inner join siconv.vrpl_servico_frente_obra_analise 	vsroa 	on vsroa.servico_fk 	= srv.id 		\n" +
			"where po.id IN (<listaPoID>)																					\n" +
			"order by mc.nr_macro_servico,srv.nr_servico																	\n")
	@RegisterFieldMapper(value=PoReducerDTO.class, prefix="po")
	@RegisterFieldMapper(value=MacroServicoReducerDTO.class, prefix="mc")
	@RegisterFieldMapper(value=ServicoReducerDTO.class, prefix="srv")
	@RegisterFieldMapper(value=ServicoFrenteObraDTO.class, prefix="vsfo")
	@RegisterFieldMapper(value=ServicoFrenteObraAnaliseDTO.class, prefix="vsroa")
	@UseRowReducer(PoReducer.class)
	List<PoReducerDTO> findByListId(@BindList("listaPoID") List<Long> listaPoID);

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO - Remover métodos usados apenas nos Testes
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SqlQuery
	@UseStringTemplateSqlLocator
	List<Long> recuperarIdsPOPorNumerosLotes(@BindBean LicitacaoDTO licitacao,
			@BindList("numerosLotes") List<Long> numerosLotes);

	@SqlQuery
	@RegisterFieldMapper(PoBD.class)
	@UseStringTemplateSqlLocator
	Long recuperarIdPoPorSubmeta(@Bind("submetaId") Long submetaId);


}
