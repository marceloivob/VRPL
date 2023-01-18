ALTER TABLE siconv.vrpl_anexo DROP CONSTRAINT ck_vrpl_anexo_in_tipoanexo;

-- Adicionando: QUADRO_RESUMO e VRPL no constraint

ALTER TABLE siconv.vrpl_anexo ADD CONSTRAINT ck_vrpl_anexo_in_tipoanexo CHECK (((in_tipoanexo)::text = ANY (ARRAY[('ATA_HOMOLOGACAO_LICITACAO'::character varying)::text, 
                                                                                                                    ('DESPACHO_ADJUDICACAO'::character varying)::text,
                                                                                                                    ('RESUMO_EDITAL'::character varying)::text, 
                                                                                                                    ('OUTROS'::character varying)::text, 
                                                                                                                    ('QUADRO_RESUMO'::character varying)::text, 
                                                                                                                    ('VRPL'::character varying)::text])));