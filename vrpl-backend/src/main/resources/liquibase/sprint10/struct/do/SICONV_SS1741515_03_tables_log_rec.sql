

-- Sequence: siconv.vrpl_pendencia_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_pendencia_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_pendencia_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_pendencia_log_rec

-- DROP TABLE siconv.vrpl_pendencia_log_rec;

CREATE TABLE siconv.vrpl_pendencia_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_pendencia_log_rec_seq'::regclass), -- Coluna de id.
  descricao character varying (1500) NOT NULL, 
  entity_id bigint NOT NULL, 
  in_resolvida boolean NOT NULL, 
  laudolog bigint NOT NULL, 
  prazo character varying (5) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_pendencia_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_pendencia_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_pendencia_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_pendencia_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_anexo_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_anexo_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_anexo_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_anexo_log_rec

-- DROP TABLE siconv.vrpl_anexo_log_rec;

CREATE TABLE siconv.vrpl_anexo_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_anexo_log_rec_seq'::regclass), -- Coluna de id.
  bucket character varying (25) NOT NULL, 
  caminho character varying (1024) NOT NULL, 
  dt_upload date NOT NULL, 
  entity_id bigint NOT NULL, 
  id_cpf_enviadopor character varying (11) NOT NULL, 
  in_perfil character varying (10) NOT NULL, 
  in_tipoanexo character varying (30) NOT NULL, 
  nm_arquivo character varying (100) NOT NULL, 
  nome_enviadopor character varying (60) NOT NULL, 
  tx_descricao character varying (30) NOT NULL, 
  vrpllicitacaolog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_anexo_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_anexo_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_anexo_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_anexo_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_evento_frente_obra_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_evento_frente_obra_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_evento_frente_obra_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_evento_frente_obra_log_rec

-- DROP TABLE siconv.vrpl_evento_frente_obra_log_rec;

CREATE TABLE siconv.vrpl_evento_frente_obra_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_evento_frente_obra_log_rec_seq'::regclass), -- Coluna de id.
  eventolog bigint NOT NULL, 
  frenteobralog bigint NOT NULL, 
  nr_mes_conclusao smallint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_evento_frente_obra_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_evento_frente_obra_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_evento_frente_obra_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_proposta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_proposta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_proposta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_proposta_log_rec

-- DROP TABLE siconv.vrpl_proposta_log_rec;

CREATE TABLE siconv.vrpl_proposta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_proposta_log_rec_seq'::regclass), -- Coluna de id.
  ano_convenio bigint, 
  ano_proposta bigint NOT NULL, 
  categoria character varying (50) NOT NULL, 
  codigo_programa character varying (13) NOT NULL, 
  data_assinatura_convenio date, 
  entity_id bigint NOT NULL, 
  id_siconv bigint NOT NULL, 
  identificacao_proponente character varying (1024), 
  modalidade bigint NOT NULL, 
  nivel_contrato character varying (20), 
  nome_mandataria character varying (1024), 
  nome_objeto character varying (5000), 
  nome_programa character varying (255) NOT NULL, 
  nome_proponente character varying (1024), 
  numero_convenio bigint, 
  numero_proposta bigint NOT NULL, 
  pc_min_contrapartida NUMERIC(3,2), 
  spa_homologado boolean NOT NULL, 
  uf character varying (2) NOT NULL, 
  valor_contrapartida NUMERIC(17,2) NOT NULL, 
  valor_global NUMERIC(17,2) NOT NULL, 
  valor_repasse NUMERIC(17,2) NOT NULL, 
  versao_in_atual boolean NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_proposta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_proposta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_proposta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_proposta_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_template_laudo_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_template_laudo_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_template_laudo_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_template_laudo_log_rec

-- DROP TABLE siconv.vrpl_template_laudo_log_rec;

CREATE TABLE siconv.vrpl_template_laudo_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_template_laudo_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  tipo character varying (10), 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_template_laudo_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_template_laudo_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_template_laudo_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_template_laudo_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_subitem_investimento_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_subitem_investimento_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_subitem_investimento_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_subitem_investimento_log_rec

-- DROP TABLE siconv.vrpl_subitem_investimento_log_rec;

