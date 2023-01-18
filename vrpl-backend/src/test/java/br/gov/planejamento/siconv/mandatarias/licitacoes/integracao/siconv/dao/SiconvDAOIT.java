package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.CtefIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.EventoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NaturezaDespesa;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.PoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubItemInvestimento;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubitemInvestimentoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.UF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.integracao.EventoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.integracao.FrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SiconvDAOIT {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String cpfUsuarioLogado = "00000000000";

	private SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, 1L, cpfUsuarioLogado);

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());
		daoFactory.getJdbi().useHandle(handle -> {
            handle.execute(getUfInsert());
            handle.execute(getContratoInsert());
            handle.execute(getPropostaInsert());
            handle.execute(getAcffoLotes());
            handle.execute(getNaturezaDespesa());
            handle.execute(getSubitemInvestimento());
            handle.execute(getAcffo());
            handle.execute(getEventoFrenteObra());
            handle.execute(getAcffo());
            handle.execute(getMacroServico());
            handle.execute(getServicoFrenteObra());
            handle.execute(getServico());
            handle.execute(getSinapiInsert());
            handle.execute(DataFactory.getConvenioInsert());
            handle.execute(DataFactory.getOrgAdministrativoInsert());
        });
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	@Test
	@Order(1)
	public void findUFS() {
		List<UF> ufs = daoFactory.get(SiconvDAO.class).findUFS();

		assertNotNull(ufs);
		assertTrue(ufs.size() > 0);
	}

	@Test
	@Order(2)
	public void findCtefByIdLicitacaoFK() {
		CtefIntegracao ctef = daoFactory.get(SiconvDAO.class).findCtefByIdLicitacaoFK(1L);

		assertNotNull(ctef);
	}

	@Test
	@Order(3)
	public void findCtefByIdsLicitacoesFK() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);

		List<Map<String, Object>> res = daoFactory.get(SiconvDAO.class).findCtefByIdsLicitacoesFK( ids );

		assertNotNull(res);
		assertTrue(res.size() > 0);
	}

	@Test
	@Order(4)
	public void findById() {
		Map<String, Object> res = daoFactory.get(SiconvDAO.class).findById(1L);

		assertNotNull(res);
		assertTrue(res.size() > 0);
	}

	@Test
	@Order(5)
	public void findNumerosLoteByIdProposta() {
		List<Long> lotes = daoFactory.get(SiconvDAO.class).findNumerosLoteByIdProposta(1L);

		assertNotNull(lotes);
		assertTrue(lotes.size() > 0);
	}

	@Test
	@Order(6)
	public void loadNaturezaDespesa() {
		NaturezaDespesa nat = daoFactory.get(SiconvDAO.class).loadNaturezaDespesa(1L);

		assertNotNull(nat);
	}

	@Test
	@Order(7)
	public void loadNaturezasDeDespesas() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		List<NaturezaDespesa> nats = daoFactory.get(SiconvDAO.class).loadNaturezasDeDespesas(ids);

		assertNotNull(nats);
		assertTrue(nats.size() > 0);
	}

	@Test
	@Order(8)
	public void loadSubItemInvestimento() {
		SubItemInvestimento sub = daoFactory.get(SiconvDAO.class).loadSubItemInvestimento(1L);

		assertNotNull(sub);
	}

	@Test
	@Order(9)
	public void recuperarDadosBasicos() {
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, 1L);

		DadosBasicosIntegracao dad = daoFactory.get(SiconvDAO.class).recuperarDadosBasicos(usuarioLogado);

		assertNotNull(dad);
	}

	@Test
	@Order(10)
	public void recuperarEventoFrenteObraPorIdEvento() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		List<EventoFrenteObraIntegracao> efo = daoFactory.get(SiconvDAO.class).recuperarEventoFrenteObraPorIdEvento(ids);

		assertNotNull(efo);
		assertTrue(efo.size() > 0);
	}

	@Test
	@Order(11)
	public void recuperarListaEventosAnalisePorIdPoIdProposta() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<EventoIntegracao> eves = daoFactory.get(SiconvDAO.class).recuperarListaEventosAnalisePorIdPoIdProposta(ids, 1L);

		assertNotNull(eves);
		assertTrue(eves.size() > 0);
	}

	@Test
	@Order(12)
	public void recuperarListaFrentesObraAnalisePorIdPoIdProposta() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<FrenteObraIntegracao> foi = daoFactory.get(SiconvDAO.class).recuperarListaFrentesObraAnalisePorIdPoIdProposta(ids, 1L);

		assertNotNull(foi);
		assertTrue(foi.size() > 0);
	}

	@Test
	@Order(13)
	public void recuperarLotesDaProposta() {
		List<String> lotes = daoFactory.get(SiconvDAO.class).recuperarLotesDaProposta(1L);

		assertNotNull(lotes);
		assertTrue(lotes.size() > 0);
	}

	@Test
	@Order(14)
	public void recuperarMacroServicosPorIdsPos() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<MacroServicoIntegracao> msi = daoFactory.get(SiconvDAO.class).recuperarMacroServicosPorIdsPos(ids);

		assertNotNull(msi);
		assertTrue(msi.size() > 0);
	}

	@Test
	@Order(15)
	public void recuperarMetasProposta() {
		List<MetaIntegracao> mets = daoFactory.get(SiconvDAO.class).recuperarMetasProposta(1L);

		assertNotNull(mets);
		assertTrue(mets.size() > 0);
	}

	@Test
	@Order(17)
	public void recuperarPosPorIdSubmeta() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<PoIntegracao> pos = daoFactory.get(SiconvDAO.class).recuperarPosPorIdSubmeta(ids);

		assertNotNull(pos);
		assertTrue(pos.size() > 0);
	}

	@Test
	@Order(18)
	public void recuperarPosPropostaPorLote() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<PoIntegracao> pos = daoFactory.get(SiconvDAO.class).recuperarPosPropostaPorLote(1L, ids);

		assertNotNull(pos);
		assertTrue(pos.size() > 0);
	}

	@Test
	@Order(19)
	public void recuperarServicoFrenteObraPorIdServico() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<ServicoFrenteObraIntegracao> sfoi = daoFactory.get(SiconvDAO.class).recuperarServicoFrenteObraPorIdServico(ids);

		assertNotNull(sfoi);
		assertTrue(sfoi.size() > 0);
	}

	@Test
	@Order(20)
	public void recuperarServicosPorIdsMacroServico() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<ServicoIntegracao> ser = daoFactory.get(SiconvDAO.class).recuperarServicosPorIdsMacroServico(ids);

		assertNotNull(ser);
		assertTrue(ser.size() > 0);
	}

	@Test
	@Order(21)
	public void recuperarSubitemInvestimentoPorMetas() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<SubitemInvestimentoIntegracao> sii = daoFactory.get(SiconvDAO.class).recuperarSubitemInvestimentoPorMetas(ids);

		assertNotNull(sii);
		assertTrue(sii.size() > 0);
	}

	@Test
	@Order(22)
	public void recuperarSubmetasDaPropostaPorLote() {
		List<Long> ids = new ArrayList<>();
		ids.add(1L);

		List<SubmetaIntegracao> subs = daoFactory.get(SiconvDAO.class).recuperarSubmetasDaPropostaPorLote(1L, ids);

		assertNotNull(subs);
		assertTrue(subs.size() > 0);
	}

	@Test
	@Order(23)
	public void confirmaExistenciaDeSPAHomologadoParaProposta() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(cpfUsuarioLogado);

			Boolean confirma = daoFactory.get(SiconvDAO.class).existeSPAHomologadoParaProposta(usuarioLogado);

			assertNotNull(confirma);
			assertTrue(confirma);
		});
	}
	
	@Test
	@Order(24)
	public void existeCargaSinapiParaDataBase() {
		Boolean existe = daoFactory.get(SiconvDAO.class).existeCargaSinapiParaDataBase(LocalDate.of(2017, 6, 1));
		Boolean naoExiste = daoFactory.get(SiconvDAO.class).existeCargaSinapiParaDataBase(LocalDate.now());

		assertNotNull(existe);
		assertTrue(existe);
		
		assertNotNull(naoExiste);
		assertFalse(naoExiste);
	}
	
	@Test
	@Order(25)
	public void recuperarDadosProposta() {
		PropostaBD propostaBD = daoFactory.get(SiconvDAO.class).recuperarDadosProposta(usuarioLogado);
		assertEquals(Integer.valueOf(2020), propostaBD.getAnoConvenio());
		assertEquals(Integer.valueOf(883675), propostaBD.getNumeroConvenio());		
		assertEquals(LocalDate.of(2020, 1, 9), propostaBD.getDataAssinaturaConvenio());
		assertEquals("CAIXA ECONOMICA FEDERAL", propostaBD.getNomeMandataria());
	}
	
	private String getServico() {
		return "INSERT INTO siconv.acffo_servico " +
				"(id, tx_observacao, macro_servico_fk, nr_servico, cd_servico, tx_descricao, sg_unidade, vl_custo_unitario_ref, pc_bdi, qt_total_itens, vl_custo_unitario, vl_preco_unitario, vl_preco_total, evento_fk, nr_versao_hibernate, in_fonte, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, '', '', '', 0, 0, 0, 0, 0, 0, 1, 0, '', 0, '', ''); ";
	}

	private String getServicoFrenteObra() {
		return "INSERT INTO siconv.acffo_servico_frente_obra " +
				"(servico_fk, frente_obra_fk, qt_itens, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 1, 0, 0, '', ''); ";
	}

	private String getMacroServico() {
		return "INSERT INTO siconv.acffo_macro_servico " +
				"(id, tx_descricao, po_fk, nr_macro_servico, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 0, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_macro_servico_parcela " +
				"(id, nr_parcela, pc_parcela, macro_servico_fk, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 0, 1, 0, 0, '', ''); ";
	}

	private String getEventoFrenteObra() {
		return "INSERT INTO siconv.acffo_evento_frente_obra " +
				"(evento_fk, frente_obra_fk, nr_mes_conclusao, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 1, 0, 0, '', ''); ";
	}

	private String getAcffo() {
		return "INSERT INTO siconv.acffo\n" +
				"(id, tx_apelido, prop_fk, nr_versao_hibernate, in_situacao, versao_nr, versao_id, versao_nm_evento)\n" +
				"VALUES(1, 'ape', 1, 0, 'HOM', 0, '', null); " +

				"INSERT INTO siconv.acffo_proposta " +
				"(id, prop_siconv_id, numero_proposta, ano_proposta, valor_global, valor_repasse, valor_contra_partida, modalidade, nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa, nome_programa, identificacao_proponente, nome_proponente, uf, pc_min_contra_partida, nome_mandataria, categoria, nr_versao_hibernate, nivel_contrato, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 1, 2018, 0, 0, 0, 0, '', 0, 0, now(), '', '', '', '', '', 0, '', '', 0, '', 0, '', ''); " +

				"INSERT INTO siconv.acffo_qci " +
				"(id, acffo_fk, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_meta " +
				"(id, tx_descricao, qt_itens, subitem_investimento_fk, qci_fk, nr_versao_hibernate, nr_meta, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 1, 0, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_submeta " +
				"(id, tx_descricao, nr_lote, vl_repasse, vl_contrapartida, vl_outros, meta_fk, vl_total, nr_versao_hibernate, nr_submeta, in_situacao, in_regime_execucao, natureza_despesa_sub_it_fk, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 0, 0, 0, 1, 0, 0, 1, '', 'EPG', 0, 1, '', ''); " +

				"INSERT INTO siconv.acffo_po " +
				"(id, dt_base, submeta_fk, in_desonerado, sg_localidade, dt_previsao_inicio_obra, qt_meses_duracao_obra, nr_versao_hibernate, in_acompanhamento_eventos, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, now(), 1, false, '', now(), 1, 0, false, 0, '', ''); " +

				"INSERT INTO siconv.acffo_evento " +
				"(id, nm_evento, po_fk, nr_evento, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_frente_obra " +
				"(id, nm_frente_obra, po_fk, nr_frente_obra, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 1, 0, 0, '', ''); ";
	}

	private String getSubitemInvestimento() {
		return "INSERT INTO siconv.und_fornecimento\n" +
				"(id, hibernate_version, codigo, descricao, adt_operacao, adt_data_hora, adt_login)\n" +
				"VALUES(1, 0, 'cod', 'desc', 'ND'::character varying, now(), 'ND'::character varying); " +

				"INSERT INTO siconv.sub_it_investimento " +
				"(id, hibernate_version, adt_login, adt_data_hora, adt_operacao, descricao, it_investimento_fk, und_fornecimento_fk, in_tipo_projeto_social) " +
				"VALUES(1, 0, '', now(), '', 'desc', 1, 1, 'in'); ";
	}

	private String getNaturezaDespesa() {
		return "INSERT INTO siconv.natureza_despesa_sub_it " +
				"(id, hibernate_version, sub_item, descricao_sub_item, observacao, natureza_despesa_fk, adt_operacao, adt_data_hora, adt_login, tipo_despesa) " +
				"VALUES(1, 0, 'subitem', 'descricao', 'observaco', 1, 'ND'::character varying, now(), 'ND'::character varying, 'tipo'); ";
	}

	private String getAcffoLotes() {
		return "INSERT INTO siconv.acffo " +
				"(id, tx_apelido, prop_fk, nr_versao_hibernate, in_situacao, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 0, '', 0, '', ''); " +

				"INSERT INTO siconv.acffo_qci " +
				"(id, acffo_fk, nr_versao_hibernate, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, 1, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_meta " +
				"(id, tx_descricao, qt_itens, subitem_investimento_fk, qci_fk, nr_versao_hibernate, nr_meta, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 0, 0, 1, 0, 0, 0, '', ''); " +

				"INSERT INTO siconv.acffo_submeta " +
				"(id, tx_descricao, nr_lote, vl_repasse, vl_contrapartida, vl_outros, meta_fk, vl_total, nr_versao_hibernate, nr_submeta, in_situacao, in_regime_execucao, natureza_despesa_sub_it_fk, versao_nr, versao_id, versao_nm_evento) " +
				"VALUES(1, '', 1, 0, 0, 0, 1, 0, 0, 0, '', 'EPG', 0, 0, '', ''); ";
	}

	private String getPropostaInsert() {
		return "INSERT INTO siconv.prop " +
				"(id, hibernate_version, inicio_execucao, fim_execucao, justificativa, valor_global, valor_repasse, " //7
				+ "valor_contra_partida, valor_repasse_exercicio_atual, sequencial, ano, data_proposta, convenio_fk, " //13
				+ "executor_fk, objeto_fk, programa_fk, proponente_fk, valor_contrapartida_financeira, " //18
				+ "valor_contrapartida_bens_servi, org_administrativo_fk, modalidade, regras_fk, mandatario_fk, " // 22
				+ "propostas_mandataria_fk, objeto_convenio, conta_bancaria_fk, agencia, data_versao, adt_operacao, " // 28
				+ "adt_data_hora, adt_login, membro_partc_fk, ug_executora_fk, gestao, situacao_envio_contrato_repass, " // 34
				+ "capacidade_tecnica, teste_capacidade_tecnica, ver_at_fk, historico, empenho_publicacao, " // 40
				+ "instituicao_mandataria_fk, org_concedente_fk, org_executor_fk, data_entrega_projeto_basico, data_limite_complementacao_pb, data_limite_entrega_projeto_ba, permite_adiar_entrega_proj_bas, permite_liberar_primeiro_repas, permite_prorrogar_entrega_proj, prazo_entrega_proj_basico, situacao_projeto_basico, tipo_projeto_basico, projeto_basico_fk, data_ultimo_envio_contrato_rep, situacao_legado, data_aprovacao_plano_trabalho, data_aprovacao_de_proposta, atribuicao_resp_analise, complementacao, situacao_proposta, ug_contrato_publicacao_fk, tipo_nao_acatamento_contrato_r, gestao_contrato_publicacao, data_ultimo_retorno_contrato_r, condicionante_liberacao_primei, descricao_condicionante, assinatura_pendente_ajuste_cro, efetivo, enviada_para_instituicao, valor_aplicacao, fim_execucao_anterior_antecipa, anexo_termo_convenio_fk, justificativa_termo_convenio, numero_controle_externo, numero_processo_connect, data_confirmacao_entrega_caixa, pontuacao, ordenacao, justificativa_celebracao_antec, documentos_aprovados, documentos_aprovacao_observaca, documentos_aprovacao_data, primeira_analise_p_t_realizada, data_analise_projeto_basico) " +
				"VALUES(1, 0, now(), now(), '', 0, 0, 0, 0, 0, 0, now(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, '', now(), " // 28
				+ "'ND'::character varying, now(), 'ND'::character varying, 0, 0, '', '', '', '', 0, false, ''" // 40
				+ ", 1, 0, 0, now(), now(), now(), false, false, false, 0, 'NAO_CADASTRADO'::character varying, '', 0, now(), 0, now(), now(), '', '', '', 0, '', '', now(), false, '', false, true, false, 0, now(), 0, NULL::character varying, '', 0, now(), 0, 0, '', false, '', now(), false, now()); ";
	}

	private String getContratoInsert() {
		return "INSERT INTO siconv.contrato " +
				" (id, hibernate_version, licitacao_fk, contrato_original_fk, ano, sequencial, data_fim_vigencia, data_inicio_vigencia, data_assinatura, valor_global, data_publicacao, objeto, tipo_aquisicao, tipo_contrato, fornecedor_fk, adt_operacao, adt_data_hora, adt_login) " +
				" VALUES(1, 0, 1, 0, '', '', now(), now(), now(), 0, now(), '', '', '', 0, 'ND'::character varying, now(), 'ND'::character varying); ";
	}

	private String getUfInsert() {
		return "INSERT INTO siconv.und_federativa (id, hibernate_version, sigla, nome, pais_fk, adt_operacao, adt_data_hora, adt_login) VALUES (1, 880, 'AC', 'Acre', 1, 'ATUALIZACAO', '2018-08-22 12:18:01.633', '85156680259');";
	}
	
	private String getSinapiInsert() {
		return "INSERT INTO siconv.acffo_sinapi (id, cd_item, sg_localidade, vl_desonerado, vl_nao_desonerado, tp_sinapi, dt_referencia, sg_unidade, tx_descricao_item) "+
				" VALUES (1,	83520,	'AC',	105.07,	108.90,	'C',	'2017-06-01',	'UN',	'TE PVC PARA COLETOR ESGOTO, EB644, D=100MM, COM JUNTA ELASTICA.');";		
		
	}
	
}
