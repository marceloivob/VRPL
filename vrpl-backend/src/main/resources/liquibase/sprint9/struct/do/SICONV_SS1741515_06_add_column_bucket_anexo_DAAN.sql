ALTER TABLE siconv.vrpl_anexo ADD bucket varchar(25) NOT NULL;

COMMENT ON COLUMN siconv.vrpl_anexo.bucket IS 'Indicador do repositório dos arquivos';