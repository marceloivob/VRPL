package br.gov.serpro.vrpl.grpc.submeta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.KeyColumn;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.config.ValueColumn;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import br.gov.serpro.vrpl.grpc.PropostaLote;

public interface SubmetaDAO {
	
	public static final String CONSULTAR_SUBMETAS = 
			"select vrpl_submeta.id as subm_id, " + 
			"	   (vrpl_meta.nr_meta_analise || '.' || vrpl_submeta.nr_submeta_analise) AS subm_numero, " + 
			"       vrpl_submeta.tx_descricao_analise as subm_descricao, " + 
			"       vrpl_submeta.vl_total_licitado as subm_valor_licitado, " + 
			"       vrpl_frente_obra.id as fo_id, " + 
			"       vrpl_frente_obra.nm_frente_obra as fo_descricao, " + 
			"       vrpl_evento.id as ev_id, " + 
			"       vrpl_evento.nm_evento as ev_descricao, " + 
			"       vrpl_servico.id as serv_id, " +
			"       vrpl_servico.nr_servico as serv_numero, " +
			"       vrpl_servico.tx_descricao as serv_descricao, " + 
			"       vrpl_servico.sg_unidade as serv_sg_unidade, " + 
			"       vrpl_servico.vl_preco_unitario_licitado as serv_preco, " + 
			"       vrpl_servico_frente_obra.qt_itens as serv_qtde_itens " + 
			"from vrpl_servico, " + 
			"     vrpl_servico_frente_obra, " + 
			"     vrpl_evento, " + 
			"     vrpl_evento_frente_obra, " + 
			"     vrpl_frente_obra, " + 
			"     vrpl_po, " + 
			"     vrpl_submeta, " + 
			"     vrpl_meta " + 
			"where vrpl_meta.id = vrpl_submeta.meta_fk and " + 
			"	  vrpl_submeta.id = vrpl_po.submeta_fk and " + 
			"      vrpl_po.id = vrpl_frente_obra.po_fk and " + 
			"      vrpl_frente_obra.id = vrpl_evento_frente_obra.frente_obra_fk and " + 
			"      vrpl_evento_frente_obra.evento_fk = vrpl_evento.id and " + 
			"      vrpl_evento.id = vrpl_servico.evento_fk and " + 
			"      vrpl_servico.id = vrpl_servico_frente_obra.servico_fk and " + 
			"      vrpl_frente_obra.id = vrpl_servico_frente_obra.frente_obra_fk and " + 
			"      vrpl_servico_frente_obra.qt_itens > 0 and " + 
			"      vrpl_submeta.id in (<ids>) " +
			"order by subm_numero, fo_id, ev_id, serv_id ";
	
	public static final String CONSULTAR_SERVICOS_SUBMETAS = 
			"			select vrpl_submeta.id as subm_id,  " + 
			"				   (vrpl_meta.nr_meta_analise || '.' || vrpl_submeta.nr_submeta_analise) AS subm_numero,  " + 
			"			       vrpl_submeta.tx_descricao_analise as subm_descricao, " + 
			"			       vrpl_submeta.vl_total_licitado as subm_valor_licitado,  " + 
			"			       vrpl_frente_obra.id as fo_id, " + 
			"			       vrpl_frente_obra.nm_frente_obra as fo_descricao,  " + 
			"	               vrpl_macro_servico.id as macro_id, " +
			"	               vrpl_macro_servico.nr_macro_servico as macro_numero, " + 
			"	               vrpl_macro_servico.tx_descricao as macro_descricao,  " + 
			"			       sum(vrpl_servico.vl_preco_unitario_licitado * vrpl_servico_frente_obra.qt_itens) as serv_valor_total, " + 
			"			       vrpl_servico.id as serv_id, " + 
			"     			   vrpl_servico.nr_servico as serv_numero,  " +
			"     			   vrpl_servico.tx_descricao as serv_descricao,  " + 
			"				   vrpl_servico.sg_unidade as serv_sg_unidade,  " + 
			"				   vrpl_servico.vl_preco_unitario_licitado as serv_preco,  " + 
			"				   vrpl_servico_frente_obra.qt_itens as serv_qtde_itens			       " + 
			"			from vrpl_servico, " + 
			"			     vrpl_macro_servico, " + 
			"			     vrpl_servico_frente_obra,  " + 
			"			     vrpl_frente_obra, " + 
			"			     vrpl_po, " + 
			"			     vrpl_submeta,  " + 
			"			     vrpl_meta " + 
			"			where vrpl_meta.id = vrpl_submeta.meta_fk and  " + 
			"				  vrpl_submeta.id = vrpl_po.submeta_fk and  " + 
			"			      vrpl_po.id = vrpl_frente_obra.po_fk and  " + 
			"              	  vrpl_frente_obra.id = vrpl_servico_frente_obra.frente_obra_fk and " + 
			"	              vrpl_servico_frente_obra.servico_fk = vrpl_servico.id and  " + 
			"	              vrpl_servico.macro_servico_fk = vrpl_macro_servico.id and " + 
			"			      vrpl_servico_frente_obra.qt_itens > 0 and " + 
			"			      vrpl_servico.evento_fk is null and " + 
			"			      vrpl_submeta.id in (<ids>) " + 
			"			group by subm_id, " + 
			"			   subm_numero,   " + 
			"		       subm_descricao,   								" + 
			"		       subm_valor_licitado,   							" + 
			"		       fo_id, 											" + 
			"		       fo_descricao,   									" + 
			"		       macro_id,   										" + 
			"		       macro_descricao, " + 
			"			   serv_id, " + 
			"     		   serv_descricao,  " + 
			"			   serv_sg_unidade,  " + 
			"			   serv_preco,  " + 
			"			   serv_qtde_itens		       " + 
			"			order by subm_numero, fo_id, macro_id, serv_id ";
	
