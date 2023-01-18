ALTER TABLE siconv.vrpl_anexo ALTER COLUMN in_tipoanexo TYPE varchar(30);
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN in_tipoanexo SET NOT NULL;
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN in_perfil SET NOT NULL;
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN adt_operacao TYPE varchar(6);
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN adt_operacao SET NOT NULL;
ALTER TABLE siconv.vrpl_anexo ADD COLUMN versao BIGINT NOT NULL;

ALTER TABLE siconv.vrpl_anexo DROP CONSTRAINT ck_vrpl_anexo_in_tipoanexo;
ALTER TABLE siconv.vrpl_anexo ADD CONSTRAINT ck_vrpl_anexo_in_tipoanexo CHECK (((in_tipoanexo)::text = ANY (ARRAY[('ATA_HOMOLOGACAO_LICITACAO'::varchar)::text, ('DESPACHO_ADJUDICACAO'::varchar)::text, ('RESUMO_EDITAL'::varchar)::text, ('OUTROS'::varchar)::text])));

-- Em consulta realizada em produção em (14/05/2019), o maior nome possuia 60 caracteres: select max(length(nome)) from membro_partc;
ALTER TABLE siconv.vrpl_anexo ALTER COLUMN nome_enviadopor TYPE varchar(60);

ALTER TABLE siconv.vrpl_anexo ADD CONSTRAINT ck_vrpl_anexo_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])));

COMMENT ON COLUMN siconv.vrpl_anexo.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_anexo.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_anexo.versao_nm_evento IS 'Nome do evento que deu origem à nova versão';
COMMENT ON COLUMN siconv.vrpl_anexo.versao IS 'Versão usada para controlar a concorrência';

COMMENT ON COLUMN siconv.vrpl_anexo.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_anexo.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_anexo.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
