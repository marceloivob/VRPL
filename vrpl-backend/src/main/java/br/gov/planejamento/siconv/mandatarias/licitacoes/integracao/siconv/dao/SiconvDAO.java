package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdbi.v3.core.mapper.MapMapper;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindBeanList;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.BindList.EmptyHandling;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Perfil;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.CtefIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.EventoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.FornecedorIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoParcelaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NaturezaDespesa;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.PoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraDetalheIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SinapiIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubItemInvestimento;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubitemInvestimentoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.UF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.integracao.EventoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.integracao.FrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.ConsultaSinapiDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

public interface SiconvDAO {

	@SqlQuery("SELECT id, sigla from siconv.und_federativa order by sigla")
	@RegisterFieldMapper(UF.class)
	List<UF> findUFS();

	@SqlQuery("SELECT inv.id, inv.descricao, inv.in_tipo_projeto_social, uni.codigo from siconv.sub_it_investimento inv, siconv.und_fornecimento uni where inv.id = :id and inv.und_fornecimento_fk = uni.id")
	@RegisterFieldMapper(SubItemInvestimento.class)
	SubItemInvestimento loadSubItemInvestimento(@Bind("id") Long id);

	@SqlQuery("SELECT id, descricao_sub_item from siconv.natureza_despesa_sub_it where id = :id")
	@RegisterFieldMapper(NaturezaDespesa.class)
	NaturezaDespesa loadNaturezaDespesa(@Bind("id") Long id);

	@SqlQuery("SELECT id, descricao_sub_item from siconv.natureza_despesa_sub_it where id in (<ids>)")
	@RegisterFieldMapper(NaturezaDespesa.class)
	List<NaturezaDespesa> loadNaturezasDeDespesas(@BindList("ids") List<Long> ids);

	@SqlQuery("select * from siconv.prop where id = ?")
	@RegisterRowMapper(MapMapper.class)
	@SingleValue
	Map<String, Object> findById(Long id);

	@SqlQuery
	@RegisterFieldMapper(DadosBasicosIntegracao.class)
	@UseStringTemplateSqlLocator
	DadosBasicosIntegracao recuperarDadosBasicos(@BindBean SiconvPrincipal usuarioLogado);

	@SqlQuery
	@UseStringTemplateSqlLocator
	Boolean existeSPAHomologadoParaProposta(@BindBean SiconvPrincipal usuarioLogado);

	@SqlQuery
	@UseStringTemplateSqlLocator
	List<Long> findNumerosLoteByIdProposta(@Bind("identificadorDaProposta") Long identificadorDaProposta);

	@SqlQuery
	@RegisterFieldMapper(LoteBD.class)
	@UseStringTemplateSqlLocator
	List<String> recuperarLotesDaProposta(@Bind("identificadorDaProposta") Long identificadorDaProposta);

	@SqlQuery("select (sequencial||'/'||ano) as numero, objeto, valor_global::numeric, data_assinatura, data_fim_vigencia from siconv.contrato where licitacao_fk = :idLicitacaoFK")
	@RegisterFieldMapper(CtefIntegracao.class)
	CtefIntegracao findCtefByIdLicitacaoFK(@Bind("idLicitacaoFK") Long idLicitacaoFK);

	@SqlQuery("select licitacao_fk as id_licitacao, (sequencial||'/'||ano) as numero from siconv.contrato where licitacao_fk in (<idsLicitacoesFK>)")
	@RegisterRowMapper(MapMapper.class)
	List<Map<String, Object>> findCtefByIdsLicitacoesFK(@BindList("idsLicitacoesFK") List<Long> idsLicitacoesFK);

	@SqlQuery
	@RegisterBeanMapper(SubmetaIntegracao.class)
	@UseStringTemplateSqlLocator
	List<SubmetaIntegracao> recuperarSubmetasDaPropostaPorLote(
			@Bind("identificadorDaProposta") Long identificadorDaProposta,
			@BindList("numerosLote") List<Long> numerosLote);

	@SqlQuery
	@RegisterBeanMapper(SubmetaIntegracao.class)
	@UseStringTemplateSqlLocator
	List<SubmetaIntegracao> recuperarSubmetasDaProposta(@Bind("identificadorDaProposta") Long identificadorDaProposta);

	@SqlQuery
	@RegisterBeanMapper(MetaIntegracao.class)
	@UseStringTemplateSqlLocator
	List<MetaIntegracao> recuperarMetasProposta(@Bind("identificadorDaProposta") Long identificadorDaProposta);

