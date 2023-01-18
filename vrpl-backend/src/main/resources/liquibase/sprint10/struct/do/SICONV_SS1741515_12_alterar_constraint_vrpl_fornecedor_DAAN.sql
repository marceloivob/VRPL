ALTER TABLE siconv.vrpl_fornecedor DROP CONSTRAINT vrpl_fornecedor_tipo_identificacao;

ALTER TABLE siconv.vrpl_fornecedor ADD CONSTRAINT vrpl_fornecedor_tipo_identificacao CHECK (((tipo_identificacao)::text = ANY (ARRAY[('CPF'::character varying)::text, ('CNPJ'::character varying)::text, ('IG'::character varying)::text])))
