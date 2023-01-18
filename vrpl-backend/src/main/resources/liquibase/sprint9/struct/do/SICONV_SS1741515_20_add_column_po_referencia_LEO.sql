ALTER TABLE siconv.vrpl_po ADD COLUMN referencia varchar(8) DEFAULT 'analise';
ALTER TABLE siconv.vrpl_po ADD CONSTRAINT ck_vrpl_po_referencia CHECK (((referencia)::text = ANY (ARRAY[('analise'::varchar)::text, ('database'::varchar)::text])));

COMMENT ON COLUMN siconv.vrpl_po.referencia IS 'Referência (analise/database) para apresentação dos preços dos serviços';
