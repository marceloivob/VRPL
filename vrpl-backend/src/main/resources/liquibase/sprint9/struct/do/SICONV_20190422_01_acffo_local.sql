CREATE TABLE siconv.acffo (
	id bigserial NOT NULL,
	tx_apelido varchar(50) NOT NULL,
	prop_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_situacao varchar(3) NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_anexo (
	id bigserial NOT NULL,
	acffo_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	nm_arquivo varchar(100) NOT NULL,
	caminho varchar(1024) NOT NULL,
	dt_upload timestamp NOT NULL,
	tx_descricao varchar(30) NOT NULL,
	in_tipoanexo varchar(20) NULL,
	id_cpf_enviadopor int4 NULL,
	in_perfil varchar(10) NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_art_rrt (
	id bigserial NOT NULL,
	nu_art_rrt varchar(50) NOT NULL,
	dt_emissao date NULL,
	responsavel_tecnico_fk int8 NOT NULL,
	anexo_fk int8 NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_doc_complementar (
	id bigserial NOT NULL,
	tx_orgao_emissor varchar(100) NOT NULL,
	dt_emissao date NOT NULL,
	dt_valida_ate date NOT NULL,
	nr_documento varchar(40) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	anexo_fk int8 NOT NULL,
	tp_manif_ambiental_fk int8 NULL,
	tp_doc_complementar_fk int8 NOT NULL,
	acffo_fk int8 NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_doc_complementar_meta (
	doc_complementar_fk int8 NOT NULL,
	meta_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	dt_selecao date NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_evento (
	id bigserial NOT NULL,
	nm_evento varchar(100) NOT NULL,
	po_fk int8 NOT NULL,
	nr_evento int2 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX fki_fkc_acffo_evento_acffo_po ON siconv.acffo_evento USING btree (po_fk);

CREATE TABLE siconv.acffo_evento_frente_obra (
	evento_fk int8 NOT NULL,
	frente_obra_fk int8 NOT NULL,
	nr_mes_conclusao int2 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_frente_obra (
	id bigserial NOT NULL,
	nm_frente_obra varchar(100) NOT NULL,
	po_fk int8 NOT NULL,
	nr_frente_obra int2 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX fki_fkc_acffo_frente_obra_acffo_po ON siconv.acffo_frente_obra USING btree (po_fk);

CREATE TABLE siconv.acffo_grupo_pergunta (
	id bigserial NOT NULL,
	numero int4 NOT NULL DEFAULT 1,
	titulo varchar(500) NOT NULL,
	template_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_grupo_obrigatorio bool NOT NULL DEFAULT false,
	versao_nr int4 NULL,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX ind_acffo_grupo_pergunta_template_fk ON siconv.acffo_grupo_pergunta USING btree (template_fk);

CREATE TABLE siconv.acffo_historico (
	id bigserial NOT NULL,
	dt_registro timestamp NOT NULL,
	in_evento bpchar(3) NOT NULL,
	nm_responsavel varchar(60) NOT NULL,
	tx_consideracoes text NULL,
	in_situacao bpchar(3) NOT NULL,
	nr_cpf_responsavel bpchar(11) NOT NULL,
	acffo_fk int8 NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0
);

CREATE TABLE siconv.acffo_laudo (
	id bigserial NOT NULL,
	qci_fk int8 NOT NULL,
	template_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_status int2 NOT NULL DEFAULT 1,
	in_resultado bpchar(2) NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX ind_acffo_laudo_template_fk ON siconv.acffo_laudo USING btree (template_fk);

CREATE TABLE siconv.acffo_macro_servico (
	id bigserial NOT NULL,
	tx_descricao varchar(100) NOT NULL,
	po_fk int8 NOT NULL,
	nr_macro_servico int2 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_macro_servico_parcela (
	id bigserial NOT NULL,
	nr_parcela int2 NOT NULL,
	pc_parcela numeric(5,2) NOT NULL,
	macro_servico_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_meta (
	id bigserial NOT NULL,
	tx_descricao varchar(100) NOT NULL,
	qt_itens numeric(17,2) NOT NULL,
	subitem_investimento_fk int8 NOT NULL,
	qci_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	nr_meta int4 NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_pendencia (
	id bigserial NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	laudo_fk int8 NOT NULL,
	in_resolvida bool NOT NULL DEFAULT false,
	descricao varchar(1500) NOT NULL,
	prazo varchar(5) NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_pendencia_submeta (
	pendencia_fk int8 NOT NULL,
	submeta_fk int8 NOT NULL
);

CREATE TABLE siconv.acffo_pergunta (
	id bigserial NOT NULL,
	numero int4 NOT NULL DEFAULT 1,
	titulo varchar(500) NOT NULL,
	tipo_resposta varchar(10) NOT NULL,
	valor_resposta varchar(100) NOT NULL,
	grupo_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	valor_esperado varchar(50) NOT NULL DEFAULT ''::character varying,
	versao_nr int4 NULL,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX ind_acffo_pergunta_grupo_fk ON siconv.acffo_pergunta USING btree (grupo_fk);

CREATE TABLE siconv.acffo_po (
	id bigserial NOT NULL,
	dt_base date NOT NULL,
	submeta_fk int8 NOT NULL,
	in_desonerado bool NOT NULL,
	sg_localidade bpchar(2) NOT NULL,
	dt_previsao_inicio_obra date NOT NULL,
	qt_meses_duracao_obra int2 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_acompanhamento_eventos bool NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX acffo_po_submeta_fk_idx ON siconv.acffo_po USING btree (submeta_fk);

CREATE TABLE siconv.acffo_projeto_social (
	id bigserial NOT NULL,
	in_tipo_projeto_social varchar(4) NOT NULL,
	loc_area_intervencao text NOT NULL,
	obj_interv_fisica text NOT NULL,
	carac_area_intervencao text NOT NULL,
	nr_familias numeric(7) NOT NULL,
	nr_pessoas numeric(7) NOT NULL,
	nr_familias_risco numeric(6) NOT NULL,
	nr_mulheres_chefe numeric(6) NOT NULL,
	nr_idosos_chefe numeric(6) NOT NULL,
	nr_idosos numeric(6) NOT NULL,
	nr_deficientes numeric(6) NOT NULL,
	renda_media_familiar numeric(8,2) NOT NULL,
	dados_adicionais text NULL,
	justificativa_ts text NOT NULL,
	objetivo_ts text NOT NULL,
	qci_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	nr_familias_reassentadas numeric(6) NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_projeto_social_acao (
	id bigserial NOT NULL,
	acao varchar(200) NOT NULL,
	projeto_social_eixo_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_projeto_social_atividade (
	id bigserial NOT NULL,
	macro_servico_fk int8 NOT NULL,
	projeto_social_acao_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_projeto_social_eixo (
	id bigserial NOT NULL,
	eixo varchar(50) NOT NULL,
	projeto_social_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_projeto_social_responsavel_tecnico (
	responsavel_tecnico_fk int8 NOT NULL,
	projeto_social_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0
);

CREATE TABLE siconv.acffo_proposta (
	id int8 NOT NULL,
	prop_siconv_id int8 NOT NULL,
	numero_proposta int4 NOT NULL,
	ano_proposta int4 NOT NULL,
	valor_global numeric(17,2) NOT NULL,
	valor_repasse numeric(17,2) NOT NULL,
	valor_contra_partida numeric(17,2) NOT NULL,
	modalidade int4 NOT NULL,
	nome_objeto varchar(5000) NULL,
	numero_convenio int4 NULL,
	ano_convenio int4 NULL,
	data_assinatura_convenio date NULL,
	codigo_programa varchar(13) NOT NULL,
	nome_programa varchar(255) NOT NULL,
	identificacao_proponente varchar(1024) NULL,
	nome_proponente varchar(1024) NULL,
	uf varchar(2) NOT NULL,
	pc_min_contra_partida numeric(3,2) NULL,
	nome_mandataria varchar(1024) NULL,
	categoria varchar(50) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	nivel_contrato varchar(20) NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX acffo_proposta_id_idx ON siconv.acffo_proposta USING btree (prop_siconv_id);

CREATE TABLE siconv.acffo_qci (
	id bigserial NOT NULL,
	acffo_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_responsavel_tecnico (
	id bigserial NOT NULL,
	nm_responsavel varchar(100) NOT NULL,
	ds_titulo varchar(100) NOT NULL,
	cd_registro_conselho varchar(50) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_responsavel_po bool NULL DEFAULT false,
	in_responsavel_cff bool NULL DEFAULT false,
	nr_cpf_responsavel bpchar(11) NOT NULL,
	tx_formacao varchar(100) NULL,
	tx_registro_profissional varchar(100) NULL,
	nu_telefone numeric(14) NULL,
	tx_email varchar(100) NULL,
	tx_orgao_responsavel varchar(100) NULL,
	nu_tel_orgao numeric(14) NULL,
	tx_email_orgao varchar(100) NULL,
	acffo_fk int8 NULL,
	und_federativa_fk int8 NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_responsavel_tecnico_po (
	responsavel_tecnico_fk int8 NOT NULL,
	po_fk int8 NOT NULL,
	art_rrt_fk int8 NULL,
	in_tipo varchar(3) NOT NULL,
	in_fase varchar(3) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_resposta (
	id bigserial NOT NULL,
	pergunta_fk int8 NOT NULL,
	resposta varchar(1500) NULL,
	laudo_fk int8 NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX ind_acffo_resposta_pergunta_fk ON siconv.acffo_resposta USING btree (pergunta_fk);

CREATE TABLE siconv.acffo_resposta_grupo_pergunta (
	id bigserial NOT NULL,
	grupo_pergunta_fk int8 NOT NULL,
	laudo_fk int8 NOT NULL,
	in_nao_aplicavel bool NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_servico (
	id bigserial NOT NULL,
	tx_observacao varchar(500) NULL,
	macro_servico_fk int8 NOT NULL,
	nr_servico int2 NOT NULL,
	cd_servico varchar(13) NOT NULL,
	tx_descricao varchar(508) NOT NULL,
	sg_unidade varchar(10) NOT NULL,
	vl_custo_unitario_ref numeric(17,2) NOT NULL DEFAULT 0,
	pc_bdi numeric(17,2) NOT NULL DEFAULT 0,
	qt_total_itens numeric(17,2) NOT NULL DEFAULT 0,
	vl_custo_unitario numeric(17,2) NOT NULL DEFAULT 0,
	vl_preco_unitario numeric(17,2) NOT NULL DEFAULT 0,
	vl_preco_total numeric(17,2) NOT NULL DEFAULT 0,
	evento_fk int8 NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	in_fonte bpchar(3) NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);
CREATE INDEX fki_fkc_acffo_servico_acffo_nivel ON siconv.acffo_servico USING btree (macro_servico_fk);

CREATE TABLE siconv.acffo_servico_frente_obra (
	servico_fk int8 NOT NULL,
	frente_obra_fk int8 NOT NULL,
	qt_itens numeric(17,2) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_spa (
	id bigserial NOT NULL,
	acffo_fk int8 NOT NULL,
	acao varchar(8) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL,
	id_municipio int8 NULL,
	nome_municipio varchar(40) NULL,
	codigo_ibge varchar(7) NULL,
	programa_de_trabalho varchar(40) NULL,
	comentario text NULL,
	sequencial int4 NULL,
	ano int4 NULL,
	data_assinatura date NULL
);

CREATE TABLE siconv.acffo_spa_participantes (
	id bigserial NOT NULL,
	id_spa_fk int8 NOT NULL,
	tipo_participante varchar(14) NOT NULL,
	natureza_juridica varchar(41) NOT NULL,
	id_participante varchar(14) NOT NULL,
	cnpj_entidade varchar(14) NOT NULL,
	nome_entidade varchar(1024) NOT NULL,
	id_responsavel varchar(14) NULL,
	nome_responsavel varchar(1024) NULL,
	email_orgao varchar(1024) NULL,
	telefone_orgao varchar(1024) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_submeta (
	id bigserial NOT NULL,
	tx_descricao varchar(100) NOT NULL,
	nr_lote int2 NOT NULL,
	vl_repasse numeric(17,2) NOT NULL,
	vl_contrapartida numeric(17,2) NOT NULL,
	vl_outros numeric(17,2) NULL,
	meta_fk int8 NOT NULL,
	vl_total numeric(17,2) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	nr_submeta int4 NOT NULL DEFAULT 0,
	in_situacao bpchar(3) NOT NULL,
	in_regime_execucao bpchar(3) NULL,
	natureza_despesa_sub_it_fk int8 NOT NULL,
	versao_nr int4 NOT NULL DEFAULT 0,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_template_laudo (
	id bigserial NOT NULL,
	tipo varchar(10) NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NULL,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_tipo_art_rrt (
	art_rrt_id int4 NOT NULL,
	tipo varchar(3) NOT NULL
);

CREATE TABLE siconv.acffo_tp_doc_complementar (
	id bigserial NOT NULL,
	tx_nome varchar(50) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NULL,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_tp_manif_ambiental (
	id bigserial NOT NULL,
	tx_nome varchar(50) NOT NULL,
	nr_versao_hibernate int4 NOT NULL DEFAULT 0,
	versao_nr int4 NULL,
	versao_id varchar(20) NULL,
	versao_nm_evento varchar(3) NULL
);

CREATE TABLE siconv.acffo_sinapi (
	id bigserial NOT NULL,
	cd_item varchar(13) NOT NULL,
	sg_localidade bpchar(2) NOT NULL,
	vl_desonerado numeric(11,2) NOT NULL,
	vl_nao_desonerado numeric(11,2) NOT NULL,
	tp_sinapi bpchar(1) NOT NULL,
	dt_referencia date NOT NULL,
	dt_registro date NOT NULL DEFAULT 'now'::text::date,
	sg_unidade varchar(10) NOT NULL,
	tx_descricao_item varchar(508) NOT NULL
);

CREATE TABLE siconv.prop (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	inicio_execucao date NULL,
	fim_execucao date NULL,
	justificativa varchar(5000) NULL,
	valor_global float8 NOT NULL,
	valor_repasse float8 NOT NULL,
	valor_contra_partida float8 NOT NULL,
	valor_repasse_exercicio_atual float8 NULL,
	sequencial int4 NOT NULL,
	ano int4 NOT NULL,
	data_proposta date NOT NULL,
	convenio_fk int8 NULL,
	executor_fk int8 NULL,
	objeto_fk int8 NULL,
	programa_fk int8 NULL,
	proponente_fk int8 NOT NULL,
	valor_contrapartida_financeira float8 NOT NULL,
	valor_contrapartida_bens_servi float8 NOT NULL,
	org_administrativo_fk int8 NULL,
	modalidade int4 NOT NULL,
	regras_fk int8 NULL,
	mandatario_fk int8 NULL,
	propostas_mandataria_fk int8 NULL,
	objeto_convenio varchar(5000) NULL,
	conta_bancaria_fk int8 NULL,
	agencia varchar(1024) NULL,
	data_versao timestamp NULL,
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying,
	adt_data_hora timestamp NOT NULL DEFAULT now(),
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying,
	membro_partc_fk int8 NULL,
	ug_executora_fk int8 NULL,
	gestao varchar(5) NULL,
	situacao_envio_contrato_repass varchar(1024) NULL,
	capacidade_tecnica varchar(5000) NULL,
	teste_capacidade_tecnica varchar(5000) NULL,
	ver_at_fk int8 NULL,
	historico bool NULL,
	empenho_publicacao varchar(1024) NULL,
	instituicao_mandataria_fk int8 NULL,
	org_concedente_fk int8 NOT NULL,
	org_executor_fk int8 NOT NULL,
	data_entrega_projeto_basico date NULL,
	data_limite_complementacao_pb date NULL,
	data_limite_entrega_projeto_ba date NULL,
	permite_adiar_entrega_proj_bas bool NULL,
	permite_liberar_primeiro_repas bool NULL,
	permite_prorrogar_entrega_proj bool NULL,
	prazo_entrega_proj_basico int4 NULL,
	situacao_projeto_basico varchar(1024) NOT NULL DEFAULT 'NAO_CADASTRADO'::character varying,
	tipo_projeto_basico varchar(1024) NULL,
	projeto_basico_fk int8 NULL,
	data_ultimo_envio_contrato_rep timestamp NULL,
	situacao_legado int4 NULL DEFAULT 0,
	data_aprovacao_plano_trabalho timestamp NULL,
	data_aprovacao_de_proposta timestamp NULL,
	atribuicao_resp_analise varchar(1024) NULL,
	complementacao varchar(5000) NULL,
	situacao_proposta varchar(1024) NULL,
	ug_contrato_publicacao_fk int8 NULL,
	tipo_nao_acatamento_contrato_r varchar(1024) NULL,
	gestao_contrato_publicacao varchar(5) NULL,
	data_ultimo_retorno_contrato_r timestamp NULL,
	condicionante_liberacao_primei bool NULL,
	descricao_condicionante varchar(1024) NULL,
	assinatura_pendente_ajuste_cro bool NULL,
	efetivo bool NOT NULL DEFAULT true,
	enviada_para_instituicao bool NULL DEFAULT false,
	valor_aplicacao numeric(15,2) NULL,
	fim_execucao_anterior_antecipa date NULL,
	anexo_termo_convenio_fk int8 NULL,
	justificativa_termo_convenio varchar(1024) NULL DEFAULT NULL::character varying,
	numero_controle_externo varchar(60) NULL,
	numero_processo_connect int4 NULL,
	data_confirmacao_entrega_caixa timestamp NULL,
	pontuacao float8 NULL,
	ordenacao int4 NULL,
	justificativa_celebracao_antec varchar(1024) NULL,
	documentos_aprovados bool NULL DEFAULT false,
	documentos_aprovacao_observaca varchar(1024) NULL,
	documentos_aprovacao_data date NULL,
	primeira_analise_p_t_realizada bool NULL,
	data_analise_projeto_basico date NULL
);

CREATE TABLE siconv.sub_it_investimento (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	adt_login varchar(30) NOT NULL,
	adt_data_hora timestamp NOT NULL,
	adt_operacao varchar(12) NOT NULL,
	descricao varchar(1024) NOT NULL,
	it_investimento_fk int8 NOT NULL,
	und_fornecimento_fk int8 NULL,
	in_tipo_projeto_social varchar(4) NULL
);

CREATE TABLE siconv.und_fornecimento (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	codigo varchar(1024) NOT NULL,
	descricao varchar(1024) NOT NULL,
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying,
	adt_data_hora timestamp NOT NULL DEFAULT now(),
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying
);

CREATE TABLE siconv.contrato (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	licitacao_fk int8 NOT NULL,
	contrato_original_fk int8 NULL,
	ano varchar(4) NOT NULL,
	sequencial varchar(6) NOT NULL,
	data_fim_vigencia date NOT NULL,
	data_inicio_vigencia date NOT NULL,
	data_assinatura date NOT NULL,
	valor_global float8 NOT NULL,
	data_publicacao date NULL,
	objeto varchar(5000) NOT NULL,
	tipo_aquisicao varchar(1024) NOT NULL,
	tipo_contrato varchar(1024) NOT NULL,
	fornecedor_fk int8 NOT NULL,
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying,
	adt_data_hora timestamp NOT NULL DEFAULT now(),
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying
);

CREATE TABLE siconv.und_federativa (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	sigla varchar(1024) NOT NULL,
	nome varchar(1024) NOT NULL,
	pais_fk int8 NOT NULL,
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying,
	adt_data_hora timestamp NOT NULL DEFAULT now(),
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying
);

CREATE TABLE siconv.natureza_despesa_sub_it (
	id int8 NOT NULL,
	hibernate_version int4 NOT NULL DEFAULT 0,
	sub_item varchar(1024) NOT NULL,
	descricao_sub_item varchar(1024) NOT NULL,
	observacao varchar(5000) NULL,
	natureza_despesa_fk int8 NOT NULL,
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying,
	adt_data_hora timestamp NOT NULL DEFAULT now(),
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying,
	tipo_despesa varchar(1024) NULL
);

-- Drop table

-- DROP TABLE siconv.fornecedores;

CREATE TABLE siconv.fornecedores (
	id int8 NOT NULL, -- Identificador único da tabela fornecedores.
	hibernate_version int4 NOT NULL DEFAULT 0, -- Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.
	razao_social varchar(150) NOT NULL, -- Razão Social do fornecedor.
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying, -- Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). 
	adt_data_hora timestamp NOT NULL DEFAULT now(), -- Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying, -- CPF do usuário que executou a operação.
	tipo_identificacao varchar(1024) NULL, -- Tipo de Identificação do fornecedor, podendo ser: "CNPJ", "CPF", "IG" (Inscrição Genérica).
	identificacao varchar(1024) NULL, -- Identificação do fornecedor, podendo ser o número do CNPJ, número do CPF ou número da Inscrição Genérica.
	fornecedor_inativo_fk int8 NULL, -- Identificador único da tabela fornecedor_inativo.
	eh_consorcio bool NULL, -- Campo booleano se 't' significa que é um CNPJ relacionado a Consórcio Público.
	CONSTRAINT fornecedores_pkey PRIMARY KEY (id),
	CONSTRAINT fornecedores_tipo_identificacao_check CHECK (((tipo_identificacao)::text = ANY (ARRAY[('CNPJ'::character varying)::text, ('CPF'::character varying)::text, ('IG'::character varying)::text])))
);

COMMENT ON TABLE siconv.fornecedores IS 'Tabela onde são persistidos os dados dos fornecedores.';

-- Column comments

COMMENT ON COLUMN siconv.fornecedores.id IS 'Identificador único da tabela fornecedores.';
COMMENT ON COLUMN siconv.fornecedores.hibernate_version IS 'Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.';
COMMENT ON COLUMN siconv.fornecedores.razao_social IS 'Razão Social do fornecedor.';
COMMENT ON COLUMN siconv.fornecedores.adt_operacao IS 'Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). ';
COMMENT ON COLUMN siconv.fornecedores.adt_data_hora IS 'Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.';
COMMENT ON COLUMN siconv.fornecedores.adt_login IS 'CPF do usuário que executou a operação.';
COMMENT ON COLUMN siconv.fornecedores.tipo_identificacao IS 'Tipo de Identificação do fornecedor, podendo ser: "CNPJ", "CPF", "IG" (Inscrição Genérica).';
COMMENT ON COLUMN siconv.fornecedores.identificacao IS 'Identificação do fornecedor, podendo ser o número do CNPJ, número do CPF ou número da Inscrição Genérica.';
COMMENT ON COLUMN siconv.fornecedores.fornecedor_inativo_fk IS 'Identificador único da tabela fornecedor_inativo.';
COMMENT ON COLUMN siconv.fornecedores.eh_consorcio IS 'Campo booleano se ''t'' significa que é um CNPJ relacionado a Consórcio Público.';


CREATE TABLE siconv.it_licitacao (
	id int8 NOT NULL, -- Identificador único da tabela it_licitacao.
	hibernate_version int4 NOT NULL DEFAULT 0, -- Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.
	sequencial int8 NOT NULL, -- Sequencial interno do item de licitação
	marca varchar(1024) NULL, -- Marca  do item de licitação
	quantidade float8 NOT NULL, -- Quantidade do item de licitação
	preco_unitario float8 NULL, -- Preço Unitário do item de licitação
	descricao varchar(5000) NOT NULL, -- Descrição do item de licitação
	fornecedor varchar(1024) NULL, -- Fornecedor do item de licitação
	unidade_fornecimento varchar(1024) NOT NULL, -- Unidade de Fornecimento do item de licitação
	licitacao_fk int8 NOT NULL, -- Identificador único da tabela licitacao.
	observacao varchar(5000) NULL, -- Observação do item de licitação
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying, -- Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). 
	adt_data_hora timestamp NOT NULL DEFAULT now(), -- Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying, -- CPF do usuário que executou a operação.
	fabricante varchar(50) NULL, -- Fabricante do item de licitação
	pagamento_convenio_fk int8 NULL, -- Identificador único da tabela pagamento_convenio.
	valor_total numeric(15,2) NULL, -- Valor Total do item de licitação
	material_cotacao_eletronica_fk int8 NULL, -- Identificador único da tabela material_cotacao_eletronica.
	servicos_cotacao_eletronica_fk int8 NULL, -- Identificador único da tabela sv_cotacao_eletronica.
	situacao_item_sc_fk int8 NULL, -- Chave da situação do Item no Sistema de Compras de Origem da Licitação
	CONSTRAINT it_licitacao_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE siconv.it_licitacao IS 'Tabela onde os dados de itens de licitação são persistidos.';
COMMENT ON COLUMN siconv.it_licitacao.id IS 'Identificador único da tabela it_licitacao.';
COMMENT ON COLUMN siconv.it_licitacao.hibernate_version IS 'Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.';
COMMENT ON COLUMN siconv.it_licitacao.sequencial IS 'Sequencial interno do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.marca IS 'Marca  do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.quantidade IS 'Quantidade do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.preco_unitario IS 'Preço Unitário do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.descricao IS 'Descrição do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.fornecedor IS 'Fornecedor do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.unidade_fornecimento IS 'Unidade de Fornecimento do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.licitacao_fk IS 'Identificador único da tabela licitacao.';
COMMENT ON COLUMN siconv.it_licitacao.observacao IS 'Observação do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.adt_operacao IS 'Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). ';
COMMENT ON COLUMN siconv.it_licitacao.adt_data_hora IS 'Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.';
COMMENT ON COLUMN siconv.it_licitacao.adt_login IS 'CPF do usuário que executou a operação.';
COMMENT ON COLUMN siconv.it_licitacao.fabricante IS 'Fabricante do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.pagamento_convenio_fk IS 'Identificador único da tabela pagamento_convenio.';
COMMENT ON COLUMN siconv.it_licitacao.valor_total IS 'Valor Total do item de licitação';
COMMENT ON COLUMN siconv.it_licitacao.material_cotacao_eletronica_fk IS 'Identificador único da tabela material_cotacao_eletronica.';
COMMENT ON COLUMN siconv.it_licitacao.servicos_cotacao_eletronica_fk IS 'Identificador único da tabela sv_cotacao_eletronica.';
COMMENT ON COLUMN siconv.it_licitacao.situacao_item_sc_fk IS 'Chave da situação do Item no Sistema de Compras de Origem da Licitação';


CREATE TABLE siconv.licitacao (
	id int8 NOT NULL, -- Identificador único da tabela licitacao.
	hibernate_version int4 NOT NULL DEFAULT 0, -- Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.
	numero varchar(1024) NULL, -- Número do Processo de Execução.
	inciso varchar(1024) NULL, -- Inciso referente ao fundamento legal.
	numero_processo varchar(1024) NULL, -- Número do Processo informado pelo usuário.
	objeto varchar(1024) NOT NULL, -- Objeto referente ao Processo de Execução.
	fundamento_legal varchar(1024) NULL, -- Fundamento Legal referente ao Processo de Execução.
	justificativa varchar(5000) NOT NULL, -- Justificativa referente ao Processo de Execução.
	nome_responsavel varchar(1024) NULL, -- Nome do responsável pelo Processo de Execução.
	funcao_responsavel varchar(1024) NULL, -- Função do responsável pelo Processo de Execução.
	municipio_fk int8 NOT NULL, -- Identificador único da tabela municipio.
	prop_fk int8 NOT NULL, -- Identificador único da tabela proposta.
	modalidade varchar(1024) NULL, -- Modalidade, depende do Recurso Nacional ou Internacional:Recurso - Lei 8.666/1993:  Convite, Tomada de Preços, Concorrência e Concurso. Recurso - Lei 10.520/2002 - Pregão: Pregão.
	valor float8 NULL, -- Valor total do Processo de Execução.
	data_publicacao date NULL, -- Data de publicação do Processo de Execução.
	data_homologacao date NULL, -- Data de homologação do Processo de Execução.
	data_realizacao_licitacao date NULL, -- Data de realização do Processo de Execução.
	caput varchar(1024) NULL, -- Caput referente ao fundamento legal.
	srp bool NULL, -- Campo booleano se 't' informa que Pregão possui registro de preço.
	cpf_homologador varchar(1024) NULL, -- CPF do homologador do Processo de Execução.
	adt_operacao varchar(12) NOT NULL DEFAULT 'ND'::character varying, -- Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). 
	adt_data_hora timestamp NOT NULL DEFAULT now(), -- Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.
	adt_login varchar(30) NOT NULL DEFAULT 'ND'::character varying, -- CPF do usuário que executou a operação.
	tipo_licitacao varchar(1024) NULL, -- Tipo da Licitação: depende da modalidade.Modalidade Convite: Menor Valor.Modalidade Tomada de Preço: Menor Preço, Melhor Técnica, Técnica e Preço.Modalidade Concorrência:  Internacional, Menor Preço, Melhor Técnica e Técnica e Preço.Modalidade Pregão:  Eletrônico, Eletrônico para Registro de Preço, Eletrônico por Maior Desconto, Eletrônico por Lote (grupo de itens), Presencial, Presencial para Registro de Preço, Presencial por Maior Desconto.
	processo_compra varchar(1024) NULL, -- Processo de Compras: Dispensa de Licitação, Inexigibilidade, Licitação, Cotação Prévia, Pesquisa de Mercado
	tipo_compra varchar(1024) NULL, -- Tipo de Compra: "Material", "Serviço" ou "Material/Serviço"
	numero_processo_cotacao varchar(1024) NULL, -- Número do Processo da Cotação.
	numero_cotacao varchar(1024) NULL, -- Número da Cotação realizada para a licitação
	data_inicio_cotacao date NULL, -- Data de Início da Cotação realizada para a licitação
	data_encerramento_cotacao date NULL, -- Data de Encerramento da Cotação realizada para a licitação
	valor_cotacao float8 NULL, -- Valor da Cotação realizada para a licitação
	cpf_homologador_cotacao varchar(1024) NULL, -- CPF do Homologador da Cotação realizada para a licitação
	numero_pesquisa_mercado varchar(1024) NULL, -- Número da Pesquisa de Mercado
	numero_licitacao varchar(1024) NULL, -- Número da Licitação
	data_encerramento_licitacao date NULL, -- Data de encerramento da licitação.
	numero_dispensa varchar(1024) NULL, -- Número da dispensa de Licitação.
	inciso_dispensa varchar(1024) NULL, -- Inciso referente a dispensa de licitação.
	data_solicitacao_dispensa date NULL, -- Data da solicitação da dispensa delicitação.
	valor_dispensa float8 NULL, -- Valor da dispensa de licitação.
	data_abertura_licitacao date NULL, -- Data de abertura da licitação.
	origem_recurso varchar(20) NULL, -- Origem do recurso depende do recurso financeiro:Recurso Financeiro Nacional: Lei 8.6666/1993, Lei 10.520/2002 - Pregão.Recurso Financeiro Internacional:  Lei 8.6666/1993, Lei 10.520/2002 - Pregão, Shopping, NCB (LPN), ICB(LPI).
	recurso_financeiro varchar(20) NULL, -- Recurso Financeiro: Nacional ou Internacional. 
	ano int4 NULL, -- Ano referente a cotação prévia ou eletrônica.
	numero_inexigbilidade varchar(100) NULL, -- Número de inexigibilidade do Processo de Compra.
	status varchar(20) NULL, -- Status: "Concluído", "Em Elaboração".
	tipo_processo_execucao varchar(20) NULL, -- Tipo de Processo de Execução: Processo de Compras, Subconvênio - Registro de Chamamento Público
	incluida_por_executor bool NULL, -- Campo booleano se 't' o registro foi incluído pelo Executor.
	ata_registro_precos_fk int8 NULL, -- Identificador único da tabela anexo_licitacao.
	sistema_origem_fk int8 NOT NULL DEFAULT 1, -- Chave do sistema de origem
	situacao_licitacao_sc_fk int8 NULL, -- Chave da situação do Sistema de Compras Externo
	id_externo int8 NULL, -- ID Externo
	CONSTRAINT licitacao_origem_recurso_check CHECK (((origem_recurso)::text = ANY (ARRAY[('INTERNO'::character varying)::text, ('EXTERNO'::character varying)::text]))),
	CONSTRAINT licitacao_pkey PRIMARY KEY (id),
	CONSTRAINT licitacao_recurso_financeiro_check CHECK (((recurso_financeiro)::text = ANY (ARRAY[('LEI'::character varying)::text, ('LEI_PREGAO'::character varying)::text, ('SHOPPING'::character varying)::text, ('NCB'::character varying)::text, ('ICB'::character varying)::text, ('LEI_PREGAO_NOVA'::character varying)::text, ('RDC'::character varying)::text]))),
	CONSTRAINT licitacao_status_check CHECK (((status)::text = ANY (ARRAY[('EM_ELABORACAO'::character varying)::text, ('CONCLUIDO'::character varying)::text]))),
	CONSTRAINT licitacao_tipo_processo_execucao_check CHECK (((tipo_processo_execucao)::text = ANY (ARRAY[('PROCESSO_COMPRA'::character varying)::text, ('SUBCONVENIO'::character varying)::text])))
);

COMMENT ON TABLE siconv.licitacao IS 'Tabela que contem dados do processo de execução (licitação).';
COMMENT ON COLUMN siconv.licitacao.id IS 'Identificador único da tabela licitacao.';
COMMENT ON COLUMN siconv.licitacao.hibernate_version IS 'Versão do artefato. Na inclusão do registro assume o valor zero, a cada update soma 1 ao valor.';
COMMENT ON COLUMN siconv.licitacao.numero IS 'Número do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.inciso IS 'Inciso referente ao fundamento legal.';
COMMENT ON COLUMN siconv.licitacao.numero_processo IS 'Número do Processo informado pelo usuário.';
COMMENT ON COLUMN siconv.licitacao.objeto IS 'Objeto referente ao Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.fundamento_legal IS 'Fundamento Legal referente ao Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.justificativa IS 'Justificativa referente ao Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.nome_responsavel IS 'Nome do responsável pelo Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.funcao_responsavel IS 'Função do responsável pelo Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.municipio_fk IS 'Identificador único da tabela municipio.';
COMMENT ON COLUMN siconv.licitacao.prop_fk IS 'Identificador único da tabela proposta.';
COMMENT ON COLUMN siconv.licitacao.modalidade IS 'Modalidade, depende do Recurso Nacional ou Internacional:Recurso - Lei 8.666/1993:  Convite, Tomada de Preços, Concorrência e Concurso. Recurso - Lei 10.520/2002 - Pregão: Pregão.';
COMMENT ON COLUMN siconv.licitacao.valor IS 'Valor total do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.data_publicacao IS 'Data de publicação do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.data_homologacao IS 'Data de homologação do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.data_realizacao_licitacao IS 'Data de realização do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.caput IS 'Caput referente ao fundamento legal.';
COMMENT ON COLUMN siconv.licitacao.srp IS 'Campo booleano se ''t'' informa que Pregão possui registro de preço.';
COMMENT ON COLUMN siconv.licitacao.cpf_homologador IS 'CPF do homologador do Processo de Execução.';
COMMENT ON COLUMN siconv.licitacao.adt_operacao IS 'Campo referente ao tipo de operação. Pode assumir os valores: ND (foi utilizado somente em 2008 e 2009 pela UFRJ - 14 registros), AE: se a inclusão do registro foi através de apuração especial, INSERCAO - inclusão do registro e ATUALIZACAO - atualização do registro (update). ';
COMMENT ON COLUMN siconv.licitacao.adt_data_hora IS 'Controle de data e hora da inserção ou atualização do registro, conforme hora do sistema.';
COMMENT ON COLUMN siconv.licitacao.adt_login IS 'CPF do usuário que executou a operação.';
COMMENT ON COLUMN siconv.licitacao.tipo_licitacao IS 'Tipo da Licitação: depende da modalidade.Modalidade Convite: Menor Valor.Modalidade Tomada de Preço: Menor Preço, Melhor Técnica, Técnica e Preço.Modalidade Concorrência:  Internacional, Menor Preço, Melhor Técnica e Técnica e Preço.Modalidade Pregão:  Eletrônico, Eletrônico para Registro de Preço, Eletrônico por Maior Desconto, Eletrônico por Lote (grupo de itens), Presencial, Presencial para Registro de Preço, Presencial por Maior Desconto.';
COMMENT ON COLUMN siconv.licitacao.processo_compra IS 'Processo de Compras: Dispensa de Licitação, Inexigibilidade, Licitação, Cotação Prévia, Pesquisa de Mercado';
COMMENT ON COLUMN siconv.licitacao.tipo_compra IS 'Tipo de Compra: "Material", "Serviço" ou "Material/Serviço"';
COMMENT ON COLUMN siconv.licitacao.numero_processo_cotacao IS 'Número do Processo da Cotação.';
COMMENT ON COLUMN siconv.licitacao.numero_cotacao IS 'Número da Cotação realizada para a licitação';
COMMENT ON COLUMN siconv.licitacao.data_inicio_cotacao IS 'Data de Início da Cotação realizada para a licitação';
COMMENT ON COLUMN siconv.licitacao.data_encerramento_cotacao IS 'Data de Encerramento da Cotação realizada para a licitação';
COMMENT ON COLUMN siconv.licitacao.valor_cotacao IS 'Valor da Cotação realizada para a licitação';
COMMENT ON COLUMN siconv.licitacao.cpf_homologador_cotacao IS 'CPF do Homologador da Cotação realizada para a licitação';
COMMENT ON COLUMN siconv.licitacao.numero_pesquisa_mercado IS 'Número da Pesquisa de Mercado';
COMMENT ON COLUMN siconv.licitacao.numero_licitacao IS 'Número da Licitação';
COMMENT ON COLUMN siconv.licitacao.data_encerramento_licitacao IS 'Data de encerramento da licitação.';
COMMENT ON COLUMN siconv.licitacao.numero_dispensa IS 'Número da dispensa de Licitação.';
COMMENT ON COLUMN siconv.licitacao.inciso_dispensa IS 'Inciso referente a dispensa de licitação.';
COMMENT ON COLUMN siconv.licitacao.data_solicitacao_dispensa IS 'Data da solicitação da dispensa delicitação.';
COMMENT ON COLUMN siconv.licitacao.valor_dispensa IS 'Valor da dispensa de licitação.';
COMMENT ON COLUMN siconv.licitacao.data_abertura_licitacao IS 'Data de abertura da licitação.';
COMMENT ON COLUMN siconv.licitacao.origem_recurso IS 'Origem do recurso depende do recurso financeiro:Recurso Financeiro Nacional: Lei 8.6666/1993, Lei 10.520/2002 - Pregão.Recurso Financeiro Internacional:  Lei 8.6666/1993, Lei 10.520/2002 - Pregão, Shopping, NCB (LPN), ICB(LPI).';
COMMENT ON COLUMN siconv.licitacao.recurso_financeiro IS 'Recurso Financeiro: Nacional ou Internacional. ';
COMMENT ON COLUMN siconv.licitacao.ano IS 'Ano referente a cotação prévia ou eletrônica.';
COMMENT ON COLUMN siconv.licitacao.numero_inexigbilidade IS 'Número de inexigibilidade do Processo de Compra.';
COMMENT ON COLUMN siconv.licitacao.status IS 'Status: "Concluído", "Em Elaboração".';
COMMENT ON COLUMN siconv.licitacao.tipo_processo_execucao IS 'Tipo de Processo de Execução: Processo de Compras, Subconvênio - Registro de Chamamento Público';
COMMENT ON COLUMN siconv.licitacao.incluida_por_executor IS 'Campo booleano se ''t'' o registro foi incluído pelo Executor.';
COMMENT ON COLUMN siconv.licitacao.ata_registro_precos_fk IS 'Identificador único da tabela anexo_licitacao.';
COMMENT ON COLUMN siconv.licitacao.sistema_origem_fk IS 'Chave do sistema de origem';
COMMENT ON COLUMN siconv.licitacao.situacao_licitacao_sc_fk IS 'Chave da situação do Sistema de Compras Externo';
COMMENT ON COLUMN siconv.licitacao.id_externo IS 'ID Externo';


CREATE TABLE siconv.fornecedores_vencedores_itens (
	itens_fk int8 NOT NULL, -- Identificador único da tabela it_licitacao.
	fornecedores_vencedores_fk int8 NOT NULL, -- Identificador único da tabela fornecedores.
	CONSTRAINT fornecedores_vencedores_itens_itens_fk_fornecedores_vencedo_key UNIQUE (itens_fk, fornecedores_vencedores_fk),
	CONSTRAINT fkc_fornecedores_itens FOREIGN KEY (itens_fk) REFERENCES siconv.it_licitacao(id),
	CONSTRAINT fkc_it_licitacao_fornecedoresv FOREIGN KEY (fornecedores_vencedores_fk) REFERENCES siconv.fornecedores(id)
);

COMMENT ON TABLE siconv.fornecedores_vencedores_itens IS 'Tabela intermediaria entre fornecedores vencedores e itens.';
COMMENT ON COLUMN siconv.fornecedores_vencedores_itens.itens_fk IS 'Identificador único da tabela it_licitacao.';
COMMENT ON COLUMN siconv.fornecedores_vencedores_itens.fornecedores_vencedores_fk IS 'Identificador único da tabela fornecedores.';