CREATE TABLE siconv.vrpl_subitem_investimento_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_subitem_investimento_log_rec_seq'::regclass), -- Coluna de id.
  codigo_und character varying (1024), 
  descricao character varying (1024) NOT NULL, 
  descricao_und character varying (1024), 
  entity_id bigint NOT NULL, 
  id_subitem_analise bigint NOT NULL, 
  in_projeto_social character varying (4), 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_subitem_investimento_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_subitem_investimento_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_subitem_investimento_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_laudo_grupo_pergunta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_laudo_grupo_pergunta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_laudo_grupo_pergunta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_laudo_grupo_pergunta_log_rec

-- DROP TABLE siconv.vrpl_laudo_grupo_pergunta_log_rec;

CREATE TABLE siconv.vrpl_laudo_grupo_pergunta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_laudo_grupo_pergunta_log_rec_seq'::regclass), -- Coluna de id.
  grupoperguntalog bigint NOT NULL, 
  entity_id bigint NOT NULL, 
  in_nao_aplicavel boolean, 
  laudolog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_laudo_grupo_pergunta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_laudo_grupo_pergunta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_laudo_grupo_pergunta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_licitacao_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_licitacao_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_licitacao_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_licitacao_log_rec

-- DROP TABLE siconv.vrpl_licitacao_log_rec;

CREATE TABLE siconv.vrpl_licitacao_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_licitacao_log_rec_seq'::regclass), -- Coluna de id.
  data_homologacao date, 
  data_publicacao date, 
  entity_id bigint NOT NULL, 
  id_licitacao_fk bigint NOT NULL, 
  in_situacao character varying (3) NOT NULL, 
  modalidade character varying (50), 
  numero_ano character varying (1024) NOT NULL, 
  objeto character varying (1024) NOT NULL, 
  processo_de_execucao character varying (50), 
  propostalog bigint NOT NULL, 
  regime_contratacao character varying (1024), 
  status_processo character varying (20) NOT NULL, 
  valor_processo NUMERIC(17,2) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_licitacao_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_licitacao_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_licitacao_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_licitacao_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_servico_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_servico_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_servico_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_servico_log_rec

-- DROP TABLE siconv.vrpl_servico_log_rec;

CREATE TABLE siconv.vrpl_servico_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_servico_log_rec_seq'::regclass), -- Coluna de id.
  cd_servico character varying (13) NOT NULL, 
  eventolog bigint, 
  entity_id bigint NOT NULL, 
  id_servico_analise bigint NOT NULL, 
  in_fonte character varying (3) NOT NULL, 
  macroservicolog bigint NOT NULL, 
  nr_servico smallint NOT NULL, 
  pc_bdi_analise NUMERIC(17,2) NOT NULL, 
  pc_bdi_licitado NUMERIC(17,2) NOT NULL, 
  qt_total_itens_analise NUMERIC(17,2) NOT NULL, 
  sg_unidade character varying (10) NOT NULL, 
  tx_descricao character varying (508) NOT NULL, 
  tx_observacao character varying (500), 
  vl_custo_unitario_analise NUMERIC(17,2) NOT NULL, 
  vl_custo_unitario_ref_analise NUMERIC(17,2) NOT NULL, 
  vl_preco_total_analise NUMERIC(17,2) NOT NULL, 
  vl_preco_unitario_analise NUMERIC(17,2) NOT NULL, 
  vl_preco_unitario_licitado NUMERIC(17,2) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_servico_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_servico_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_servico_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_servico_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_grupo_pergunta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_grupo_pergunta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_grupo_pergunta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_grupo_pergunta_log_rec

-- DROP TABLE siconv.vrpl_grupo_pergunta_log_rec;

CREATE TABLE siconv.vrpl_grupo_pergunta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_grupo_pergunta_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  in_grupo_obrigatorio boolean NOT NULL, 
  numero bigint NOT NULL, 
  templatelog bigint NOT NULL, 
  titulo character varying (500) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_grupo_pergunta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_grupo_pergunta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_grupo_pergunta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta_log_rec.id IS 'Coluna de id.';



CREATE SEQUENCE siconv.vrpl_servico_frente_obra_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


-- Table: siconv.vrpl_servico_frente_obra_log_rec

-- DROP TABLE siconv.vrpl_servico_frente_obra_log_rec;