	@SqlQuery
	@RegisterBeanMapper(PoIntegracao.class)
	@UseStringTemplateSqlLocator
	List<PoIntegracao> recuperarPosPorIdSubmeta(
			@BindList(value = "idsSubmeta", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsSubmeta);

	@SqlQuery
	@RegisterBeanMapper(PoIntegracao.class)
	@UseStringTemplateSqlLocator
	List<PoIntegracao> recuperarPosPropostaPorLote(@Bind("identificadorDaProposta") Long identificadorDaProposta,
			@BindList("numerosLote") List<Long> numerosLote);

	@SqlQuery("SELECT evento.*                              \n" + "             FROM                              \n"
			+ "                    siconv.acffo_evento evento, \n" + "                    siconv.acffo_po po, \n"
			+ "                    siconv.acffo_submeta submeta, \n" + "                    siconv.acffo_meta meta, \n"
			+ "                    siconv.acffo_qci qci, \n" + "                    siconv.acffo acffo\n"
			+ "                                        \n" + "             WHERE                       \n"
			+ "                 evento.po_fk in (<idsPo>) AND\n" + "                 evento.po_fk = po.id AND\n"
			+ "                 po.submeta_fk = submeta.id AND \n" + "                 submeta.meta_fk = meta.id AND \n"
			+ "                 meta.qci_fk = qci.id AND\n" + "                 qci.acffo_fk = acffo.id AND \n"
			+ "                 acffo.prop_fk = :idProposta  AND \n"
			+ "                 acffo.versao_nr = (select max(versao_nr) from siconv.acffo where id = acffo.id and acffo.prop_fk = :idProposta) ")
	@RegisterFieldMapper(EventoIntegracao.class)
	List<EventoIntegracao> recuperarListaEventosAnalisePorIdPoIdProposta(
			@BindList(value = "idsPo", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsPo,
			@Bind("idProposta") Long idProposta);

	@SqlQuery("SELECT frente.*                              \n" + "             FROM                              \n"
			+ "                    siconv.acffo_frente_obra frente, \n" + "                    siconv.acffo_po po, \n"
			+ "                    siconv.acffo_submeta submeta, \n" + "                    siconv.acffo_meta meta, \n"
			+ "                    siconv.acffo_qci qci, \n" + "                    siconv.acffo acffo\n"
			+ "                                        \n" + "             WHERE                       \n"
			+ "                 frente.po_fk in (<idsPo>) AND\n" + "                 frente.po_fk = po.id AND\n"
			+ "                 po.submeta_fk = submeta.id AND \n" + "                 submeta.meta_fk = meta.id AND \n"
			+ "                 meta.qci_fk = qci.id AND\n" + "                 qci.acffo_fk = acffo.id AND \n"
			+ "                 acffo.prop_fk = :idProposta  AND \n"
			+ "                 acffo.versao_nr = (select max(versao_nr) from siconv.acffo where id = acffo.id and acffo.prop_fk = :idProposta) ")
	@RegisterFieldMapper(FrenteObraIntegracao.class)
	List<FrenteObraIntegracao> recuperarListaFrentesObraAnalisePorIdPoIdProposta(
			@BindList(value = "idsPo", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsPo,
			@Bind("idProposta") Long idProposta);

	@SqlQuery
	@RegisterBeanMapper(value = MacroServicoIntegracao.class, prefix = "servico")
	@RegisterBeanMapper(value = MacroServicoParcelaIntegracao.class, prefix = "parcela")
	@UseRowReducer(MacroServicoReducer.class)
	@UseStringTemplateSqlLocator
	List<MacroServicoIntegracao> recuperarMacroServicosPorIdsPos(
			@BindList(value = "idsPo", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsPo);

	@SqlQuery
	@RegisterBeanMapper(ServicoIntegracao.class)
	@UseStringTemplateSqlLocator
	List<ServicoIntegracao> recuperarServicosPorIdsMacroServico(
			@BindList(value = "idsMacroServico", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsMacroServico);

	@SqlQuery
	@RegisterBeanMapper(EventoFrenteObraIntegracao.class)
	@UseStringTemplateSqlLocator
	List<EventoFrenteObraIntegracao> recuperarEventoFrenteObraPorIdEvento(
			@BindList(value = "idsEvento", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsEvento);

	@SqlQuery
	@RegisterBeanMapper(ServicoFrenteObraIntegracao.class)
	@UseStringTemplateSqlLocator
	List<ServicoFrenteObraIntegracao> recuperarServicoFrenteObraPorIdServico(
			@BindList(value = "idsServico", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsServico);
	
	@SqlQuery
	@RegisterBeanMapper(ServicoFrenteObraDetalheIntegracao.class)
	@UseStringTemplateSqlLocator
	List<ServicoFrenteObraDetalheIntegracao>recuperarServicoFrenteObraDetalhePorIdServico(
			@BindList(value = "idsServico", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsServico);

	@SqlQuery
	@RegisterBeanMapper(SubitemInvestimentoIntegracao.class)
	@UseStringTemplateSqlLocator
	List<SubitemInvestimentoIntegracao> recuperarSubitemInvestimentoPorMetas(
			@BindList(value = "idsMetas", onEmpty = EmptyHandling.NULL_STRING) List<Long> idsMetas);

	@SqlQuery
	@RegisterBeanMapper(FornecedorIntegracao.class)
	@UseStringTemplateSqlLocator
	List<FornecedorIntegracao> recuperarFornecedoresProposta(@Bind("idProposta") Long idProposta);

	@Deprecated
	@SqlQuery
	@RegisterBeanMapper(SinapiIntegracao.class)
	@UseStringTemplateSqlLocator
	SinapiIntegracao recuperarSinapiPorCodigoItemData(@Bind("cdItem") String cdItem, @Bind("data") LocalDate data,
			@Bind("local") String local);

	@SqlQuery
	@RegisterBeanMapper(SinapiIntegracao.class)
	@RegisterFieldMapper(value = ConsultaSinapiDTO.class)
	@RegisterFieldMapper(value = SinapiIntegracao.class)
	@UseStringTemplateSqlLocator
	Map<ConsultaSinapiDTO,SinapiIntegracao> recuperarListaSinapiPorCodigoItemData(@BindBeanList(value = "lista", propertyNames = {"cdItem","sgLocalidade","dataFormatada"}) Set<ConsultaSinapiDTO> listaConsultaSinapiDTO);
	
	@SqlQuery
	@UseStringTemplateSqlLocator
	List<String> recuperarEmailsPerfilProponente(@Bind("propostaFk") Long propostaFk,
			@BindList("perfis") Collection<Perfil> perfis);

	@SqlQuery
	@UseStringTemplateSqlLocator
	List<String> recuperarEmailsPerfilConcedente(@Bind("propostaFk") Long propostaFk,
			@BindList("perfis") Collection<Perfil> perfis);

	@SqlQuery
	@UseStringTemplateSqlLocator
	List<String> recuperarEmailsPerfilMandataria(@Bind("propostaFk") Long propostaFk,
			@BindList("perfis") Collection<Perfil> perfis);

	@SqlQuery("select exists (select 1 from siconv.acffo_sinapi s where s.dt_referencia = :dataBase)")
	boolean existeCargaSinapiParaDataBase(@Bind("dataBase") LocalDate dataBase);

	@SqlQuery("select p.id as id, p.modalidade as modalidade, "
			+ "p.valor_global as valorGlobal, p.valor_repasse as valorRepasse, p.valor_contra_partida as valorContrapartida, "
			+ "c.id as idConvenio, c.sequencial as numeroConvenio, c.data_assinatura as dataAssinaturaConvenio, "
			+ "c.ano as anoConvenio, oa.nome as nomeMandataria from	siconv.prop p \n"
			+ "	inner join siconv.convenio c on p.convenio_fk = c.id \n"
			+ "	left join siconv.org_administrativo oa on p.instituicao_mandataria_fk = oa.id \n"
			+ "where p.id = :idProposta;")
	@RegisterBeanMapper(PropostaBD.class)
	PropostaBD recuperarDadosProposta(@BindBean SiconvPrincipal usuarioLogado);

	
	@SqlQuery("select p.ver_at_fk as id, p.modalidade as modalidade, "
			+ "p.valor_global as valorGlobal, p.valor_repasse as valorRepasse, p.valor_contra_partida as valorContrapartida, "
			+ "c.id as idConvenio, c.sequencial as numeroConvenio, c.data_assinatura as dataAssinaturaConvenio, "
			+ "c.ano as anoConvenio, oa.nome as nomeMandataria from	siconv.prop p \n"
			+ "	inner join siconv.convenio c on p.convenio_fk = c.id \n"
			+ "	left join siconv.org_administrativo oa on p.instituicao_mandataria_fk = oa.id \n"
			+ "where p.ver_at_fk = :idProposta \n"
			+ "and p.historico = true \n"  
			+ "and p.situacao_proposta = 'HISTORICO';")
	@RegisterBeanMapper(PropostaBD.class)
	PropostaBD recuperarDadosPropostaComTermoAditivoPendente(@BindBean SiconvPrincipal usuarioLogado);

}
