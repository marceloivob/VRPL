/*
* ======================= DADOS DO SCRIPT ====================================
* AUTOR: Carlos Eugênio Palma da Purificação
* OBJETIVO: Cria a tabela ORG_ADMINISTRATIVO para execução de testes
* PRE-REQUISITOS: N/A
*/
CREATE TABLE "siconv"."org_administrativo"
(
   id bigint PRIMARY KEY NOT NULL,
   hibernate_version int DEFAULT 0 NOT NULL,
   codigo varchar(1024) NOT NULL,
   nome varchar(1024) NOT NULL,
   org_superior_fk bigint,
   tipo_orgao varchar(1024) DEFAULT '1'::character varying NOT NULL,
   adt_operacao varchar(12) DEFAULT 'ND'::character varying NOT NULL,
   adt_data_hora timestamp DEFAULT now() NOT NULL,
   adt_login varchar(30) DEFAULT 'ND'::character varying NOT NULL,
   instituicao_mandataria bool DEFAULT false NOT NULL,
   banco_fk bigint,
   bancolog varchar(1024),
   membro_padr_fk bigint,
   mandatario_define_agencia_na_a bool DEFAULT false,
   recebe_proposta_via_siconv bool DEFAULT true NOT NULL,
   info_envio_prop_fora_siconv varchar(1024),
   cod_siorg varchar(20)
);