	public static final String CONSULTAR_INDICADOR_ACOMP_EVENTO = 
			"select po.in_acompanhamento_eventos " + 
			"from vrpl_po po " + 
			"where po.submeta_fk = Cast(:id as int8) ";
	
	public static final String CONSULTAR_INDICADOR_ACOMP_EVENTO_LISTA_SUBMETA = 
			"select distinct po.in_acompanhamento_eventos " + 
			"from vrpl_po po " + 
			"where po.submeta_fk in (<ids>) ";

	public static final String CONSULTAR_LOTES_COM_SUBMETAS = 
			" SELECT" +
			"     lote.numero_lote AS numero_lote," +
			"     sub.id as id_submeta," +
			"     meta.nr_meta_analise || '.' || sub.nr_submeta_analise AS numero_submeta," +
			"     sub.tx_descricao_analise AS descricao_submeta," +
			"     sub.in_situacao AS situacao_submeta," +
			"     sub.in_situacao_analise AS situacao_analise_submeta," +
			"     sub.in_regime_execucao," +
			"     sub.in_regime_execucao_analise," +
			"     sub.vl_total_analise," +
			"     sub.vl_total_licitado," +
			"     lote.licitacao_fk AS id_licitacao," +
			"	  prop.ano_convenio AS ano_convenio, " +
			"	  prop.numero_convenio AS numero_convenio, " +
			"	  prop.nome_objeto AS nome_objeto, " +
			"	  prop.uf AS uf, " +
			"	  prop.modalidade AS modalidade, " +
			"	  prop.termo_compromisso_tem_mandatar AS possui_instituicao_mandataria, " +
			"	  prop.nome_proponente AS nome_proponente " +
			" FROM vrpl_submeta sub" +
			"   INNER JOIN vrpl_meta meta ON meta.id = sub.meta_fk" +
			"   INNER JOIN vrpl_lote_licitacao lote ON lote.id = sub.vrpl_lote_licitacao_fk" +
			"   INNER JOIN vrpl_proposta prop ON prop.id = sub.proposta_fk" +
			" WHERE prop.id_siconv = :idPropostaSiconv" + 
			"   AND prop.versao_in_atual "+
			"	AND sub.in_situacao <> 'REJ' " +				
	   		" ORDER BY numero_lote, numero_submeta"; 

	@SqlQuery(CONSULTAR_INDICADOR_ACOMP_EVENTO_LISTA_SUBMETA)
	List<Boolean> getListaIndicadorAcompEvento(@BindList("ids") List<Long> ids);			
	
	@SqlQuery(CONSULTAR_SUBMETAS)
	@RegisterFieldMapper(value = Submeta.class, prefix = "subm")
	@RegisterFieldMapper(value = FrenteObra.class, prefix = "fo")
	@RegisterFieldMapper(value = Evento.class, prefix = "ev")
	@RegisterFieldMapper(value = Servico.class, prefix = "serv")
	@UseRowReducer(SubmetasListReducer.class)
	List<Submeta> getListaSubmetas(@BindList("ids") List<Long> ids);
	
	@SqlQuery(CONSULTAR_SERVICOS_SUBMETAS)
	@RegisterFieldMapper(value = Submeta.class, prefix = "subm")
	@RegisterFieldMapper(value = FrenteObra.class, prefix = "fo")
	@RegisterFieldMapper(value = MacroServico.class, prefix = "macro")
	@RegisterFieldMapper(value = Servico.class, prefix = "serv")
	@UseRowReducer(SubmetasServicoListReducer.class)
	List<Submeta> getServicoListaSubmetas(@BindList("ids") List<Long> ids);	
	
	@SqlQuery(CONSULTAR_INDICADOR_ACOMP_EVENTO)
	Boolean isContratoAcompEvento(@Bind("id") Long id);		
	
	@SqlQuery(CONSULTAR_LOTES_COM_SUBMETAS)
	@UseRowReducer(LoteSubmetaReducer.class)
	PropostaLote getListaLotesComSubmetas(@Bind("idPropostaSiconv") Long idPropostaSiconv);

	@SqlQuery("SELECT id, id_submeta_analise FROM siconv.vrpl_submeta WHERE ID in (<submetasId>)")
	@KeyColumn("id")
	@ValueColumn("id_submeta_analise")
	Map<Long, Long> recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(
			@BindList("submetasId") List<Long> submetasId);
}
