

CREATE SEQUENCE siconv.vrpl_proposta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siconv.vrpl_proposta (
    id bigint DEFAULT nextval('siconv.vrpl_proposta_seq'::regclass) PRIMARY KEY,
    id_siconv bigint NOT NULL,
    numero_proposta int4 NOT NULL,
    ano_proposta int4 NOT NULL,
    valor_global numeric(17,2) NOT NULL,
    valor_repasse numeric(17,2) NOT NULL,
    valor_contrapartida numeric(17,2) NOT NULL,
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
    pc_min_contrapartida numeric(3,2) NULL,
    nome_mandataria varchar(1024) NULL,
    categoria varchar(50) NOT NULL,
    nivel_contrato varchar(20) NULL,
    spa_homologado boolean DEFAULT true NOT NULL,
    adt_login varchar(60)  NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_in_atual boolean DEFAULT true NOT NULL,

    CONSTRAINT ck_vrpl_proposta_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))

);

COMMENT ON TABLE siconv.vrpl_proposta IS 'Tabela com informações da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.id  IS 'Identificador da Proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.id_siconv IS 'Identificador da Proposta no SICONV';
COMMENT ON COLUMN siconv.vrpl_proposta.numero_proposta IS 'Número da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.ano_proposta IS 'Ano da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.valor_global IS 'Valor global aceito na análise';
COMMENT ON COLUMN siconv.vrpl_proposta.valor_repasse IS 'Valor de repasse aceito na análise';
COMMENT ON COLUMN siconv.vrpl_proposta.valor_contrapartida IS 'Valor de contrapartida';
COMMENT ON COLUMN siconv.vrpl_proposta.modalidade IS 'Modalidade da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.nome_objeto IS 'Descrição do objeto da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.numero_convenio IS 'Número do convênio associado a proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.ano_convenio IS 'Ano do convênio associado à proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.data_assinatura_convenio IS 'Data de assinatura do convênio';
COMMENT ON COLUMN siconv.vrpl_proposta.codigo_programa IS 'Código do programa ao qual a proposta foi submetida';
COMMENT ON COLUMN siconv.vrpl_proposta.nome_programa IS 'Nome do programa ao qual a proposta foi submetida';
COMMENT ON COLUMN siconv.vrpl_proposta.identificacao_proponente IS 'Proponente da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.nome_proponente IS 'Nome do proponente da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.pc_min_contrapartida IS 'Percentual mínimo de contrapartida';
COMMENT ON COLUMN siconv.vrpl_proposta.nome_mandataria IS 'Mandatária responsável';
COMMENT ON COLUMN siconv.vrpl_proposta.categoria IS 'Categoria da proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.nivel_contrato IS 'Nível do contrato';
COMMENT ON COLUMN siconv.vrpl_proposta.uf IS 'Estado do Objeto da Proposta';
COMMENT ON COLUMN siconv.vrpl_proposta.spa_homologado IS 'Status do SPA';
COMMENT ON COLUMN siconv.vrpl_proposta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_proposta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_proposta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_proposta.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_proposta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_proposta.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_proposta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_proposta.versao_in_atual IS 'Indicador da última versão do VRPL';


