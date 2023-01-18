ALTER TABLE siconv.vrpl_meta ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_meta ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_meta ALTER COLUMN adt_operacao SET NOT NULL;

ALTER TABLE siconv.vrpl_meta ALTER COLUMN adt_login TYPE varchar(60);
ALTER TABLE siconv.vrpl_meta ALTER COLUMN adt_operacao TYPE varchar(6);

ALTER TABLE siconv.vrpl_meta ADD CONSTRAINT ck_vrpl_meta_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])));

COMMENT ON COLUMN siconv.vrpl_meta.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_meta.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_meta.versao_nm_evento IS 'Nome do evento que deu origem à nova versão';
COMMENT ON COLUMN siconv.vrpl_meta.versao IS 'Versão usada para controlar a concorrência';

COMMENT ON COLUMN siconv.vrpl_meta.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_meta.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_meta.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';

COMMENT ON COLUMN siconv.vrpl_meta.tx_descricao_analise IS 'Texto de descrição da meta da Análise';
COMMENT ON COLUMN siconv.vrpl_meta.nr_meta_analise IS 'Número da meta da Análise';
COMMENT ON COLUMN siconv.vrpl_meta.qt_itens_analise IS 'Quantidade de itens da meta da Análise';
COMMENT ON COLUMN siconv.vrpl_meta.in_social IS 'Indicador que define se a meta é de projeto social';
