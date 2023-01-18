ALTER TABLE siconv.vrpl_servico ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_servico ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_servico ALTER COLUMN adt_operacao SET NOT NULL;

ALTER TABLE siconv.vrpl_servico ALTER COLUMN adt_login TYPE varchar(60);
ALTER TABLE siconv.vrpl_servico ALTER COLUMN adt_operacao TYPE varchar(6);

ALTER TABLE siconv.vrpl_servico ADD CONSTRAINT ck_vrpl_servico_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::varchar)::text, ('UPDATE'::varchar)::text, ('DELETE'::varchar)::text])));

COMMENT ON COLUMN siconv.vrpl_servico.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';
COMMENT ON COLUMN siconv.vrpl_servico.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_servico.versao_nm_evento IS 'Nome do evento que deu origem à nova versão';
COMMENT ON COLUMN siconv.vrpl_servico.versao IS 'Versão usada para controlar a concorrência';

COMMENT ON COLUMN siconv.vrpl_servico.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_servico.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_servico.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';

ALTER TABLE siconv.vrpl_servico RENAME COLUMN vl_custo_unitario_ref TO vl_custo_unitario_ref_analise;
ALTER TABLE siconv.vrpl_servico RENAME COLUMN pc_bdi TO pc_bdi_analise;
ALTER TABLE siconv.vrpl_servico RENAME COLUMN qt_total_itens TO qt_total_itens_analise;
ALTER TABLE siconv.vrpl_servico RENAME COLUMN vl_custo_unitario TO vl_custo_unitario_analise;
ALTER TABLE siconv.vrpl_servico RENAME COLUMN vl_preco_unitario TO vl_preco_unitario_analise;
ALTER TABLE siconv.vrpl_servico RENAME COLUMN vl_preco_total TO vl_preco_total_analise;

COMMENT ON COLUMN siconv.vrpl_servico.vl_custo_unitario_ref_analise IS 'Valor do custo unitário referência na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.pc_bdi_analise IS 'Percentual do BDI na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.qt_total_itens_analise IS 'Quantidade total de itens na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.vl_custo_unitario_analise IS 'Valor do custo unitário na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.vl_preco_unitario_analise IS 'Valor do preço unitário na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.vl_preco_total_analise IS 'Valor do preço total na fase de análise';
COMMENT ON COLUMN siconv.vrpl_servico.pc_bdi_licitado IS 'Percentual do BDI licitado';
COMMENT ON COLUMN siconv.vrpl_servico.vl_preco_unitario_licitado IS 'Valor do preço unitário licitado';