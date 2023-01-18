ALTER TABLE siconv.vrpl_resposta ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_resposta ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_resposta ALTER COLUMN adt_operacao SET NOT NULL;

ALTER TABLE siconv.vrpl_resposta ALTER COLUMN adt_login TYPE varchar(60);
ALTER TABLE siconv.vrpl_resposta ALTER COLUMN adt_operacao TYPE varchar(6);

ALTER TABLE siconv.vrpl_resposta ADD CONSTRAINT ck_vrpl_resposta_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])));

COMMENT ON COLUMN siconv.vrpl_resposta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_resposta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_resposta.versao_nm_evento IS 'Nome do evento que deu origem à nova versão';
COMMENT ON COLUMN siconv.vrpl_resposta.versao IS 'Versão usada para controlar a concorrência';

COMMENT ON COLUMN siconv.vrpl_resposta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_resposta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_resposta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