CREATE TABLE siconv.vrpl_servico_frente_obra_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_servico_frente_obra_log_rec_seq'::regclass), -- Coluna de id.
  frenteobralog bigint NOT NULL, 
  qt_itens NUMERIC(17,2) NOT NULL, 
  qt_itens_analise NUMERIC(17,2) NOT NULL, 
  servicolog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_servico_frente_obra_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_servico_frente_obra_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_servico_frente_obra_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_lote_licitacao_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_lote_licitacao_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_lote_licitacao_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_lote_licitacao_log_rec

-- DROP TABLE siconv.vrpl_lote_licitacao_log_rec;

CREATE TABLE siconv.vrpl_lote_licitacao_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_lote_licitacao_log_rec_seq'::regclass), -- Coluna de id.
  fornecedorlog bigint, 
  entity_id bigint NOT NULL, 
  licitacaolog bigint, 
  numero_lote smallint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_lote_licitacao_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_lote_licitacao_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_lote_licitacao_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_lote_licitacao_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_submeta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_submeta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_submeta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_submeta_log_rec

-- DROP TABLE siconv.vrpl_submeta_log_rec;

CREATE TABLE siconv.vrpl_submeta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_submeta_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  id_submeta_analise bigint NOT NULL, 
  in_regime_execucao character varying (3) NOT NULL, 
  in_regime_execucao_analise character varying (3), 
  in_situacao character varying (3) NOT NULL, 
  in_situacao_analise character varying (3) NOT NULL, 
  metalog bigint NOT NULL, 
  natureza_despesa_sub_it_fk_analise bigint NOT NULL, 
  nr_lote_analise smallint NOT NULL, 
  nr_submeta_analise bigint NOT NULL, 
  propostalog bigint NOT NULL, 
  tx_descricao_analise character varying (100) NOT NULL, 
  vl_contrapartida NUMERIC(17,2) NOT NULL, 
  vl_contrapartida_analise NUMERIC(17,2) NOT NULL, 
  vl_outros NUMERIC(17,2) NOT NULL, 
  vl_outros_analise NUMERIC(17,2), 
  vl_repasse NUMERIC(17,2) NOT NULL, 
  vl_repasse_analise NUMERIC(17,2) NOT NULL, 
  vl_total_analise NUMERIC(17,2) NOT NULL, 
  vl_total_licitado NUMERIC(17,2) NOT NULL, 
  vrpllotelicitacaolog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_submeta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_submeta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_submeta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_submeta_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_macro_servico_parcela_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_macro_servico_parcela_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_macro_servico_parcela_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_macro_servico_parcela_log_rec

-- DROP TABLE siconv.vrpl_macro_servico_parcela_log_rec;

CREATE TABLE siconv.vrpl_macro_servico_parcela_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_macro_servico_parcela_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  macroservicolog bigint NOT NULL, 
  nr_parcela smallint NOT NULL, 
  pc_parcela NUMERIC(5,2) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_macro_servico_parcela_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_macro_servico_parcela_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_macro_servico_parcela_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_macro_servico_parcela_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_laudo_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_laudo_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_laudo_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_laudo_log_rec

-- DROP TABLE siconv.vrpl_laudo_log_rec;

CREATE TABLE siconv.vrpl_laudo_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_laudo_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  in_resultado character varying (2), 
  in_status bigint NOT NULL, 
  licitacaolog bigint NOT NULL, 
  templatelog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_laudo_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_laudo_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_laudo_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_laudo_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_macro_servico_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_macro_servico_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_macro_servico_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_macro_servico_log_rec

-- DROP TABLE siconv.vrpl_macro_servico_log_rec;

CREATE TABLE siconv.vrpl_macro_servico_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_macro_servico_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  id_macro_servico_analise bigint NOT NULL, 
  nr_macro_servico smallint NOT NULL, 
  polog bigint NOT NULL, 
  tx_descricao character varying (100) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_macro_servico_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_macro_servico_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_macro_servico_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_macro_servico_log_rec.id IS 'Coluna de id.';



-- Sequence: siconv.vrpl_po_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_po_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_po_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_po_log_rec

-- DROP TABLE siconv.vrpl_po_log_rec;