CREATE SEQUENCE siconv.vrpl_anexo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_anexo (
    id bigint DEFAULT nextval('siconv.vrpl_anexo_seq'::regclass) NOT NULL,
    vrpl_licitacao_fk bigint NOT NULL,
    nm_arquivo varchar(100) NOT NULL,
    caminho varchar(1024) NOT NULL,
    dt_upload date NOT NULL,
    tx_descricao varchar(30) NOT NULL,
    in_tipoanexo varchar(20),
    id_cpf_enviadopor varchar(11) NOT NULL,
    nome_enviadopor varchar(1024) NOT NULL,
    in_perfil varchar(10),
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    CONSTRAINT ck_vrpl_anexo_in_perfil CHECK (((in_perfil)::text = ANY (ARRAY[('PROPONENTE'::varchar)::text, ('CONCEDENTE'::varchar)::text, ('MANDATARIA'::varchar)::text]))),
    CONSTRAINT ck_vrpl_anexo_in_tipoanexo CHECK (((in_tipoanexo)::text = ANY (ARRAY[('EXTRATO_CTEF'::varchar)::text, ('PLE'::varchar)::text, ('DEC_REGIME_EXEC'::varchar)::text, ('DEC_EMP_VENCEDORA'::varchar)::text, ('PUB_RESUMO_EDITAL'::varchar)::text, ('ATO_HOMOLOGACAO_LIC'::varchar)::text, ('DESPACHO_ADJUDIACAO'::varchar)::text, ('OUTROS'::varchar)::text, ('VRPL'::varchar)::text, ('VRPLS'::varchar)::text, ('QUADRO_RESUMO'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_anexo IS 'Tabela que representa os Anexos do vrpl';
COMMENT ON COLUMN siconv.vrpl_anexo.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_anexo.vrpl_licitacao_fk IS 'Chave estrangeira para a Licitacao';
COMMENT ON COLUMN siconv.vrpl_anexo.nm_arquivo IS 'Nome do arquivo';
COMMENT ON COLUMN siconv.vrpl_anexo.caminho IS 'Caminho do arquivo';
COMMENT ON COLUMN siconv.vrpl_anexo.dt_upload IS 'Data de upload do arquivo';
COMMENT ON COLUMN siconv.vrpl_anexo.tx_descricao IS 'Descricao do arquivo';
COMMENT ON COLUMN siconv.vrpl_anexo.in_tipoanexo IS 'Tipo do anexo';
COMMENT ON COLUMN siconv.vrpl_anexo.id_cpf_enviadopor IS 'CPF do usuario que enviou o anexo';
COMMENT ON COLUMN siconv.vrpl_anexo.nome_enviadopor IS 'Nome do usuario que enviou o anexo';
COMMENT ON COLUMN siconv.vrpl_anexo.in_perfil IS 'Perfil do usuario que enviou o anexo';



CREATE SEQUENCE siconv.vrpl_evento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_evento (
    id bigint DEFAULT nextval('siconv.vrpl_evento_seq'::regclass) NOT NULL,
    nm_evento varchar(100) NOT NULL,
    po_fk bigint NOT NULL,
    nr_evento smallint NOT NULL,
    adt_login varchar(60)  NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao_nr integer DEFAULT 0 NOT NULL,

    CONSTRAINT ck_vrpl_evento_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_evento IS 'Tabela que representa os Eventos';
COMMENT ON COLUMN siconv.vrpl_evento.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_evento.nm_evento IS 'Nome do evento';
COMMENT ON COLUMN siconv.vrpl_evento.po_fk IS 'Chave estrangeira para PO';
COMMENT ON COLUMN siconv.vrpl_evento.nr_evento IS 'Numero do evento';
COMMENT ON COLUMN siconv.vrpl_evento.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_evento.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_evento.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_evento.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_evento.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_evento.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_evento.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';


CREATE TABLE siconv.vrpl_evento_frente_obra (
    evento_fk bigint NOT NULL,
    frente_obra_fk bigint NOT NULL,
    nr_mes_conclusao smallint NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar(60) not null,
    adt_data_hora timestamp without time zone not null,
    adt_operacao varchar(6) not null,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    CONSTRAINT ck_vrpl_evento_frente_obra_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_evento_frente_obra IS 'Tabela que representa o relacionamento NxM entre Evento e Frente Obra';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.evento_fk IS 'Chave estrangeira para Evento';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.frente_obra_fk IS 'Chave estrangeira para Frente Obra';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.nr_mes_conclusao IS 'Mes de conclusao';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_evento_frente_obra.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';






CREATE SEQUENCE siconv.vrpl_frente_obra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE TABLE siconv.vrpl_frente_obra (
    id bigint DEFAULT nextval('siconv.vrpl_frente_obra_seq'::regclass) NOT NULL,
    nm_frente_obra varchar(100) NOT NULL,
    po_fk bigint NOT NULL,
    nr_frente_obra smallint NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar(60),
    adt_data_hora timestamp without time zone,
    adt_operacao varchar(6),
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    CONSTRAINT ck_vrpl_frente_obra_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_frente_obra IS 'Tabela que representa a Frente Obra';
COMMENT ON COLUMN siconv.vrpl_frente_obra.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_frente_obra.nm_frente_obra IS 'Nome da Frente Obra';
COMMENT ON COLUMN siconv.vrpl_frente_obra.po_fk IS 'Chave estrangeira para PO';
COMMENT ON COLUMN siconv.vrpl_frente_obra.nr_frente_obra IS 'Numero da Frente Obra';
COMMENT ON COLUMN siconv.vrpl_frente_obra.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_frente_obra.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_frente_obra.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_frente_obra.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_frente_obra.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_frente_obra.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_frente_obra.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';




CREATE SEQUENCE siconv.vrpl_grupo_pergunta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_grupo_pergunta (
    id bigint DEFAULT nextval('siconv.vrpl_grupo_pergunta_seq'::regclass) NOT NULL,
    numero integer DEFAULT 1 NOT NULL,
    titulo varchar(500) NOT NULL,
    in_grupo_obrigatorio boolean DEFAULT false NOT NULL,
    template_fk bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20) NOT NULL,
    versao_nm_evento varchar(3) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar(60) NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    CONSTRAINT ck_vrpl_grupo_pergunta_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_grupo_pergunta IS 'Tabela que representa os Grupos de Pergunta no VRPL';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.id IS 'Sequencial da tabela';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.numero IS 'Numero do grupo de pergunta';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.titulo IS 'Titulo do grupo de pergunta';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.in_grupo_obrigatorio IS 'Indicador de obrigatoriedade do grupo de pergunta';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.template_fk IS 'Chave estrangeira para a tabela de template de laudo';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_grupo_pergunta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_historico_licitacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE TABLE siconv.vrpl_historico_licitacao (
    id bigint DEFAULT nextval('siconv.vrpl_historico_licitacao_seq'::regclass) NOT NULL,
    licitacao_fk bigint NOT NULL,
    in_evento varchar(3) NOT NULL,
    in_situacao varchar(3) NOT NULL,
    tx_consideracoes text,
    dt_registro timestamp without time zone NOT NULL,
    nm_responsavel varchar(60) NOT NULL,
    nr_cpf_responsavel varchar(11) NOT NULL,
    adt_login varchar(60) NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
	CONSTRAINT ck_vrpl_historico_licitacao_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_historico_licitacao IS 'Tabela que representa o histórico da licitação';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.id IS 'Identificador do historico da licitação na base de dados';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.licitacao_fk IS 'Chave da licitacao a qual o historico está associado';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.in_evento IS 'Indicador do Evento gerador do historico';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.in_situacao IS 'Sigla da Situação da documentacao';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.tx_consideracoes IS 'Texto com as consideracoes';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.dt_registro IS 'Data de registro';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.nm_responsavel IS 'Nome do responsavel';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.nr_cpf_responsavel IS 'CPF do responsavel';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_historico_licitacao.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';




CREATE SEQUENCE siconv.vrpl_laudo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_laudo (
    id bigint DEFAULT nextval('siconv.vrpl_laudo_seq'::regclass) NOT NULL,
    licitacao_fk bigint NOT NULL,
    template_fk bigint NOT NULL,
    in_status bigint NOT NULL,
    in_resultado varchar(2),
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar(60) NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    CONSTRAINT ck_vrpl_laudo_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON TABLE siconv.vrpl_laudo IS 'Tabela que representa os Laudos no VRPL';
COMMENT ON COLUMN siconv.vrpl_laudo.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_laudo.licitacao_fk IS 'Chave estrangeira para a tabela de Licitacao';
COMMENT ON COLUMN siconv.vrpl_laudo.template_fk IS 'Chave estrangeira para a tabela de Template de Laudo';
COMMENT ON COLUMN siconv.vrpl_laudo.in_status IS 'Indicador do status do Laudo: 1 - Rascunho, 2 - Emitido';
COMMENT ON COLUMN siconv.vrpl_laudo.in_resultado IS 'Indicador do resultado do laudo';
COMMENT ON COLUMN siconv.vrpl_laudo.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_laudo.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_laudo.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_laudo.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_laudo.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_laudo.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_laudo.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';



CREATE SEQUENCE siconv.vrpl_laudo_grupo_pergunta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE TABLE siconv.vrpl_laudo_grupo_pergunta (
    id bigint DEFAULT nextval('siconv.vrpl_laudo_grupo_pergunta_seq'::regclass) NOT NULL,
    in_nao_aplicavel boolean,
    grupo_pergunta_fk bigint NOT NULL,
    laudo_fk bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar
);

COMMENT ON TABLE siconv.vrpl_laudo_grupo_pergunta IS 'Relação entre Resposta e Grupo Pergunta';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.in_nao_aplicavel IS 'Determina se a Resposta do Grupo Pergunta é aplicável';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.grupo_pergunta_fk IS 'FK para Grupo Pergunta';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.laudo_fk IS 'FK para Laudo';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_laudo_grupo_pergunta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_licitacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_licitacao (
    id bigint DEFAULT nextval('siconv.vrpl_licitacao_seq'::regclass) NOT NULL,
    proposta_fk bigint,
    numero_ano varchar(1024),
    objeto varchar(1024) NOT NULL,
    valor_processo numeric(17,2),
    status_processo varchar(20),
    id_licitacao_fk bigint NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    in_situacao varchar(3) NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL
);

COMMENT ON TABLE siconv.vrpl_licitacao IS 'Tabela que representa a licitação';
COMMENT ON COLUMN siconv.vrpl_licitacao.id IS 'Identificador da licitação na base de dados';
COMMENT ON COLUMN siconv.vrpl_licitacao.proposta_fk IS 'Chave da proposta a qual a licitação está associada';
COMMENT ON COLUMN siconv.vrpl_licitacao.numero_ano IS 'Identificador numero/ano da licitação';
COMMENT ON COLUMN siconv.vrpl_licitacao.objeto IS 'Objeto da Licitação';
COMMENT ON COLUMN siconv.vrpl_licitacao.valor_processo IS 'Valor em reais do processo licitatório';
COMMENT ON COLUMN siconv.vrpl_licitacao.status_processo IS 'Status do processo licitatório: Em licitação ou Concluído';
COMMENT ON COLUMN siconv.vrpl_licitacao.id_licitacao_fk IS 'Chave estrangeira da licitação obtida do serviço de licitações';
COMMENT ON COLUMN siconv.vrpl_licitacao.in_situacao IS 'Sigla da Situação da documentacao';

CREATE SEQUENCE siconv.vrpl_fornecedor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siconv.vrpl_fornecedor (
    id bigint DEFAULT nextval('siconv.vrpl_fornecedor_seq'::regclass) PRIMARY KEY,
    licitacao_fk bigint NOT NULL,
    razao_social varchar(150),
    tipo_identificacao varchar(4),
    identificacao varchar(20),
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar
    CONSTRAINT vrpl_fornecedor_tipo_identificacao CHECK (((tipo_identificacao)::text = ANY ((ARRAY['CPF'::varchar, 'CNPJ'::varchar])::text[])))
);

COMMENT ON TABLE siconv.vrpl_fornecedor IS 'Tabela de fornecedores da licitação';
COMMENT ON COLUMN siconv.vrpl_fornecedor.id IS 'Identificador do forcenedor na base de dados';
COMMENT ON COLUMN siconv.vrpl_fornecedor.licitacao_fk IS 'Identificador da licitacao';
COMMENT ON COLUMN siconv.vrpl_fornecedor.razao_social IS 'Razao social do fornecedor';
COMMENT ON COLUMN siconv.vrpl_fornecedor.tipo_identificacao IS 'Informa se a identificação é CPF ou CNPJ';
COMMENT ON COLUMN siconv.vrpl_fornecedor.identificacao IS 'CPF ou CNPJ do fornecedor';
COMMENT ON COLUMN siconv.vrpl_fornecedor.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_fornecedor.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_fornecedor.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_fornecedor.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_fornecedor.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_fornecedor.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_fornecedor.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_lote_licitacao_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_lote_licitacao (
    id bigint DEFAULT nextval('siconv.vrpl_lote_licitacao_seq'::regclass) NOT NULL,
    licitacao_fk bigint,
    fornecedor_fk bigint,
    numero_lote smallint,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL
);


COMMENT ON TABLE siconv.vrpl_lote_licitacao IS 'Tabela que representa a associação de lotes com uma licitação';
COMMENT ON COLUMN siconv.vrpl_lote_licitacao.id IS 'Identificador da associação na base de dados';
COMMENT ON COLUMN siconv.vrpl_lote_licitacao.licitacao_fk IS 'Chave da licitação a qual o lote está associado';
COMMENT ON COLUMN siconv.vrpl_lote_licitacao.numero_lote IS 'Número do lote associado';


CREATE SEQUENCE siconv.vrpl_macro_servico_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_macro_servico (
    id bigint DEFAULT nextval('siconv.vrpl_macro_servico_seq'::regclass) NOT NULL,
    tx_descricao varchar(100) NOT NULL,
    po_fk bigint NOT NULL,
    nr_macro_servico smallint NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    id_macro_servico_analise bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);


COMMENT ON TABLE siconv.vrpl_macro_servico IS 'Tabela que representa os Macro Servicos';
COMMENT ON COLUMN siconv.vrpl_macro_servico.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_macro_servico.tx_descricao IS 'Descricao do Macro Servico';
COMMENT ON COLUMN siconv.vrpl_macro_servico.po_fk IS 'Chave estrangeira para PO';
COMMENT ON COLUMN siconv.vrpl_macro_servico.nr_macro_servico IS 'Número do Macro Servico';
COMMENT ON COLUMN siconv.vrpl_macro_servico.id_macro_servico_analise IS 'Id de análise';



CREATE SEQUENCE siconv.vrpl_macro_servico_parcela_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_macro_servico_parcela (
    id bigint DEFAULT nextval('siconv.vrpl_macro_servico_parcela_seq'::regclass) NOT NULL,
    nr_parcela smallint NOT NULL,
    pc_parcela numeric(5,2) NOT NULL,
    macro_servico_fk bigint NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);

COMMENT ON TABLE siconv.vrpl_macro_servico_parcela IS 'Tabela que representa o relacionamento NxM de Macro Servico e Parcela';
COMMENT ON COLUMN siconv.vrpl_macro_servico_parcela.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_macro_servico_parcela.nr_parcela IS 'Numero da parcela';
COMMENT ON COLUMN siconv.vrpl_macro_servico_parcela.pc_parcela IS 'Percentual da parcela';
COMMENT ON COLUMN siconv.vrpl_macro_servico_parcela.macro_servico_fk IS 'Chave estrangeira para Macro Servico';


CREATE SEQUENCE siconv.vrpl_meta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_meta (
    id bigint DEFAULT nextval('siconv.vrpl_meta_seq'::regclass) NOT NULL,
    id_meta_analise bigint NOT NULL,
    tx_descricao_analise varchar(100) NOT NULL,
    nr_meta_analise integer NOT NULL,
    qt_itens_analise numeric(17,2) NOT NULL,
    in_social boolean DEFAULT false NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    subitem_fk bigint,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL
);

COMMENT ON TABLE siconv.vrpl_meta IS 'Tabela que representa as Metas do VRPL';
COMMENT ON COLUMN siconv.vrpl_meta.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_meta.id_meta_analise IS 'Coluna de id da meta da fase de analise';
COMMENT ON COLUMN siconv.vrpl_meta.subitem_fk IS 'Referência para subitem investimento';


CREATE SEQUENCE siconv.vrpl_pendencia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_pendencia (
    id bigint DEFAULT nextval('siconv.vrpl_pendencia_seq'::regclass) NOT NULL,
    in_resolvida boolean DEFAULT false NOT NULL,
    descricao varchar(1500) NOT NULL,
    prazo varchar(5) NOT NULL,
    laudo_fk bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    CONSTRAINT vrpl_pendencia_prazo CHECK (((prazo)::text = ANY ((ARRAY['RTS'::varchar, 'CTR'::varchar, 'AIO'::varchar, 'PRM'::varchar, 'PNU'::varchar, 'ULT'::varchar, 'PCF'::varchar])::text[])))
);

COMMENT ON TABLE siconv.vrpl_pendencia IS 'Tabela que representa as Pendências';
COMMENT ON COLUMN siconv.vrpl_pendencia.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_pendencia.in_resolvida IS 'Determina se a pendência foi resolvida';
COMMENT ON COLUMN siconv.vrpl_pendencia.descricao IS 'Descrição da Pendência';
COMMENT ON COLUMN siconv.vrpl_pendencia.prazo IS 'Prazo para correção';
COMMENT ON COLUMN siconv.vrpl_pendencia.laudo_fk IS 'FK para o Laudo a que a Pendência se Refere';
COMMENT ON COLUMN siconv.vrpl_pendencia.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_pendencia.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_pendencia.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_pendencia.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_pendencia.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_pendencia.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_pendencia.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_pergunta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_pergunta (
    id bigint DEFAULT nextval('siconv.vrpl_pergunta_seq'::regclass) NOT NULL,
    numero integer DEFAULT 1 NOT NULL,
    titulo varchar(500) NOT NULL,
    tipo_resposta varchar(10) NOT NULL,
    valor_resposta varchar(100) NOT NULL,
    valor_esperado varchar(50) DEFAULT ''::varchar NOT NULL,
    grupo_fk bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar
);


COMMENT ON TABLE siconv.vrpl_pergunta IS 'Tabela que representa as Perguntas';
COMMENT ON COLUMN siconv.vrpl_pergunta.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_pergunta.numero IS 'Número da pergunta';
COMMENT ON COLUMN siconv.vrpl_pergunta.titulo IS 'Título da pergunta';
COMMENT ON COLUMN siconv.vrpl_pergunta.tipo_resposta IS 'Tipo de resposta da Pergunta';
COMMENT ON COLUMN siconv.vrpl_pergunta.valor_resposta IS 'Valor da resposta da Pergunta';
COMMENT ON COLUMN siconv.vrpl_pergunta.valor_esperado IS 'Campo contém o valor esperado para a resposta do campo que deve ativar a validação específica';
COMMENT ON COLUMN siconv.vrpl_pergunta.grupo_fk IS 'FK para Grupo a que a Pergunta se refere';
COMMENT ON COLUMN siconv.vrpl_pergunta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_pergunta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_pergunta.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_pergunta.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_pergunta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_pergunta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_pergunta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_po_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_po (
    id bigint DEFAULT nextval('siconv.vrpl_po_seq'::regclass) NOT NULL,
    id_po_analise bigint NOT NULL,
    submeta_fk bigint NOT NULL,
    dt_previsao_inicio_obra date NOT NULL,
    qt_meses_duracao_obra smallint NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    in_acompanhamento_eventos boolean,
    in_desonerado boolean,
    sg_localidade varchar(2),
    dt_base date DEFAULT '2019-01-01'::date NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL
);

COMMENT ON TABLE siconv.vrpl_po IS 'Tabela que representa as mudancas das POs. no VRPL';
COMMENT ON COLUMN siconv.vrpl_po.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_po.submeta_fk IS 'Chave estrangeira para Submeta';
COMMENT ON COLUMN siconv.vrpl_po.dt_previsao_inicio_obra IS 'Data de previsao do inicio da obra';
COMMENT ON COLUMN siconv.vrpl_po.qt_meses_duracao_obra IS 'Quantidade de meses de duracao da obra';
COMMENT ON COLUMN siconv.vrpl_po.in_acompanhamento_eventos IS 'Indica se PO acompanhada por eventos';
COMMENT ON COLUMN siconv.vrpl_po.in_desonerado IS 'Indica se PO desonerada';
COMMENT ON COLUMN siconv.vrpl_po.sg_localidade IS 'Sigla da localidade';
COMMENT ON COLUMN siconv.vrpl_po.dt_base IS 'Data Base';


CREATE SEQUENCE siconv.vrpl_resposta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE TABLE siconv.vrpl_resposta (
    id bigint DEFAULT nextval('siconv.vrpl_resposta_seq'::regclass) NOT NULL,
    resposta varchar(500),
    pergunta_fk bigint NOT NULL,
    laudo_fk bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar
);


COMMENT ON TABLE siconv.vrpl_resposta IS 'Tabela que representa as Respostas';
COMMENT ON COLUMN siconv.vrpl_resposta.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_resposta.resposta IS 'Resposta da pergunta';
COMMENT ON COLUMN siconv.vrpl_resposta.pergunta_fk IS 'FK para a Pergunta a que a Resposta se refere';
COMMENT ON COLUMN siconv.vrpl_resposta.laudo_fk IS 'FK para o Laudo a que a Resposta se refere';
COMMENT ON COLUMN siconv.vrpl_resposta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_resposta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_resposta.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_resposta.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_resposta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_resposta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_resposta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';


CREATE SEQUENCE siconv.vrpl_servico_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_servico (
    id bigint DEFAULT nextval('siconv.vrpl_servico_seq'::regclass) NOT NULL,
    tx_observacao varchar(500),
    macro_servico_fk bigint NOT NULL,
    nr_servico smallint NOT NULL,
    cd_servico varchar(13) NOT NULL,
    tx_descricao varchar(508) NOT NULL,
    sg_unidade varchar(10) NOT NULL,
    vl_custo_unitario_ref numeric(17,2) DEFAULT 0 NOT NULL,
    pc_bdi numeric(17,2) DEFAULT 0 NOT NULL,
    qt_total_itens numeric(17,2) DEFAULT 0 NOT NULL,
    vl_custo_unitario numeric(17,2) DEFAULT 0 NOT NULL,
    vl_preco_unitario numeric(17,2) DEFAULT 0 NOT NULL,
    vl_preco_total numeric(17,2) DEFAULT 0 NOT NULL,
    pc_bdi_licitado numeric(17,2) DEFAULT 0 NOT NULL,
    vl_preco_unitario_licitado numeric(17,2) DEFAULT 0 NOT NULL,
    evento_fk bigint,
    in_fonte character(3) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    id_servico_analise bigint NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);

COMMENT ON TABLE siconv.vrpl_servico IS 'Tabela que representa os Servicos';
COMMENT ON COLUMN siconv.vrpl_servico.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_servico.tx_observacao IS 'Texto de observacao do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.macro_servico_fk IS 'Chave estrangeira para Macro Servico';
COMMENT ON COLUMN siconv.vrpl_servico.nr_servico IS 'Numero do servico';
COMMENT ON COLUMN siconv.vrpl_servico.cd_servico IS 'Codigo do servico';
COMMENT ON COLUMN siconv.vrpl_servico.tx_descricao IS 'Texto de descricao do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.sg_unidade IS 'Sigla da unidade do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.vl_custo_unitario_ref IS 'Valor do custo unitario referencia do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.pc_bdi IS 'Percentual do BDI do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.qt_total_itens IS 'Quantidade total de itens do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.vl_custo_unitario IS 'Valor do custo unitario do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.vl_preco_unitario IS 'Valor do preco unitario do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.vl_preco_total IS 'Valor do preco total do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.evento_fk IS 'Chave estrangeira para Evento';
COMMENT ON COLUMN siconv.vrpl_servico.in_fonte IS 'Indicador de fonte do Servico';
COMMENT ON COLUMN siconv.vrpl_servico.id_servico_analise IS 'Id de análise';


CREATE TABLE siconv.vrpl_servico_frente_obra (
    servico_fk bigint NOT NULL,
    frente_obra_fk bigint NOT NULL,
    qt_itens numeric(17,2) NOT NULL,
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    qt_itens_analise numeric(17,2) DEFAULT 0 NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);


COMMENT ON TABLE siconv.vrpl_servico_frente_obra IS 'Tabela que representa o relacionamento MxN entre Servico e Frente Obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra.servico_fk IS 'Chave estrangeira para Servico';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra.frente_obra_fk IS 'Chave estrangeira para Frente Obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra.qt_itens IS 'Quantidade de itens';



CREATE SEQUENCE siconv.vrpl_subitem_investimento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_subitem_investimento (
    id bigint DEFAULT nextval('siconv.vrpl_subitem_investimento_seq'::regclass) NOT NULL,
    id_subitem_analise bigint NOT NULL,
    descricao varchar(1024) NOT NULL,
    in_projeto_social varchar(4),
    codigo_und varchar(1024),
    descricao_und varchar(1024),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);

COMMENT ON TABLE siconv.vrpl_subitem_investimento IS 'Tabela que representa um Subitem investimento';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.id_subitem_analise IS 'Id do item de análise original';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.descricao IS 'Descrição do subitem de investimento';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.in_projeto_social IS 'Indicador do tipo de projeto social';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.codigo_und IS 'Código da unidade do item';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.descricao_und IS 'Descricação da unidade do item';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.versao IS 'Versão para controle de concorrência';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_subitem_investimento.versao_nm_evento IS 'Nome do evento que gerou versão';


CREATE SEQUENCE siconv.vrpl_submeta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_submeta (
    id bigint DEFAULT nextval('siconv.vrpl_submeta_seq'::regclass) NOT NULL,
    id_submeta_analise bigint NOT NULL,
    proposta_fk bigint NOT NULL,
    vrpl_lote_licitacao_fk bigint NOT NULL,
    meta_fk bigint NOT NULL,
    vl_repasse numeric(17,2) DEFAULT 0 NOT NULL,
    vl_outros numeric(17,2) DEFAULT 0 NOT NULL,
    nr_submeta_analise integer NOT NULL,
    tx_descricao_analise varchar(100) NOT NULL,
    nr_lote_analise smallint NOT NULL,
    vl_repasse_analise numeric(17,2) NOT NULL,
    vl_contrapartida_analise numeric(17,2) NOT NULL,
    vl_outros_analise numeric(17,2),
    vl_total_analise numeric(17,2) NOT NULL,
    in_situacao_analise varchar(3) NOT NULL,
    in_regime_execucao_analise varchar(3),
    natureza_despesa_sub_it_fk_analise bigint NOT NULL,
    adt_login varchar(60) NOT NULL,
    adt_data_hora timestamp without time zone NOT NULL,
    adt_operacao varchar(6) NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    in_regime_execucao varchar(3) DEFAULT 'EPG'::varchar NOT NULL,
    vl_contrapartida numeric(17,2) DEFAULT 0 NOT NULL,
    vl_total_licitado numeric(17,2) DEFAULT 0 NOT NULL,
    in_situacao varchar(3) DEFAULT 'ELA'::varchar NOT NULL,
    CONSTRAINT ck_vrpl_submeta_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])))
);

COMMENT ON COLUMN siconv.vrpl_submeta.id IS 'identificador da tabela';
COMMENT ON COLUMN siconv.vrpl_submeta.id_submeta_analise IS 'identificador do registro relativa a tabela de analise, acffo_submeta';
COMMENT ON COLUMN siconv.vrpl_submeta.vrpl_lote_licitacao_fk IS 'chave para tabela de lote';
COMMENT ON COLUMN siconv.vrpl_submeta.meta_fk IS 'chave para tabela de Meta';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_repasse IS 'valor do repasse';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_outros IS 'valor dos outros custos';
COMMENT ON COLUMN siconv.vrpl_submeta.nr_submeta_analise IS 'Numero da Submeta da fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.proposta_fk IS 'Proposta de origem da Submeta';
COMMENT ON COLUMN siconv.vrpl_submeta.tx_descricao_analise IS 'Texto da Descricao da fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.nr_lote_analise IS 'Numero do lote na fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_repasse_analise IS 'valor do repasse na fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_contrapartida_analise IS 'valor da contrapartida na fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_outros_analise IS 'valor outros custos na fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_total_analise IS 'valor do custo total na fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.in_situacao_analise IS 'indicador da situacao da fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.in_regime_execucao_analise IS 'indicador do regime de execucao da fase de analise';
COMMENT ON COLUMN siconv.vrpl_submeta.natureza_despesa_sub_it_fk_analise IS 'chave para tabela natureza de despesa';
COMMENT ON COLUMN siconv.vrpl_submeta.versao IS 'Versão para controle de concorrência';
COMMENT ON COLUMN siconv.vrpl_submeta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_submeta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_submeta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_submeta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_submeta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_submeta.versao_nm_evento IS 'Nome do evento que gerou versão';
COMMENT ON COLUMN siconv.vrpl_submeta.in_regime_execucao IS 'Indicador do Regime de Execução';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_contrapartida IS 'valor da contrapartida';
COMMENT ON COLUMN siconv.vrpl_submeta.vl_total_licitado IS 'valor total licitado';
COMMENT ON COLUMN siconv.vrpl_submeta.in_situacao IS 'indicador da situacao da submetas';

CREATE SEQUENCE siconv.vrpl_template_laudo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE siconv.vrpl_template_laudo (
    id bigint DEFAULT nextval('siconv.vrpl_template_laudo_seq'::regclass) NOT NULL,
    tipo varchar(10),
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3),
    versao bigint DEFAULT 0 NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar
);

COMMENT ON TABLE siconv.vrpl_template_laudo IS 'Tabela que representa os templates do Laudo no VRPL';
COMMENT ON COLUMN siconv.vrpl_template_laudo.id IS 'Coluna de id';
COMMENT ON COLUMN siconv.vrpl_template_laudo.tipo IS 'Tipo do Template do laudo';
COMMENT ON COLUMN siconv.vrpl_template_laudo.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_template_laudo.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_template_laudo.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_template_laudo.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_template_laudo.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_template_laudo.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_template_laudo.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';



ALTER TABLE ONLY siconv.vrpl_anexo
    ADD CONSTRAINT vrpl_anexo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_evento_frente_obra
    ADD CONSTRAINT vrpl_evento_frente_obra_pkey PRIMARY KEY (evento_fk, frente_obra_fk);

ALTER TABLE ONLY siconv.vrpl_evento
    ADD CONSTRAINT vrpl_evento_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_frente_obra
    ADD CONSTRAINT vrpl_frente_obra_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_grupo_pergunta
    ADD CONSTRAINT vrpl_grupo_pergunta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_historico_licitacao
    ADD CONSTRAINT vrpl_historico_licitacao_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_laudo_grupo_pergunta
    ADD CONSTRAINT vrpl_laudo_grupo_pergunta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_laudo
    ADD CONSTRAINT vrpl_laudo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_licitacao
    ADD CONSTRAINT vrpl_licitacao_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_lote_licitacao
    ADD CONSTRAINT vrpl_lote_licitacao_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_macro_servico_parcela
    ADD CONSTRAINT vrpl_macro_servico_parcela_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_meta
    ADD CONSTRAINT vrpl_meta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_macro_servico
    ADD CONSTRAINT vrpl_macro_servico_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_pendencia
    ADD CONSTRAINT vrpl_pendencia_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_pergunta
    ADD CONSTRAINT vrpl_pergunta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_po
    ADD CONSTRAINT vrpl_po_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_resposta
    ADD CONSTRAINT vrpl_resposta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_servico_frente_obra
    ADD CONSTRAINT vrpl_servico_frente_obra_pkey PRIMARY KEY (servico_fk, frente_obra_fk);

ALTER TABLE ONLY siconv.vrpl_servico
    ADD CONSTRAINT vrpl_servico_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_subitem_investimento
    ADD CONSTRAINT vrpl_subitem_investimento_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_submeta
    ADD CONSTRAINT vrpl_submeta_pkey PRIMARY KEY (id);

ALTER TABLE ONLY siconv.vrpl_template_laudo
    ADD CONSTRAINT vrpl_template_laudo_pkey PRIMARY KEY (id);

CREATE INDEX ind_vrpl_evento_po_fk ON siconv.vrpl_evento USING btree (po_fk);

CREATE INDEX ind_vrpl_frente_obra_po_fk ON siconv.vrpl_frente_obra USING btree (po_fk);

CREATE INDEX ind_vrpl_servico_macro_servico_fk ON siconv.vrpl_servico USING btree (macro_servico_fk);

CREATE INDEX ind_vrpl_grupo_pergunta_template_fk ON siconv.vrpl_grupo_pergunta USING btree (template_fk);

CREATE INDEX ind_vrpl_laudo_template_fk ON siconv.vrpl_laudo USING btree (template_fk);

CREATE INDEX ind_vrpl_pergunta_grupo_fk ON siconv.vrpl_pergunta USING btree (grupo_fk);

CREATE INDEX ind_vrpl_resposta_pergunta_fk ON siconv.vrpl_resposta USING btree (pergunta_fk);

CREATE INDEX ind_vrpl_po_submeta_fk_idx ON siconv.vrpl_po USING btree (submeta_fk);

ALTER TABLE ONLY siconv.vrpl_licitacao
    ADD CONSTRAINT fkc_vrpl_licitacao_proposta FOREIGN KEY (proposta_fk) REFERENCES siconv.vrpl_proposta(id);

ALTER TABLE ONLY siconv.vrpl_fornecedor
    ADD CONSTRAINT fkc_vrpl_forcenedor_licitacao FOREIGN KEY (licitacao_fk) REFERENCES siconv.vrpl_licitacao(id);

ALTER TABLE ONLY siconv.vrpl_historico_licitacao
    ADD CONSTRAINT fkc_historico_licitacao FOREIGN KEY (licitacao_fk) REFERENCES siconv.vrpl_licitacao(id);

ALTER TABLE ONLY siconv.vrpl_lote_licitacao
    ADD CONSTRAINT fkc_lote_licitacao_licitacao FOREIGN KEY (licitacao_fk) REFERENCES siconv.vrpl_licitacao(id);

ALTER TABLE ONLY siconv.vrpl_lote_licitacao
    ADD CONSTRAINT fkc_lote_licitacao_forcenedor FOREIGN KEY (fornecedor_fk) REFERENCES siconv.vrpl_fornecedor(id);

ALTER TABLE ONLY siconv.vrpl_anexo
    ADD CONSTRAINT fkc_vrpl_anexo_licitacao FOREIGN KEY (vrpl_licitacao_fk) REFERENCES siconv.vrpl_licitacao(id);

ALTER TABLE ONLY siconv.vrpl_evento
    ADD CONSTRAINT fkc_vrpl_evento_vrpl_po FOREIGN KEY (po_fk) REFERENCES siconv.vrpl_po(id);

ALTER TABLE ONLY siconv.vrpl_frente_obra
    ADD CONSTRAINT fkc_vrpl_frente_obra_vrpl_po FOREIGN KEY (po_fk) REFERENCES siconv.vrpl_po(id);

ALTER TABLE ONLY siconv.vrpl_pergunta
    ADD CONSTRAINT fkc_vrpl_grupo_pergunta_vrpl_pergunta FOREIGN KEY (grupo_fk) REFERENCES siconv.vrpl_grupo_pergunta(id);

ALTER TABLE ONLY siconv.vrpl_laudo_grupo_pergunta
    ADD CONSTRAINT fkc_vrpl_laudo_grupo_pergunta_vrpl_grupo_pergunta_fk FOREIGN KEY (grupo_pergunta_fk) REFERENCES siconv.vrpl_grupo_pergunta(id);

ALTER TABLE ONLY siconv.vrpl_laudo_grupo_pergunta
    ADD CONSTRAINT fkc_vrpl_laudo_grupo_pergunta_vrpl_laudo_fk FOREIGN KEY (laudo_fk) REFERENCES siconv.vrpl_laudo(id);

ALTER TABLE ONLY siconv.vrpl_laudo
    ADD CONSTRAINT fkc_vrpl_laudo_vrpl_licitacao FOREIGN KEY (licitacao_fk) REFERENCES siconv.vrpl_licitacao(id);

ALTER TABLE ONLY siconv.vrpl_laudo
    ADD CONSTRAINT fkc_vrpl_laudo_vrpl_template_laudo FOREIGN KEY (template_fk) REFERENCES siconv.vrpl_template_laudo(id);

ALTER TABLE ONLY siconv.vrpl_servico
    ADD CONSTRAINT fkc_vrpl_macro_servico_vrpl_servico FOREIGN KEY (macro_servico_fk) REFERENCES siconv.vrpl_macro_servico(id);

ALTER TABLE ONLY siconv.vrpl_macro_servico
    ADD CONSTRAINT fkc_vrpl_nivel_vrpl_po FOREIGN KEY (po_fk) REFERENCES siconv.vrpl_po(id);

ALTER TABLE ONLY siconv.vrpl_pendencia
    ADD CONSTRAINT fkc_vrpl_pendencia_laudo FOREIGN KEY (laudo_fk) REFERENCES siconv.vrpl_laudo(id);

ALTER TABLE ONLY siconv.vrpl_resposta
    ADD CONSTRAINT fkc_vrpl_resposta_vrpl_laudo FOREIGN KEY (laudo_fk) REFERENCES siconv.vrpl_laudo(id);

ALTER TABLE ONLY siconv.vrpl_resposta
    ADD CONSTRAINT fkc_vrpl_resposta_vrpl_pergunta FOREIGN KEY (pergunta_fk) REFERENCES siconv.vrpl_pergunta(id);

ALTER TABLE ONLY siconv.vrpl_grupo_pergunta
    ADD CONSTRAINT fkc_vrpl_template_laudo_vrpl_grupo_pergunta FOREIGN KEY (template_fk) REFERENCES siconv.vrpl_template_laudo(id);

ALTER TABLE ONLY siconv.vrpl_meta
    ADD CONSTRAINT meta_subitem_fk FOREIGN KEY (subitem_fk) REFERENCES siconv.vrpl_subitem_investimento(id);

ALTER TABLE ONLY siconv.vrpl_evento_frente_obra
    ADD CONSTRAINT vrpl_evento_frente_obra_vrpl_evento_fk FOREIGN KEY (evento_fk) REFERENCES siconv.vrpl_evento(id);

ALTER TABLE ONLY siconv.vrpl_evento_frente_obra
    ADD CONSTRAINT vrpl_evento_frente_obra_vrpl_frente_obra_fk FOREIGN KEY (frente_obra_fk) REFERENCES siconv.vrpl_frente_obra(id);

ALTER TABLE ONLY siconv.vrpl_macro_servico_parcela
    ADD CONSTRAINT vrpl_macro_servico_parcela_vrpl_macro_servico_fk FOREIGN KEY (macro_servico_fk) REFERENCES siconv.vrpl_macro_servico(id);

ALTER TABLE ONLY siconv.vrpl_po
    ADD CONSTRAINT vrpl_po_vrpl_submeta_fk FOREIGN KEY (submeta_fk) REFERENCES siconv.vrpl_submeta(id);

ALTER TABLE ONLY siconv.vrpl_servico_frente_obra
    ADD CONSTRAINT vrpl_servico_frente_obra_vrpl_frente_obra_fk FOREIGN KEY (frente_obra_fk) REFERENCES siconv.vrpl_frente_obra(id);

ALTER TABLE ONLY siconv.vrpl_servico_frente_obra
    ADD CONSTRAINT vrpl_servico_frente_obra_vrpl_servico_fk FOREIGN KEY (servico_fk) REFERENCES siconv.vrpl_servico(id);

ALTER TABLE ONLY siconv.vrpl_servico
    ADD CONSTRAINT vrpl_servico_vrpl_evento_fk FOREIGN KEY (evento_fk) REFERENCES siconv.vrpl_evento(id);

ALTER TABLE ONLY siconv.vrpl_submeta
    ADD CONSTRAINT fkc_vrpl_submeta_proposta FOREIGN KEY (proposta_fk) REFERENCES siconv.vrpl_proposta(id);

ALTER TABLE ONLY siconv.vrpl_submeta
    ADD CONSTRAINT vrpl_submeta_meta_fk_fkey FOREIGN KEY (meta_fk) REFERENCES siconv.vrpl_meta(id);

ALTER TABLE ONLY siconv.vrpl_submeta
    ADD CONSTRAINT vrpl_submeta_vrpl_lote_licitacao_fk_fkey FOREIGN KEY (vrpl_lote_licitacao_fk) REFERENCES siconv.vrpl_lote_licitacao(id);

