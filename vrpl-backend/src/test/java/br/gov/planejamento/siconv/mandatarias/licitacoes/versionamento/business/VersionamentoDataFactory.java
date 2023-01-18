package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

public class VersionamentoDataFactory {

	public static final String INSERT_PROPOSTA = "INSERT INTO siconv.vrpl_proposta "
			+ " (id, id_siconv, numero_proposta, ano_proposta, valor_global, valor_repasse, valor_contrapartida, modalidade, nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa, nome_programa, identificacao_proponente, nome_proponente, uf, pc_min_contrapartida, nome_mandataria, categoria, nivel_contrato, spa_homologado, apelido_empreendimento, adt_login, adt_data_hora, adt_operacao, versao, versao_id, versao_nm_evento, versao_nr, versao_in_atual) "
			+ " VALUES(78, 1332263, 31406, 2018, 338160.59, 232380.95, 105779.64, 2, 'RECAPEAMENTO ASFÁLTICO', 869494, 2018, '2018-06-29', '5600020180001', 'PLANEJAMENTO URBANO', '87564381000110', 'MUNICIPIO DE IBIRUBA', 'RS', 0.10, 'CAIXA ECONOMICA FEDERAL', 'OBRAS_SERVICOS_ENGENHARIA', 'CONTRATO_NIVEL_I', true, 'apelido', current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', 1, NULL, NULL, 0, true);";

	public static final String INSERT_PROPOSTA_QUE_NAO_DEVE_SER_ALTERADA = "INSERT INTO siconv.vrpl_proposta "
			+ " (id, id_siconv, numero_proposta, ano_proposta, valor_global, valor_repasse, valor_contrapartida, modalidade, nome_objeto, numero_convenio, ano_convenio, data_assinatura_convenio, codigo_programa, nome_programa, identificacao_proponente, nome_proponente, uf, pc_min_contrapartida, nome_mandataria, categoria, nivel_contrato, spa_homologado, apelido_empreendimento, adt_login, adt_data_hora, adt_operacao, versao, versao_id, versao_nm_evento, versao_nr, versao_in_atual) "
			+ " VALUES(15, 1000000, 25000, 2018, 100000.00, 20000.00, 10000.00, 2, 'RECAPEAMENTO ASFÁLTICO', 869494, 2018, '2018-06-29', '5600020180001', 'PLANEJAMENTO URBANO', '87564381000110', 'MUNICIPIO DE IBIRUBA', 'RS', 0.10, 'CAIXA ECONOMICA FEDERAL', 'OBRAS_SERVICOS_ENGENHARIA', 'CONTRATO_NIVEL_I', true, 'apelido', current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', 1, NULL, NULL, 0, true);";

	public static final String INSERT_LICITACAO_1 = "INSERT INTO siconv.vrpl_licitacao "
			+ " (proposta_fk, numero_ano, objeto, "
			+ "  valor_processo, status_processo, id_licitacao_fk, adt_login, adt_data_hora, adt_operacao, in_situacao, versao_nr, versao_id, versao_nm_evento, versao,"
			+ "  modalidade, regime_contratacao, data_publicacao, data_homologacao, processo_de_execucao) "
			+ " VALUES(78, '0062018', 'Contratação de empresa especializada para execução de empreitada global (material) para obras de recapeamento asfáltico de 6.181,30 m² na Rua Tiradentes (297,40 m x 14,50 m / 133,50 m x 14,00 m = 6.181,30 m²) no município de Ibirubá - RS, em atendimento ao Contrato de Repasse OGU nº 869494/2018 – Operação 1054920-25 – Programa Planejamento Urbano, conforme Memorial Descritivo, Planilha Orçamentária e Cronogramas que fazem parte do Edital', "
			+ "  7836.20, 'Concluído', 631230, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', 'ENV', 0, null, null, 1,"
			+ " 'Tomada de Preços', 'Lei 8.666/1993', '2018-10-26', '2018-12-04', 'Licitação');";