CREATE TABLE siconv.vrpl_po_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_po_log_rec_seq'::regclass), -- Coluna de id.
  dt_base_analise date NOT NULL, 
  dt_base_vrpl date, 
  dt_previsao_inicio_obra date NOT NULL, 
  entity_id bigint NOT NULL, 
  id_po_analise bigint NOT NULL, 
  in_acompanhamento_eventos boolean, 
  in_desonerado boolean, 
  qt_meses_duracao_obra smallint NOT NULL, 
  referencia character varying (8), 
  sg_localidade character varying (2), 
  submetalog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_po_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_po_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_po_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_po_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_fornecedor_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_fornecedor_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_fornecedor_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_fornecedor_log_rec

-- DROP TABLE siconv.vrpl_fornecedor_log_rec;

CREATE TABLE siconv.vrpl_fornecedor_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_fornecedor_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  identificacao character varying (20), 
  licitacaolog bigint NOT NULL, 
  razao_social character varying (150), 
  tipo_identificacao character varying (4), 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_fornecedor_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_fornecedor_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_fornecedor_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_fornecedor_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_pergunta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_pergunta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_pergunta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_pergunta_log_rec

-- DROP TABLE siconv.vrpl_pergunta_log_rec;

CREATE TABLE siconv.vrpl_pergunta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_pergunta_log_rec_seq'::regclass), -- Coluna de id.
  grupolog bigint NOT NULL, 
  entity_id bigint NOT NULL, 
  numero bigint NOT NULL, 
  tipo_resposta character varying (10) NOT NULL, 
  titulo character varying (500) NOT NULL, 
  valor_esperado character varying (50) NOT NULL, 
  valor_resposta character varying (100) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_pergunta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_pergunta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_pergunta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_pergunta_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_frente_obra_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_frente_obra_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_frente_obra_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_frente_obra_log_rec

-- DROP TABLE siconv.vrpl_frente_obra_log_rec;

CREATE TABLE siconv.vrpl_frente_obra_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_frente_obra_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  nm_frente_obra character varying (100) NOT NULL, 
  nr_frente_obra smallint NOT NULL, 
  polog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_frente_obra_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_frente_obra_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_frente_obra_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_frente_obra_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_meta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_meta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_meta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_meta_log_rec

-- DROP TABLE siconv.vrpl_meta_log_rec;

CREATE TABLE siconv.vrpl_meta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_meta_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  id_meta_analise bigint NOT NULL, 
  in_social boolean NOT NULL, 
  nr_meta_analise bigint NOT NULL, 
  qt_itens_analise NUMERIC(17,2) NOT NULL, 
  subitemlog bigint, 
  tx_descricao_analise character varying (100) NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_meta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_meta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_meta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_meta_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_resposta_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_resposta_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_resposta_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_resposta_log_rec

-- DROP TABLE siconv.vrpl_resposta_log_rec;

CREATE TABLE siconv.vrpl_resposta_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_resposta_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  laudolog bigint NOT NULL, 
  perguntalog bigint NOT NULL, 
  resposta character varying (500), 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_resposta_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_resposta_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_resposta_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_resposta_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_historico_licitacao_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_historico_licitacao_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_historico_licitacao_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_historico_licitacao_log_rec

-- DROP TABLE siconv.vrpl_historico_licitacao_log_rec;

CREATE TABLE siconv.vrpl_historico_licitacao_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_historico_licitacao_log_rec_seq'::regclass), -- Coluna de id.
  dt_registro date NOT NULL, 
  entity_id bigint NOT NULL, 
  in_evento character varying (3) NOT NULL, 
  in_situacao character varying (3) NOT NULL, 
  licitacaolog bigint NOT NULL, 
  nm_responsavel character varying (60) NOT NULL, 
  nr_cpf_responsavel character varying (11) NOT NULL, 
  tx_consideracoes text, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_historico_licitacao_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_historico_licitacao_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_historico_licitacao_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao_log_rec.id IS 'Coluna de id.';


-- Sequence: siconv.vrpl_evento_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_evento_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_evento_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;




-- Table: siconv.vrpl_evento_log_rec

-- DROP TABLE siconv.vrpl_evento_log_rec;

CREATE TABLE siconv.vrpl_evento_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_evento_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  nm_evento character varying (100) NOT NULL, 
  nr_evento smallint NOT NULL, 
  polog bigint NOT NULL, 
  versao bigint,
  versao_nr integer NOT NULL DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_evento_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_evento_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_evento_log_rec
  IS 'Tabela que representa ????';
COMMENT ON COLUMN siconv.vrpl_evento_log_rec.id IS 'Coluna de id.';
