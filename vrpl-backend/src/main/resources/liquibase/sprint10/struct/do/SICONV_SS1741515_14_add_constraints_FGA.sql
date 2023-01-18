SELECT set_config('vrpl.cpf_usuario', 'SISTEMA', true);

ALTER TABLE siconv.vrpl_laudo ADD CONSTRAINT vrpl_laudo_in_resultado CHECK (((in_resultado)::text = ANY (ARRAY[('VI'::character varying)::text, ('IN'::character varying)::text])));
ALTER TABLE siconv.vrpl_laudo ADD CONSTRAINT vrpl_laudo_in_status CHECK ((in_status = ANY (ARRAY[(1)::int8, (2)::int8])));

ALTER TABLE siconv.vrpl_fornecedor ADD CONSTRAINT ck_vrpl_fornecedor_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::character varying)::text, ('UPDATE'::character varying)::text, ('DELETE'::character varying)::text])));
ALTER TABLE siconv.vrpl_template_laudo ADD CONSTRAINT ck_vrpl_template_laudo_adt_operacao CHECK (((adt_operacao)::text = ANY (ARRAY[('INSERT'::character varying)::text, ('UPDATE'::character varying)::text, ('DELETE'::character varying)::text])));

UPDATE siconv.vrpl_fornecedor 
	SET adt_login = 'SISTEMA', adt_data_hora = current_timestamp, adt_operacao = 'UPDATE', versao = versao+1 
	WHERE adt_login IS NULL;

ALTER TABLE siconv.vrpl_fornecedor ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_fornecedor ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_fornecedor ALTER COLUMN adt_operacao SET NOT NULL;

UPDATE siconv.vrpl_template_laudo 
	SET adt_login = 'SISTEMA', adt_data_hora = current_timestamp, adt_operacao = 'UPDATE', versao = versao+1 
	WHERE adt_login IS NULL;

ALTER TABLE siconv.vrpl_template_laudo ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_template_laudo ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_template_laudo ALTER COLUMN adt_operacao SET NOT NULL;

UPDATE siconv.vrpl_frente_obra 
	SET adt_login = 'SISTEMA', adt_data_hora = current_timestamp, adt_operacao = 'UPDATE', versao = versao+1 
	WHERE adt_login IS NULL;

ALTER TABLE siconv.vrpl_frente_obra ALTER COLUMN adt_login SET NOT NULL;
ALTER TABLE siconv.vrpl_frente_obra ALTER COLUMN adt_data_hora SET NOT NULL;
ALTER TABLE siconv.vrpl_frente_obra ALTER COLUMN adt_operacao SET NOT NULL;

COMMIT;