	public static final String INSERT_LICITACAO_2 = "INSERT INTO siconv.vrpl_licitacao "
			+ " (proposta_fk, numero_ano, objeto, "
			+ "  valor_processo, status_processo, id_licitacao_fk, adt_login, adt_data_hora, adt_operacao, in_situacao, versao_nr, versao_id, versao_nm_evento, versao,"
			+ "  modalidade, regime_contratacao, data_publicacao, data_homologacao, processo_de_execucao) "
			+ " VALUES(78, '0072018', 'Contratação de empresa especializada para execução de empreitada global (mão de obra) para obras de recapeamento asfáltico de 6.181,30 m² na Rua Tiradentes (297,40 m x 14,50 m / 133,50 m x 14,00 m = 6.181,30 m²) no município de Ibirubá - RS, em atendimento ao Contrato de Repasse OGU nº 869494/2018 – Operação 1054920-25 – Programa Planejamento Urbano, conforme Memorial Descritivo, Planilha Orçamentária e Cronogramas que fazem parte do Edital', "
			+ "  330000.00, 'Concluído', 631230, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', 'ENV', 0, null, null, 1,"
			+ " 'Tomada de Preços', 'Lei 8.666/1993', '2018-10-26', '2018-12-04', 'Licitação');";

	public static final String INSERT_LICITACAO_QUE_NAO_DEVE_SER_ALTERADA = "INSERT INTO siconv.vrpl_licitacao "
			+ " (proposta_fk, numero_ano, objeto, "
			+ "  valor_processo, status_processo, id_licitacao_fk, adt_login, adt_data_hora, adt_operacao, in_situacao, versao_nr, versao_id, versao_nm_evento, versao,"
			+ "  modalidade, regime_contratacao, data_publicacao, data_homologacao, processo_de_execucao) "
			+ " VALUES(15, '0072018', 'Contratação de empresa especializada para execução de empreitada global (mão de obra) para obras de recapeamento asfáltico de 6.181,30 m² na Rua Tiradentes (297,40 m x 14,50 m / 133,50 m x 14,00 m = 6.181,30 m²) no município de Ibirubá - RS, em atendimento ao Contrato de Repasse OGU nº 869494/2018 – Operação 1054920-25 – Programa Planejamento Urbano, conforme Memorial Descritivo, Planilha Orçamentária e Cronogramas que fazem parte do Edital', "
			+ "  330000.00, 'Concluído', 631230, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', 'ENV', 0, null, null, 1,"
			+ " 'Tomada de Preços', 'Lei 8.666/1993', '2018-10-26', '2018-12-04', 'Licitação');";

	public static final String INSERT_FORNECEDOR_PARA_LICITACAO_1 = "INSERT INTO siconv.vrpl_fornecedor "
			+ "(licitacao_fk, razao_social, tipo_identificacao, identificacao, versao_nr, versao_id, versao_nm_evento, versao, adt_login, adt_data_hora, adt_operacao) "
			+ "VALUES(1, 'CMP PROJETOS E CONSTRUCOES LTDA', 'CNPJ', '23207621000146', 0, NULL, NULL, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT');";

	public static final String INSERT_FORNECEDOR_PARA_LICITACAO_2 = "INSERT INTO siconv.vrpl_fornecedor "
			+ "(licitacao_fk, razao_social, tipo_identificacao, identificacao, versao_nr, versao_id, versao_nm_evento, versao, adt_login, adt_data_hora, adt_operacao) "
			+ "VALUES(2, 'CONSTRUTORA E PAVIMENTADORA PAVICON LTDA', 'CNPJ', '88256979000104', 0, NULL, NULL, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT');";

	public static final String INSERT_FORNECEDOR_PARA_LICITACAO_QUE_NAO_DEVE_SER_ALTERADA = "INSERT INTO siconv.vrpl_fornecedor "
			+ "(licitacao_fk, razao_social, tipo_identificacao, identificacao, versao_nr, versao_id, versao_nm_evento, versao, adt_login, adt_data_hora, adt_operacao) "
			+ "VALUES(3, 'NOROMIX CONCRETO S/A', 'CNPJ', '10558895000138', 0, NULL, NULL, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT');";

}
